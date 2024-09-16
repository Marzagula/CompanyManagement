package org.gminds.accounting_service.repository;

import org.gminds.accounting_service.model.PaymentRange;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRangeRepository extends JpaRepository<PaymentRange, Long> {
}
