package ru.stepup.service;

import org.springframework.stereotype.Service;
import ru.stepup.repository.UserDao;
import ru.stepup.entity.User;

import java.util.List;

@Service
public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }
    public User save(User user) {
        return userDao.save(user);
    }

    public User findById(Long id) {
        return userDao.findById(id).orElse(null);
    }

    public void deleteById(Long id) {
        userDao.deleteById(id);
    }

    public List<User> findAll() {
        return userDao.findAll();
    }
}