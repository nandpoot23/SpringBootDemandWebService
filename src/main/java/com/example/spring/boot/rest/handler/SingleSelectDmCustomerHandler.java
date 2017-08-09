package com.example.spring.boot.rest.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.spring.boot.rest.types.EmpConfigIdentifier;
import com.example.spring.boot.rest.types.EmpDetails;

/**
 * @author mlahariya
 * @version 1.0, Dec 2016
 */

@Component
public class SingleSelectDmCustomerHandler {

    @Autowired
    private com.example.spring.boot.rest.dao.DmDbService mathsDbService;

    public com.example.spring.boot.rest.dao.DmDbService getMathsDbService() {
        return mathsDbService;
    }

    public void setMathsDbService(com.example.spring.boot.rest.dao.DmDbService mathsDbService) {
        this.mathsDbService = mathsDbService;
    }

    public EmpDetails queryEmpConfigs(EmpConfigIdentifier id) {

        EmpDetails empDetails = null;

        if (id != null) {
            empDetails = getMathsDbService().queryEmpConfigs(id);
        }

        return empDetails;
    }

}
