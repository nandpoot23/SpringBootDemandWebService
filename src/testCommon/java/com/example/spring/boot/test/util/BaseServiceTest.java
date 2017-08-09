package com.example.spring.boot.test.util;

import static com.github.dreamhead.moco.Moco.file;
import static com.github.dreamhead.moco.Moco.header;
import static com.github.dreamhead.moco.Moco.httpServer;
import static com.github.dreamhead.moco.Moco.log;
import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;

import java.io.IOException;

import org.junit.Before;
import org.junit.BeforeClass;

import com.github.dreamhead.moco.HttpServer;
import com.github.dreamhead.moco.Moco;
import com.github.dreamhead.moco.RequestMatcher;
import com.github.dreamhead.moco.resource.ContentResource;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.specification.RequestSpecification;

/**
 * @author mlahariya
 * @version 1.0, Dec 2016
 */

public class BaseServiceTest {

    protected HttpServer mocoServer;

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 6062;
    }

    @Before
    public void setUp() throws Exception {
        mocoServer = httpServer(12306, log());
    }

    protected static Object[] getRestHeaders() {
        Object[] headers = new String[] { "timestamp", "2013-10-16T13:08:32.831Z", "sourceSystemId", "ss-id",
                "sourceSystemUserId", "ss-user-id", "sourceServerId", "sourceServerId-value", "trackingId",
                "trackingId-value", };
        return headers;

    }

    protected static RequestSpecification givenAuthorizationHeader() {
        return givenAuthorizationHeaderMergedWith();
    }

    protected static RequestSpecification givenAuthorizationHeaderMergedWith(String... headers) {
        return given().headers("Authorization", "Bearer " + TokenHelper.getToken(), (Object[]) headers).when();
    }

    protected RequestMatcher xmlUTF8() {
        return Moco.eq(header("Content-Type"), "application/xml;charset=UTF-8");
    }

    protected static RequestSpecification given() {
        return RestAssured.given().log().ifValidationFails();
    }

    public static ContentResource contentFromFile(String testDir, String fileName) {
        return file("src/" + testDir + "/resources/" + fileName);
    }

    public static byte[] readFileContent(String testDir, final String filePath) throws IOException {
        return readAllBytes(get("src/" + testDir + "/resources/" + filePath));
    }
}
