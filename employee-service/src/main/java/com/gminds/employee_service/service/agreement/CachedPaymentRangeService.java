package com.gminds.employee_service.service.agreement;

import com.gminds.employee_service.model.PaymentRange;
import com.gminds.employee_service.repository.PaymentRangeRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class CachedPaymentRangeService {
    private static final Logger logger = LoggerFactory.getLogger(CachedPaymentRangeService.class);
    private final PaymentRangeRepository paymentRangeRepository;
    private List<PaymentRange> cachedPaymentRanges;
    private long lastUpdateTimestamp;

    @Value("${cache.refresh.interval}")
    private Long refreshRate;
    public CachedPaymentRangeService(PaymentRangeRepository paymentRangeRepository) {
        this.paymentRangeRepository = paymentRangeRepository;
        init();
    }

    @PostConstruct
    public void init() {
        refreshCache();
    }

    public synchronized void refreshCache() {
        this.cachedPaymentRanges = paymentRangeRepository.findAll();
        this.lastUpdateTimestamp = System.currentTimeMillis();
        logger.info("Cache of CachedPaymentRangeService has been refreshed.");
    }

    public List<PaymentRange> getCachedPaymentRanges() {
        if (isCacheStale()) {
            refreshCache();
        }
        return cachedPaymentRanges;
    }

    private boolean isCacheStale() {
        long currentTime = System.currentTimeMillis();
        long cacheAge = currentTime - lastUpdateTimestamp;
        long cacheExpiry = refreshRate;
        return cacheAge > cacheExpiry;
    }
}

