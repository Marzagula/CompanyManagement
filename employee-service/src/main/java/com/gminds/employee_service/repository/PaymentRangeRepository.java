package com.gminds.employee_service.repository;

import com.gminds.employee_service.model.PaymentRange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRangeRepository extends JpaRepository<PaymentRange, Long> {

}
