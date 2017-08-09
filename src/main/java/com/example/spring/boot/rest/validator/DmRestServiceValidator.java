package com.example.spring.boot.rest.validator;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.example.spring.boot.rest.exception.ErrorCode;
import com.example.spring.boot.rest.service.DmServiceInterface;
import com.example.spring.boot.rest.types.EmpAddress;
import com.example.spring.boot.rest.types.EmpConfigIdentifier;

/**
 * @author mlahariya
 * @version 1.0, Dec 2016
 */

@Component
public class DmRestServiceValidator extends ServiceValidator<DmServiceInterface> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DmRestServiceValidator.class);

    /**
     * Validates the {@link DmService#queryEmpConfigs(EmpConfigIdentifier id)}
     * operation.
     * 
     * @param errors
     *            A {@link Errors} object containing the errors to return to the
     *            client.
     * @param id
     *            The <code>empConfigId</code> parameter passed in by the
     *            client.
     */

    @ValidatesMethod("queryEmpConfigs")
    public void validateQueryEmpConfigs(Errors errors, EmpConfigIdentifier empConfigId) {

        if (empConfigId.getId() == 0) {
            LOGGER.debug("please provide the valid id");
            errors.reject(ErrorCode.CS_1005.getValue());
            return;
        }

        else {
            if (empConfigId.getId() < 0) {
                errors.reject(ErrorCode.CS_1006.getValue());
                return;

            }
        }
    }

    /**
     * Validates the {@link DmService#selectAllEmpAllData(EmpAddress empAddr)}
     * operation.
     * 
     * @param errors
     *            A {@link Errors} object containing the errors to return to the
     *            client.
     * @param id
     *            The <code>empAddr</code> parameter passed in by the client.
     */

    @ValidatesMethod("selectAllEmpAllData")
    public void validateSelectAllEmpAllData(Errors errors, EmpAddress empAddr) {

        if (StringUtils.isBlank(empAddr.getAddress())) {
            errors.reject(ErrorCode.CS_1007.getValue());
            return;

        }

        else {
            if (empAddr.getAddress() == null) {
                errors.reject(ErrorCode.CS_1008.getValue());
                return;

            }

        }

    }

}
