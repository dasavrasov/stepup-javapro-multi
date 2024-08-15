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
        try {
            ResponseEntity<ProductResponse> response = restTemplate.getForEntity(url, ProductResponse.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return response;
            } else if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
                ErrorResponse errorResponse = new ErrorResponse();
                errorResponse.setMessage("Продукт с Id " + id + " не найден");
                return ResponseEntity.status(response.getStatusCode()).body(errorResponse);
            } else {
                ErrorResponse errorResponse = new ErrorResponse();
                errorResponse.setMessage("Ошибка получения продукта с Id " + id);
                return ResponseEntity.status(response.getStatusCode()).body(errorResponse);
            }
        } catch (RestClientException e) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage("Ошибка ProductService: " + e.getCause().getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/newPayment")
    public ResponseEntity<?> newPayment(@RequestBody PaymentRequest paymentRequest) {
        String url = productServiceUrl + "/user?userId=" + paymentRequest.getUser().getId() + "&account=" + paymentRequest.getAccount();
        try {
            ResponseEntity<Product> response = restTemplate.getForEntity(url, Product.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                Product product = response.getBody();
                if (product.getBalance().compareTo(paymentRequest.getAmount()) < 0) {
                    ErrorResponse errorResponse = new ErrorResponse();
                    errorResponse.setMessage("Недостаточно средств на счете для оплаты");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
                } else {
                    return ResponseEntity.ok("Платеж успешно проведен");
                }
            } else {
                ErrorResponse errorResponse = new ErrorResponse();
                errorResponse.setMessage("Нет продукта с такими данными");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
        } catch (RestClientException e) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage("Ошибка ProductService: " + e.getCause().getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}