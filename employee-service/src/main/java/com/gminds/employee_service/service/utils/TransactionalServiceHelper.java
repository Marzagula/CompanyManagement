package com.gminds.employee_service.service.utils;

import com.gminds.employee_service.exceptions.TransactionException;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
public class TransactionalServiceHelper {
    
    public <T> T executeInTransaction(Supplier<T> action) {
        try {
            return action.get();
        } catch (Exception e) {
            throw new TransactionException("Transaction failed", e);
        }
    }
}