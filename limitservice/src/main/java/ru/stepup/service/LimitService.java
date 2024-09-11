package ru.stepup.service;

import lombok.extern.slf4j.Slf4j;
import ru.stepup.model.Limit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.stepup.repository.LimitDao;

import java.math.BigDecimal;

@Slf4j
@Service
public class LimitService {

    private static final BigDecimal INITIAL_USER_LIMIT = BigDecimal.valueOf(10000);

    @Autowired
    private LimitDao limitDao;

    public Limit getLimitByUserId(Long userId) {
        try {
            Limit lim=limitDao.findTopByUserIdOrderByCreatedAtDesc(userId);
            return lim;
        } catch (Exception e) {
            log.error("Error while getting limit by user id", e);
            return null;
        }
    }

    public Limit reduceUserLimit(Long userId, BigDecimal summa) {
        Limit limit = getLimitByUserId(userId);
        BigDecimal newValue = (limit == null) ? INITIAL_USER_LIMIT.subtract(summa) : limit.getValue().subtract(summa);
        Limit newLimit = new Limit(userId, newValue);
        return limitDao.save(newLimit);
    }

    public void restoreUserLimit(Long userId) {
        Limit limit = getLimitByUserId(userId);
        if (limit != null) {
            limitDao.delete(limit);
        } else {
            limit = new Limit(userId, INITIAL_USER_LIMIT);
            limitDao.save(limit);
        }
    }
}