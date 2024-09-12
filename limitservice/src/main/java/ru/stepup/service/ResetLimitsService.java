package ru.stepup.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.Transactional;
import ru.stepup.model.Limit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.stepup.repository.LimitDao;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class ResetLimitsService {

    private static final BigDecimal INITIAL_USER_LIMIT = BigDecimal.valueOf(10000);

    @Autowired
    private LimitDao limitDao;

    //для тестирования сбрасываем лимтиты каждые 10 минут
//    @Scheduled(cron = "0 0 0 * * ?")
    @Scheduled(fixedRate = 10 * 60 * 1000)
    @Transactional
    public void resetAllLimits() {
        log.info("Resetting all limits");
        List<Limit> allLimits = limitDao.findAll();
        Set<Long> userIds = new HashSet<>();
        for (Limit limit : allLimits) {
            userIds.add(limit.getUserId());
            limitDao.deleteByUserId(limit.getUserId());
        }
        for (Long userId : userIds) {
            Limit newLimit = new Limit();
            newLimit.setUserId(userId);
            newLimit.setValue(INITIAL_USER_LIMIT);
            limitDao.save(newLimit);
        }
    }
}