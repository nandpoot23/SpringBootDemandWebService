package com.example.dm.customer.functional;

import static com.example.spring.boot.test.util.Utils.DM_SERVICE_SOAP_ENDPOINT_1000;
import static com.example.spring.boot.test.util.Utils.FUNCTINAL_TEST_DIR;
import static com.example.spring.boot.test.util.Utils.TEST_COMMON_DIR;
import static com.github.dreamhead.moco.Moco.by;
import static com.github.dreamhead.moco.Moco.uri;
import static com.github.dreamhead.moco.Runner.running;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.xml.HasXPath.hasXPath;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.example.spring.boot.test.util.BaseServiceTest;
import com.jayway.restassured.path.xml.XmlPath;
import com.jayway.restassured.response.ResponseBody;

/**
 * @author mlahariya
 * @version 1.0, Dec 2016
 */

public class QueryEmpConfigsFunctionalTest extends BaseServiceTest {

    private static final String QUERY_EMP_CONFIGS_ID_ONE_XML_REQUEST = "QueryEmpConfigsRequestIdOne.xml";
    private static final String QUERY_EMP_CONFIGS_ID_TWO_XML_REQUEST = "QueryEmpConfigsRequestIdTwo.xml";
    private static final String QUERY_EMP_CONFIGS_ID_THREE_XML_REQUEST = "QueryEmpConfigsRequestIdThree.xml";
    private static final String QUERY_EMP_CONFIGS_XML_REQUEST = "QueryEmpConfigFunctionalRequest.xml";
    private static final String QUERY_EMP_CONFIGS_XML_REQUEST_EXCEPTION = "QueryEmpConfigsException.xml";

    @Test
    public void testQueryEmpConfigsIdOne() throws Exception {

        mocoServer.request(by(uri("/as/token.oauth2"))).response(contentFromFile(TEST_COMMON_DIR, "token.json"));

        // eligible when we have any external call.

        // mocoServer.request(and(contain(text("RetrieveAccount")))).response(with(contentFromFile(FUNCTINAL_TEST_DIR,
        // RETRIEVE_ACCOUNT_RESPONSE_XML)));

        running(mocoServer,
                () -> {
                    ResponseBody<?> response = given().headers("Content-Type", "text/xml")
                            .body(readFileContent(FUNCTINAL_TEST_DIR, QUERY_EMP_CONFIGS_ID_ONE_XML_REQUEST))
                            .post(DM_SERVICE_SOAP_ENDPOINT_1000).body();

                    XmlPath xmlPath = new XmlPath(response.asString())
                            .setRoot("Envelope.Body.queryEmpConfigsResponse.return");

                    assertNotNull(xmlPath.getString("firstName"));
                    assertEquals("Roo6", xmlPath.getString("firstName"));
                });

    }

    @Test
    public void testQueryEmpConfigsIdTwo() throws Exception {

        mocoServer.request(by(uri("/as/token.oauth2"))).response(contentFromFile(TEST_COMMON_DIR, "token.json"));

        running(mocoServer,
                () -> given().headers("Content-Type", "text/xml")
                        .body(readFileContent(FUNCTINAL_TEST_DIR, QUERY_EMP_CONFIGS_ID_TWO_XML_REQUEST)).when()
                        .post(DM_SERVICE_SOAP_ENDPOINT_1000).then().assertThat()
                        .body(hasXPath("/Envelope/Body/queryEmpConfigsResponse/return/firstName/text()"))
                        .statusCode(200));

    }

    @Test
    public void testQueryEmpConfigsIdThree() throws Exception {

        mocoServer.request(by(uri("/as/token.oauth2"))).response(contentFromFile(TEST_COMMON_DIR, "token.json"));

        running(mocoServer,
                () -> given()
                        .headers("Content-Type", "text/xml")
                        .body(readFileContent(FUNCTINAL_TEST_DIR, QUERY_EMP_CONFIGS_ID_THREE_XML_REQUEST))
                        .when()
                        .post(DM_SERVICE_SOAP_ENDPOINT_1000)
                        .then()
                        .assertThat()
                        .body(hasXPath("/Envelope/Body/queryEmpConfigsResponse/return/firstName/text()",
                                equalTo("Ruchi"))).statusCode(200));
    }

    @Test
    public void testQueryEmpConfigsId() throws Exception {

        mocoServer.request(by(uri("/as/token.oauth2"))).response(contentFromFile(TEST_COMMON_DIR, "token.json"));

        running(mocoServer,
                () -> given().headers("Content-Type", "text/xml")
                        .body(readFileContent(FUNCTINAL_TEST_DIR, QUERY_EMP_CONFIGS_XML_REQUEST)).when()
                        .post(DM_SERVICE_SOAP_ENDPOINT_1000).then().assertThat().statusCode(200));

    }

    @Test
    public void testQueryEmpConfigsMultiValidation() throws Exception {

        mocoServer.request(by(uri("/as/token.oauth2"))).response(contentFromFile(TEST_COMMON_DIR, "token.json"));

        running(mocoServer,
                () -> given()
                        .headers("Content-Type", "text/xml")
                        .body(readFileContent(FUNCTINAL_TEST_DIR, QUERY_EMP_CONFIGS_XML_REQUEST))
                        .when()
                        .post(DM_SERVICE_SOAP_ENDPOINT_1000)
                        .then()
                        .assertThat()
                        .body(hasXPath("/Envelope/Body/queryEmpConfigsResponse/return/firstName/text()",
                                equalTo("Roo6")),

                                hasXPath("/Envelope/Body/queryEmpConfigsResponse/return/lastName/text()", equalTo("L")),

                                hasXPath("/Envelope/Body/queryEmpConfigsResponse/return/address/text()",
                                        equalTo("Sec49")),

                                hasXPath("/Envelope/Body/queryEmpConfigsResponse/return/city/text()", equalTo("BNDA")),

                                hasXPath("/Envelope/Body/queryEmpConfigsResponse/return/email/text()",
                                        equalTo("r1@gmail.com")),

                                hasXPath("/Envelope/Body/queryEmpConfigsResponse/return/mobile/text()",
                                        equalTo("644681423")),

                                hasXPath("/Envelope/Body/queryEmpConfigsResponse/return/id/text()", equalTo("1")))

                        .statusCode(200));
    }

    @Test
    public void testQueryEmpConfigsException() throws Exception {

        mocoServer.request(by(uri("/as/token.oauth2"))).response(contentFromFile(TEST_COMMON_DIR, "token.json"));

        running(mocoServer,
                () -> given().headers("Content-Type", "text/xml")
                        .body(readFileContent(FUNCTINAL_TEST_DIR, QUERY_EMP_CONFIGS_XML_REQUEST_EXCEPTION)).when()
                        .post(DM_SERVICE_SOAP_ENDPOINT_1000).then().assertThat()
                        // below is the standard way for exception
                        // .body(hasXPath("/Envelope/Body/Fault/detail/serviceFault/messages/message/code/text()",
                        // equalTo("CUSTOMER-1001")))
                        .statusCode(500));

    }

}