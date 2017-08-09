package com.example.spring.boot.test.util;

import static com.github.dreamhead.moco.Moco.file;
import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.github.dreamhead.moco.resource.ContentResource;

/**
 * @author mlahariya
 * @version 1.0, Dec 2016
 */

public class Utils {

    public static final String DM_SERVICE_SOAP_ENDPOINT_1000 = "/DmService/1.0";

    public static final String TEST_COMMON_DIR = "testCommon";
    public static final String FUNCTINAL_TEST_DIR = "functionalTest";
    public static final String CONTRACT_TEST_DIR = "contractTest";

    public static File fromFile(String testDir, String fileName) {
        return new File("src/" + testDir + "/resources/" + fileName);
    }

    public static ContentResource contentFromFile(String testDir, String fileName) {
        return file("src/" + testDir + "/resources/" + fileName);
    }

    public static String returnStringFromFile(String testDir, String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get("src/" + testDir + "/resources/" + fileName)));
    }

    public static byte[] readFileContent(String testDir, final String filePath) throws IOException {
        return readAllBytes(get("src/" + testDir + "/resources/" + filePath));
    }
}
