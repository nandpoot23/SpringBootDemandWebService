package com.example.dm.customer.e2e;

import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import org.junit.Before;
import org.yaml.snakeyaml.Yaml;

/**
 * @author mlahariya
 * @version 1.0, Dec 2016
 */

public abstract class BaseE2EServiceTest {

    protected Map<String, String> testProperties;

    @Before
    public void setUp() throws FileNotFoundException {
        String environmentName = System.getProperty("spring.profiles.active");

        if (environmentName == null || environmentName.isEmpty()) {
            throw new IllegalArgumentException(
                    "Test requires environment parameterization via -Dspring.profiles.active=dm-xxx");
        }

        Yaml yaml = new Yaml();
        FileInputStream fileInputStream = new FileInputStream("src/e2eTest/resources/" + "properties.yml");
        @SuppressWarnings("unchecked")
        Map<String, Map<String, String>> data = (Map<String, Map<String, String>>) yaml.load(fileInputStream);
        if (!data.keySet().contains(environmentName)) {
            throw new IllegalArgumentException("Unable to find specified environment named " + environmentName);
        }
        testProperties = data.get(environmentName);
        testProperties.put("profile", environmentName);

    }

    protected byte[] readFileContent(final String filePath) throws IOException {
        return readAllBytes(get("src/e2eTest/resources/" + filePath));
    }

    protected String getFileName(String apiName) {
        if (testProperties.get("profile").indexOf("prd") > 0)
            return apiName + "-prd.xml";
        else if (testProperties.get("profile").indexOf("qa") > 0)
            return apiName + "-qa.xml";
        else if (testProperties.get("profile").indexOf("int") > 0)
            return apiName + "-int.xml";
        else if (testProperties.get("profile").indexOf("stg") > 0)
            return apiName + "-stg.xml";
        else if (testProperties.get("profile").indexOf("dev") > 0)
            return apiName + "-dev.xml";
        else if (testProperties.get("profile").indexOf("local") > 0)
            return apiName + "-local.xml";
        else
            return apiName + ".xml";
    }

}
