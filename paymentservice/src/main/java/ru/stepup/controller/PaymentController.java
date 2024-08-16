package ru.stepup.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.stepup.model.ErrorResponse;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.stepup.model.PaymentRequest;
import ru.stepup.model.Product;
import ru.stepup.model.ProductResponse;

@RestController
@RequestMapping("/pay")
public class PaymentController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${product.service.url}")
    private String productServiceUrl;

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
        String url = productServiceUrl + "/user?userId=" + paymentRequest.getUser().getId() + "&account=" + paymentRequest.getAccount();
        ResponseEntity<Product> response = restTemplate.getForEntity(url, Product.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            Product product = response.getBody();
            if (product.getBalance().compareTo(paymentRequest.getAmount()) < 0) {
                throw new RestClientException("Недостаточно средств на счете для оплаты");
            } else {
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