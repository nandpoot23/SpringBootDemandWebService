package com.example.spring.boot.rest.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import io.swagger.annotations.Contact;
import io.swagger.annotations.ExternalDocs;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring.boot.rest.service.DmServiceInterface;
import com.example.spring.boot.rest.types.EmpAddress;
import com.example.spring.boot.rest.types.EmpConfigIdentifier;
import com.example.spring.boot.rest.types.EmpDetails;

/**
 * @author mlahariya
 * @version 1.0, Dec 2016
 */

@Api(basePath = "/api/dmCustomer/v1", value = "Demand Service", description = "Operations on Demand Service", produces = "application/json", authorizations = { @Authorization(value = "dm_auth", scopes = { @AuthorizationScope(description = "Dm V1 authorization scope", scope = "dm") }) }

)
@SwaggerDefinition(basePath = "", externalDocs = @ExternalDocs(value = "Dig", url = "http://www.google.com"), host = "", info = @Info(title = "Demand Service", version = "1.0", contact = @Contact(name = "Demand Service Support", email = "manul30@yahoo.in", url = "http://www.google.com"), description = "Operations on Demand Service"))
@RestController
@RequestMapping(value = "/api/dmCustomer/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class SpringBootDemandRestServiceController {

    @Autowired
    @Qualifier("DmServiceImpl")
    private DmServiceInterface dmService;

    private static final Logger LOG = LoggerFactory.getLogger(SpringBootDemandRestServiceController.class);

    @Lazy(false)
    @ApiOperation(nickname = "getEmpConfiguration", value = "Get Emp Configuration", notes = "This Api fetches the employee service and its related personal details like address or city. "
            + "It also provides the corresponding email if any. "
            + "Simple Data Management is implemented for this api to enable tracking of user audits.", response = EmpDetails.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Invalid request", response = com.example.spring.boot.rest.exception.ErrorResponse.class),
            @ApiResponse(code = 500, message = "Error processing request", response = com.example.spring.boot.rest.exception.ErrorResponse.class), })
    @RequestMapping(method = RequestMethod.POST, value = "/empConfig", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<EmpDetails> getEmpConfiguration(
            @ApiParam(name = "EmpConfigIdentifier", value = "EmpConfigIdentifier id service request ", required = true) @RequestBody EmpConfigIdentifier id) {

        LOG.info("Received Request for getEmpConfiguration {} ", id);

        EmpDetails response = dmService.queryEmpConfigs(id);

        LOG.info("Returning Response for getEmpConfiguration");

        return new ResponseEntity<EmpDetails>(response, HttpStatus.OK);
    }

    @Lazy(false)
    @ApiOperation(nickname = "getAllEmpByAddress", value = "Get All Emp By Address", notes = "This Api fetches the all employee personal details based on emp address. ", response = EmpDetails.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Invalid request", response = com.example.spring.boot.rest.exception.ErrorResponse.class),
            @ApiResponse(code = 500, message = "Error processing request", response = com.example.spring.boot.rest.exception.ErrorResponse.class), })
    @RequestMapping(method = RequestMethod.POST, value = "/empConfigAddress", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<List<EmpDetails>> getAllEmpByAddress(
            @ApiParam(name = "EmpAddress", value = "EmpAddress addr service request ", required = true) @RequestBody EmpAddress empAddr) {

        LOG.info("Received Request for getAllEmpByAddress {} ", empAddr);

        List<EmpDetails> response = dmService.selectAllEmpAllData(empAddr);

        LOG.info("Returning Response for getAllEmpByAddress");

        return new ResponseEntity<List<EmpDetails>>(response, HttpStatus.OK);
    }

}
