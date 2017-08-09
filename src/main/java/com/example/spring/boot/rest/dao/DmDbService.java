package com.example.spring.boot.rest.dao;

import java.util.List;

import com.example.spring.boot.rest.types.EmpAddress;
import com.example.spring.boot.rest.types.EmpConfigIdentifier;
import com.example.spring.boot.rest.types.EmpDetails;

/**
 * @author mlahariya
 * @version 1.0, Dec 2016
 */

public interface DmDbService {

    public EmpDetails queryEmpConfigs(EmpConfigIdentifier id);

    public List<EmpDetails> selectAllEmpAllData(EmpAddress empAddr);

}
