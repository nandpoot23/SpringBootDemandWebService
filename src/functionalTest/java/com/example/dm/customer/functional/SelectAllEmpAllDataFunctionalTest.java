package com.example.dm.customer.functional;

import static com.example.spring.boot.test.util.Utils.DM_SERVICE_SOAP_ENDPOINT_1000;
import static com.example.spring.boot.test.util.Utils.FUNCTINAL_TEST_DIR;
import static com.example.spring.boot.test.util.Utils.TEST_COMMON_DIR;
import static com.github.dreamhead.moco.Moco.by;
import static com.github.dreamhead.moco.Moco.uri;
import static com.github.dreamhead.moco.Runner.running;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
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

public class SelectAllEmpAllDataFunctionalTest extends BaseServiceTest {

    private static final String SELECT_ALL_EMP_ALL_DATA_XML_REQUEST = "SelectAllEmpAllDataFunctionalRequest.xml";

    @Test
    public void testSelectAllEmpAllData() throws Exception {

        mocoServer.request(by(uri("/as/token.oauth2"))).response(contentFromFile(TEST_COMMON_DIR, "token.json"));

        running(mocoServer,
                () -> {
                    ResponseBody<?> response = given().headers("Content-Type", "text/xml")
                            .body(readFileContent(FUNCTINAL_TEST_DIR, SELECT_ALL_EMP_ALL_DATA_XML_REQUEST))
                            .post(DM_SERVICE_SOAP_ENDPOINT_1000).body();

                    XmlPath xmlPath = new XmlPath(response.asString())
                            .setRoot("Envelope.Body.selectAllEmpAllDataResponse");
                    assertNotNull(xmlPath.getString("return[0].firstName"));
                    assertEquals("Roo6", xmlPath.getString("return[0].firstName"));
                });

    }

    @Test
    public void testSelectAllEmpAllDataMultiValidation() throws Exception {

        mocoServer.request(by(uri("/as/token.oauth2"))).response(contentFromFile(TEST_COMMON_DIR, "token.json"));

        running(mocoServer,
                () -> {
                    ResponseBody<?> response = given().headers("Content-Type", "text/xml")
                            .body(readFileContent(FUNCTINAL_TEST_DIR, SELECT_ALL_EMP_ALL_DATA_XML_REQUEST))
                            .post(DM_SERVICE_SOAP_ENDPOINT_1000).body();

                    XmlPath xmlPath = new XmlPath(response.asString())
                            .setRoot("Envelope.Body.selectAllEmpAllDataResponse");

                    assertNotNull(xmlPath.getString("return[0].firstName"));
                    assertEquals("Roo6", xmlPath.getString("return[0].firstName"));

                    assertNotNull(xmlPath.getString("return[1].firstName"));
                    assertEquals("Manul", xmlPath.getString("return[1].firstName"));

                    assertNotNull(xmlPath.getString("return[2].firstName"));
                    assertEquals("Ruchi", xmlPath.getString("return[2].firstName"));

                    assertNotNull(xmlPath.getString("return[3].firstName"));
                    assertEquals("Shraddha", xmlPath.getString("return[3].firstName"));

                });

    }

    @Test
    public void testSelectAllEmpAllDataSingleValidation() throws Exception {

        mocoServer.request(by(uri("/as/token.oauth2"))).response(contentFromFile(TEST_COMMON_DIR, "token.json"));

        running(mocoServer,
                () -> given()
                        .headers("Content-Type", "text/xml")
                        .body(readFileContent(FUNCTINAL_TEST_DIR, SELECT_ALL_EMP_ALL_DATA_XML_REQUEST))
                        .when()
                        .post(DM_SERVICE_SOAP_ENDPOINT_1000)
                        .then()
                        .assertThat()
                        .body(hasXPath("/Envelope/Body/selectAllEmpAllDataResponse/return[0]/firstName/text()",
                                not(equalTo("Roo6asefw")))).statusCode(200));

    }

}
