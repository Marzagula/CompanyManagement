package com.gminds.employee_service.service.agreement.validator;

import com.gminds.employee_service.exceptions.EmployeeAgreementException;
import com.gminds.employee_service.model.EmployeeAgreement;
import com.gminds.employee_service.model.PaymentRange;
import com.gminds.employee_service.model.enums.AgreementStatus;
import com.gminds.employee_service.model.enums.EmplAgreementType;
import com.gminds.employee_service.service.agreement.CachedPaymentRangeService;
import com.gminds.employee_service.service.utils.validator.DateValidator;
import com.gminds.employee_service.service.utils.validator.ListValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgreementValidatorService implements AgreementValidator, SalaryValidator, ListValidator<EmployeeAgreement> {

    private static final Logger logger = LoggerFactory.getLogger(AgreementValidatorService.class);
    private final CachedPaymentRangeService cachedPaymentRangeService;

    public AgreementValidatorService(CachedPaymentRangeService cachedPaymentRangeService) {
        this.cachedPaymentRangeService = cachedPaymentRangeService;
    }

    @Override
    public void validateAgreement(EmployeeAgreement employeeAgreement) throws EmployeeAgreementException {
        if (employeeAgreement.getStatus() != AgreementStatus.FINISHED) {
            validateSalary(getPaymentRange(employeeAgreement), employeeAgreement.getSalary());
        }
        logger.info("Employee Agreement with id: {}, and salary: {}", employeeAgreement.getId(), employeeAgreement.getSalary());
        DateValidator.validateIfEarlierIsBeforeLater(employeeAgreement.getFromDate(), employeeAgreement.getToDate());
    }

    @Override
    public void validateSalary(PaymentRange paymentRange, Double salary) throws EmployeeAgreementException {
        if (paymentRange.getMinSalary() > salary || paymentRange.getMaxSalary() < salary) {
            logger.error("Salary ({}) must be in range of job payment range ({} - {}).",
                    salary,
                    paymentRange.getMinSalary(),
                    paymentRange.getMaxSalary()
            );
            throw new EmployeeAgreementException("Salary (" + salary + ") must be in range of job payment range ("
                    + paymentRange.getMinSalary() + " - " + paymentRange.getMaxSalary() + ").");
        }

    }

    private PaymentRange getPaymentRange(EmployeeAgreement employeeAgreement) throws EmployeeAgreementException {
        return cachedPaymentRangeService.getCachedPaymentRanges().stream()
                .filter(pR -> pR.getJob().getId() == employeeAgreement.getEmployee().getJob().getId()
                        && pR.getEmplAgreementType() == EmplAgreementType.B2B)
                .findFirst().orElseThrow(() -> {
                    logger.error("Can't find " + employeeAgreement.getAgreementType() + " payment range for agreement with id: {}\n job id:{}\n agreementType:{}"
                            , employeeAgreement.getId()
                            , employeeAgreement.getEmployee().getJob().getId()
                            , employeeAgreement.getAgreementType()
                    );
                    return new EmployeeAgreementException("Can't find " + employeeAgreement.getAgreementType() + " payment range for agreement with id: "
                            + employeeAgreement.getId()
                            + "\n job id: " + employeeAgreement.getEmployee().getJob().getId()
                            + "\n agreementType: " + employeeAgreement.getAgreementType()
                    );
                });
    }

    @Override
    /**
     * Validates all agreements.
     *
     * @param agreements all employee agreements
     * */
    public void validateList(List<EmployeeAgreement> agreements) throws EmployeeAgreementException {
        long activeAgreementsCount = agreements.stream()
                .filter(agreement -> agreement.getStatus().equals(AgreementStatus.ACTIVE))
                .count();

        if (activeAgreementsCount > 1) {
            throw new EmployeeAgreementException("Employee can't have more than 1 active agreement.");
        }
    }
}
