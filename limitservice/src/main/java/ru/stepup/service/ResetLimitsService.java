package ru.stepup.service;

import ru.stepup.model.Limit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.stepup.repository.LimitDao;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ResetLimitsService {

    private static final BigDecimal INITIAL_USER_LIMIT = BigDecimal.valueOf(10000);

    @Autowired
    private LimitDao limitDao;

    @Scheduled(cron = "0 0 0 * * ?")
    public void resetAllLimits() {
        List<Limit> allLimits = limitDao.findAll();
        for (Limit limit : allLimits) {
            limit.setValue(INITIAL_USER_LIMIT);
            limitDao.save(limit);
        }
    }
}