package ru.stepup.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.stepup.model.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@RestController
@RequestMapping("/pay")
public class PaymentController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${product.service.url}")
    private String productServiceUrl;

    @Value("${limit.service.url}")
    private String limitServiceUrl;

    @GetMapping("/products/{id}")
    public ResponseEntity<?> payForProduct(@PathVariable Long id) {
        String url = productServiceUrl + id;
        ResponseEntity<ProductResponse> response = restTemplate.getForEntity(url, ProductResponse.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response;
        } else if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
            throw new RestClientException("Продукт с Id " + id + " не найден");
        } else {
            throw new RestClientException("Ошибка получения продукта с Id " + id);
        }
    }

    @PostMapping("/newPayment")
    public ResponseEntity<?> newPayment(@RequestBody PaymentRequest paymentRequest) {
        Long userId = paymentRequest.getUser().getId();
        BigDecimal paymentSumma = paymentRequest.getAmount();
        // Проеряем лимит
        ResponseEntity<Limit> limitResponse = restTemplate.getForEntity(limitServiceUrl + "/limit/" + userId, Limit.class);
        if (limitResponse.getStatusCode() != HttpStatus.OK) {
            throw new RestClientException("Ошибка получения лимита user id " + userId);
        }
        BigDecimal limit = limitResponse.getBody().getValue();
        if (limit.compareTo(paymentSumma) < 0) {
            throw new RestClientException("Превышен лимит на платежи");
        }

        String url = productServiceUrl + "/user?userId=" + paymentRequest.getUser().getId() + "&account=" + paymentRequest.getAccount();
        ResponseEntity<Product> response = restTemplate.getForEntity(url, Product.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            Product product = response.getBody();
            if (product.getBalance().compareTo(paymentRequest.getAmount()) < 0) {
                throw new RestClientException("Недостаточно средств на счете для оплаты");
            } else {
                // Уменьшаем лимит
                restTemplate.postForEntity(limitServiceUrl + "/reducelimit?userId=" + userId + "&summa=" + paymentSumma, null, Void.class);
                // Уменьшаем баланс
                try {
                    restTemplate.postForEntity(productServiceUrl + "/reducebalance?userId=" + userId + "&account=" + paymentRequest.getAccount() + "&summa=" + paymentSumma, null, Void.class);
                } catch (RestClientException e) {
                    // Восстанавливаем лимит
                    restTemplate.postForEntity(limitServiceUrl + "/restorelimit/" + userId, null, Void.class);
                }
                return ResponseEntity.ok("Платеж успешно проведен");
            }
        } else {
            throw new RestClientException("Нет продукта с такими данными");
        }
    }

    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<ErrorResponse> handleRestClientException(RestClientException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("Ошибка ProductService: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}