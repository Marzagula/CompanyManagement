package com.gminds.employee_service.service.utils;

import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
public abstract class AbstractTransactionHelper<E extends Throwable> {

    public <T> T executeInTransaction(Supplier<T> action) throws E {
        try {
            return action.get();
        } catch (Exception e) {
            throw convertException(e);
        }
    }

    protected abstract E convertException(Exception e);
}