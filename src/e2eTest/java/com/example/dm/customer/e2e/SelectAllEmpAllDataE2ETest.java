package com.example.dm.customer.e2e;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.path.xml.XmlPath;
import com.jayway.restassured.response.ResponseBody;

/**
 * @author mlahariya
 * @version 1.0, Dec 2016
 */

public class SelectAllEmpAllDataE2ETest extends BaseE2EServiceTest {

    public static final String DM_SERVICE_SOAP_ENDPOINT_1000 = "/DmService/1.0";

    @Test
    public void testSelectAllEmpAllData() throws Exception {

        final String endPoint = testProperties.get("dmServiceEndpoint");
        RestAssured.baseURI = endPoint;

        ResponseBody<?> response = null;

        response = given().headers("Content-Type", "text/xml").headers("Content-Type", "text/xml")
                .body(readFileContent(getFileName("SelectAllEmpAllData"))).post(DM_SERVICE_SOAP_ENDPOINT_1000).body();

        String responseString = response.asString();
        int start = responseString.indexOf("<SOAP-ENV:Envelope");
        int finish = responseString.indexOf("</SOAP-ENV:Envelope>") + "</SOAP-ENV:Envelope>".length();
        responseString = responseString.substring(start, finish);

        XmlPath xmlPath = new XmlPath(response.asString())
                .setRoot("Envelope.Body.selectAllEmpAllDataResponse.return[0]");
        assertNotNull(xmlPath.getString("firstName"));
        assertEquals("Roo6", xmlPath.getString("firstName"));
    }
}
