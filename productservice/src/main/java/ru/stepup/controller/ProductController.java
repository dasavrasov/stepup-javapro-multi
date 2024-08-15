package ru.stepup.controller;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.stepup.entity.Product;
import ru.stepup.model.ErrorResponse;
import ru.stepup.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        try {
            Product product = productService.findById(id);
            return new ResponseEntity<>(product, HttpStatus.OK);
        } catch (EmptyResultDataAccessException e) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage("Продукт с id" + id + " не найден");
            ResponseEntity<ErrorResponse> response=ResponseEntity.status(HttpStatus.NO_CONTENT).body(errorResponse);
            return response;
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage("Ошибка ProductService: " + e.getMessage());
            ResponseEntity<ErrorResponse> response=ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
            return response;
        }
    }

    @GetMapping("/user")
    public ResponseEntity<?> getProductsByUserIdAndAccount(@RequestParam Long userId, @RequestParam(required = false) String account) {
        try {
            if (account == null) {
                List<Product> products = productService.findByUserId(userId);
                return new ResponseEntity<>(products, HttpStatus.OK);
            } else {
                Product product = productService.findByUserIdAndAccount(userId, account);
                return new ResponseEntity<>(product, HttpStatus.OK);
            }
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage("Ошибка ProductService: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}