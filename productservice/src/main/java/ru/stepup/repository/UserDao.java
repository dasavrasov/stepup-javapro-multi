package ru.stepup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.stepup.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<User, Long> {
    public User save(User user);

    public Optional<User> findById(Long id);

    public void deleteById(Long id);

    public List<User> findAll();
}