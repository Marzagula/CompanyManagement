package com.gminds.employee_service.service.utils;

import jakarta.transaction.Transactional;

import java.util.function.Supplier;

public abstract class TransactionHelper<T, E extends RuntimeException> {

    @Transactional
    public <T> T executeInTransaction(Supplier<T> action) throws E {
        try {
            return action.get();
        } catch (RuntimeException e) {
            throw prepareException(e);
        }
    }

    protected abstract E prepareException(RuntimeException e);

}