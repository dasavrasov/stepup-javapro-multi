package ru.stepup.controller;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.stepup.entity.Product;
import ru.stepup.model.ErrorResponse;
import ru.stepup.service.ProductService;
import ru.stepup.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final UserService userService;

    public ProductController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.findById(id);
        if (product == null) {
            throw new EmptyResultDataAccessException(1);
        }
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<?> getProductsByUserIdAndAccount(@RequestParam Long userId, @RequestParam(required = false) String account) {
        if (account == null) {
            List<Product> products = productService.findByUser(userService.findById(userId));
            return new ResponseEntity<>(products, HttpStatus.OK);
        } else {
            Product product = productService.findByUserAndAccount(userService.findById(userId), account);
            return new ResponseEntity<>(product, HttpStatus.OK);
        }
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<ErrorResponse> handleEmptyResultDataAccessException(EmptyResultDataAccessException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("Продукт не найден");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("Ошибка ProductService: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}