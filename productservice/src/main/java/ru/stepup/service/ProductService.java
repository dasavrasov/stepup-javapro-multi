package ru.stepup.service;

import org.springframework.stereotype.Service;
import ru.stepup.entity.User;
import ru.stepup.repository.ProductDao;
import ru.stepup.entity.Product;

import java.util.List;

@Service
public class ProductService {

    private final ProductDao productDao;

    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    public Product save(Product user) {
        return productDao.save(user);
    }

    public void deleteById(Long id) {
        productDao.deleteById(id);
    }

    public Product findById(Long id) {
        return productDao.findById(id).orElse(null);
    }

    public Product findByUserAndAccount(User user, String account) {
        return productDao.findByUserAndAccount(user, account);
    }

    public List<Product> findByUser(User user) {
        return productDao.findByUser(user);
    }
}
