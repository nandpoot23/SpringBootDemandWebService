package com.example.dm.customer.contract;

import static com.example.spring.boot.test.util.Utils.CONTRACT_TEST_DIR;
import static com.example.spring.boot.test.util.Utils.DM_SERVICE_SOAP_ENDPOINT_1000;
import static com.example.spring.boot.test.util.Utils.TEST_COMMON_DIR;
import static com.github.dreamhead.moco.Moco.by;
import static com.github.dreamhead.moco.Moco.uri;
import static com.github.dreamhead.moco.Runner.running;
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

public class SelectAllEmpAllDataContractTest extends BaseServiceTest {

    private static final String SELECT_ALL_EMP_ALL_DATA_XML_REQUEST = "SelectAllEmpAllDataContractRequest.xml";

    @Test
    public void testSelectAllEmpAllData() throws Exception {

        mocoServer.request(by(uri("/as/token.oauth2"))).response(contentFromFile(TEST_COMMON_DIR, "token.json"));

        running(mocoServer,
                () -> {
                    ResponseBody<?> response = given().headers("Content-Type", "text/xml")
                            .body(readFileContent(CONTRACT_TEST_DIR, SELECT_ALL_EMP_ALL_DATA_XML_REQUEST))
                            .post(DM_SERVICE_SOAP_ENDPOINT_1000).body();

                    XmlPath xmlPath = new XmlPath(response.asString())
                            .setRoot("Envelope.Body.selectAllEmpAllDataResponse.return[0]");
                    assertNotNull(xmlPath.getString("firstName"));
                    assertEquals("Roo6", xmlPath.getString("firstName"));
                });

    }
}
