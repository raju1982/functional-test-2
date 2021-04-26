
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeSuite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TestBase {
    protected static String environment;
    protected static Properties ucpProperties;
    protected static RequestSpecification videoDBRequestSpec;
    protected static RequestSpecification footballRequestSpec;
    protected static ResponseSpecification footballResponseSpecification;

    //constants
    public static final String path = "com.test.path";

    private static Logger logger = LoggerFactory.getLogger(TestBase.class);

    @BeforeSuite(alwaysRun = true)
    public void setup() throws IOException {
        environment = System.getProperty("TEST_ENV");

        if (environment == null) {
            environment = Constants.DEFAULT_TEST_ENV;
        }
        logger.info("env = " + environment);
        //load properties from corresponding environment
        ucpProperties = new Properties();
        InputStream resourceStream = TestBase.class.getResourceAsStream("ui-" + environment + ".properties");
        ucpProperties.load(resourceStream);
        createRequestSpecification();
    }

    private void createRequestSpecification() {
        videoDBRequestSpec = new RequestSpecBuilder()
                .setBaseUri("http://localhost")
                .setBasePath("/app/")
                .setPort(8080)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();

        footballRequestSpec = new RequestSpecBuilder()
                .setBaseUri("http://api.football-data.org")
                .setBasePath("/v2/")
                .addHeader("X-Auth-Token", "849b7975c9054d1484f1ce30f30b2589")
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();

        footballResponseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .build();
    }

    protected synchronized String readPayloadFromFile(String fileName, String id) throws IOException {
        String payload = readPayloadFromFile(fileName);
        if(id != null && id.length() != 0){
            payload = String.format(payload, id);
        }
        return payload;
    }

    protected synchronized String readPayloadFromFile(String fileName) throws IOException {
        String payload;
        InputStream inputStream = ClassLoader.getSystemResourceAsStream(fileName);
        try{
            payload = IOUtils.toString(inputStream);
        }catch (IOException exp){
            throw exp;
        }
        return payload;
    }

    protected String randomID(){
        long min = 100;
        long max = 1000;
        return randomID(min, max);
    }

    protected String randomID(long min, long max){
        Long id = (long) (Math.random() * ((max-min) + 1)) + min;
        return id.toString();
    }

}
