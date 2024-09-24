package org.gminds.accounting_service.service.util;

import org.gminds.accounting_service.model.PaymentRange;
import org.gminds.accounting_service.model.dtos.PaymentRangeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PaymentRangeMapper {
    PaymentRangeMapper INSTANCE = Mappers.getMapper(PaymentRangeMapper.class);

    PaymentRangeDTO toPaymentRangeDTO(PaymentRange employee);

    PaymentRange toPaymentRange(PaymentRangeDTO employeeDTO);
}