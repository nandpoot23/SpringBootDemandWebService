package com.example.dm.customer.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.ws.context.MessageContext;

import com.example.spring.boot.rest.app.SpringBootDemandWebServiceApplication;
import com.example.spring.boot.soap.controller.SpringBootDemandSoapServiceController;
import com.sample.soap.xml.dm.EmpAddress;
import com.sample.soap.xml.dm.EmpConfigIdentifier;
import com.sample.soap.xml.dm.QueryEmpConfigs;
import com.sample.soap.xml.dm.QueryEmpConfigsResponse;
import com.sample.soap.xml.dm.SelectAllEmpAllData;
import com.sample.soap.xml.dm.SelectAllEmpAllDataResponse;

/**
 * @author mlahariya
 * @version 1.0, Dec 2016
 */

@SuppressWarnings("deprecation")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringBootDemandWebServiceApplication.class)
@WebAppConfiguration
@IntegrationTest
public class SpringBootDemandSoapIntegrationTest {

    @Autowired
    private SpringBootDemandSoapServiceController dmSoapServiceController;

    @Test
    public void testQueryEmpConfigs() throws Exception {

        MessageContext messageContext = null;
        QueryEmpConfigs queryCustomer = new QueryEmpConfigs();
        EmpConfigIdentifier empConfigIdentifier = new EmpConfigIdentifier();
        empConfigIdentifier.setId(1);
        queryCustomer.setArg0(empConfigIdentifier);

        QueryEmpConfigsResponse response = dmSoapServiceController.queryEmpConfigs(queryCustomer, messageContext);

        System.out.println("*************" + response.getReturn().getId());
        System.out.println("*************" + response.getReturn().getFirstName());
        System.out.println("*************" + response.getReturn().getLastName());
        System.out.println("*************" + response.getReturn().getAddress());
        System.out.println("*************" + response.getReturn().getCity());
        System.out.println("*************" + response.getReturn().getEmail());
        System.out.println("*************" + response.getReturn().getMobile());
        assertNotNull(response);
        assertNotNull(response.getReturn());
        assertEquals(1, response.getReturn().getId());
        assertEquals("Roo6", response.getReturn().getFirstName());
        assertEquals("L", response.getReturn().getLastName());
        assertEquals("Sec49", response.getReturn().getAddress());
        assertEquals("BNDA", response.getReturn().getCity());
        assertEquals("r1@gmail.com", response.getReturn().getEmail());
        assertEquals("644681423", response.getReturn().getMobile());
    }

    @Test
    public void testSelectAllEmpAllData() throws Exception {

        SelectAllEmpAllData selectAllEmpAllData = new SelectAllEmpAllData();
        EmpAddress empAddress = new EmpAddress();
        empAddress.setAddress("Sec49");
        selectAllEmpAllData.setArg0(empAddress);

        SelectAllEmpAllDataResponse response = dmSoapServiceController.selectAllEmpAllData(selectAllEmpAllData, null);
        assertNotNull(response);
        assertNotNull(response.getReturn());
        assertNotNull(response.getReturn().size());
        assertTrue(response.getReturn().size() > 0);
        assertFalse(response.getReturn().size() <= 0);
        assertEquals("Roo6", response.getReturn().get(0).getFirstName());
        assertEquals("Manul", response.getReturn().get(1).getFirstName());
        assertEquals("Ruchi", response.getReturn().get(2).getFirstName());
        assertEquals("Shraddha", response.getReturn().get(3).getFirstName());

    }

}
