package com.example.dm.customer.integration;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import com.example.spring.boot.rest.app.SpringBootDemandWebServiceApplication;
import com.example.spring.boot.rest.types.EmpAddress;
import com.example.spring.boot.rest.types.EmpConfigIdentifier;
import com.example.spring.boot.rest.types.EmpDetails;

/**
 * @author mlahariya
 * @version 1.0, Dec 2016
 */

@SuppressWarnings("deprecation")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringBootDemandWebServiceApplication.class)
@WebAppConfiguration
@IntegrationTest
public class SpringBootDemandRestIntegrationTest {

    private String URL = "http://localhost:6062/api/dmCustomer/v1";

    @Test
    public void testGetEmpConfiguration() {

        EmpConfigIdentifier request = new EmpConfigIdentifier();
        request.setId(1);

        HttpEntity<EmpConfigIdentifier> restRequest = new HttpEntity<EmpConfigIdentifier>(request, getHeaders());

        RestTemplate restTemplate = new RestTemplate();

        EmpDetails response = restTemplate.postForObject(URL + "/empConfig", restRequest, EmpDetails.class);

        assertNotNull(response);

    }

    @Test
    public void testGetAllEmpByAddress() {

        EmpAddress request = new EmpAddress();
        request.setAddress("Sec49");

        HttpEntity<EmpAddress> restRequest = new HttpEntity<EmpAddress>(request, getHeaders());

        RestTemplate restTemplate = new RestTemplate();

        ParameterizedTypeReference<List<com.example.spring.boot.rest.types.EmpDetails>> responseType = new ParameterizedTypeReference<List<com.example.spring.boot.rest.types.EmpDetails>>() {
        };
        ResponseEntity<List<com.example.spring.boot.rest.types.EmpDetails>> response = restTemplate.exchange(URL
                + "/empConfigAddress", HttpMethod.POST, restRequest, responseType);

        assertNotNull(response);

    }

    //if we have array type in return then use below convention
     //ServiceableLocation[] response = restTemplate.postForObject(URL + "modifyServiceableLocation", restRequest,
               // ServiceableLocation[].class);
     //Term[] response = restTemplate.postForObject(URL + "getTermGroup", restRequest, Term[].class);                
     
    
    //if we have boolean, string type in return then use below convention
     //Boolean response = restTemplate.postForObject(URL + "setPortNumber", restRequest, Boolean.class);
     //String response = restTemplate.postForObject(URL + "recordContract",
                //restRequest, String.class);
     
    private HttpHeaders getHeaders() {

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.set("Authorization", "Bearer");
        requestHeaders.set("sourceSystemId", "dmapp");
        requestHeaders.set("sourceSystemUserId", "1");
        requestHeaders.set("sourceServerId", "1");
        requestHeaders.set("trackingId", "ID");
        requestHeaders.set("timestamp", "2016-12-22T13:20:00-05:00");
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        return requestHeaders;
    }

}
