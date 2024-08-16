package ru.stepup.repository;

import org.springframework.stereotype.Repository;
import ru.stepup.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.stepup.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductDao extends JpaRepository<Product, Long>{

    @Override
    Optional<Product> findById(Long id);

    Product findByUserAndAccount(User user, String account);

    List<Product> findByUser(User user);

}