package tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import utils.ConfigReader;

public class BaseTest {
    protected static final String BASE_URL = ConfigReader.getProperty("base.url");
    protected RequestSpecification requestSpec;

    @BeforeClass
    public void setup() {
        // Настройка ObjectMapper для snake_case
        RestAssured.config = RestAssured.config()
                .objectMapperConfig(new ObjectMapperConfig()
                        .jackson2ObjectMapperFactory((type, s) -> {
                            ObjectMapper mapper = new ObjectMapper();
                            mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
                            return mapper;
                        }));

        requestSpec = new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .setContentType(ContentType.JSON)
                .build();

        Allure.addAttachment("Base URL", "text/uri-list", BASE_URL);
        Allure.addAttachment("Content-Type", ContentType.JSON.toString());
    }
}