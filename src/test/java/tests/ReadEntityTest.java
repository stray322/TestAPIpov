package tests;

import io.qameta.allure.*;
import models.Addition;
import models.Entity;
import org.testng.annotations.Test;
import utils.APIEndpoints;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("API Тесты для работы с сущностями")
@Feature("Чтение сущностей")
public class ReadEntityTest extends BaseTest {

    private Entity createTestEntity() {
        return Entity.builder()
                .title("Test Entity " + System.currentTimeMillis())
                .verified(true)
                .importantNumbers(List.of(1, 2, 3))
                .addition(Addition.builder()
                        .additionalInfo("Тестовые данные")
                        .additionalNumber(42)
                        .build())
                .build();
    }

    private Integer createEntityAndGetId(Entity entity) {
        String response = given()
                .spec(requestSpec)
                .body(entity)
                .when()
                .post(APIEndpoints.CREATE_ENDPOINT)
                .then()
                .statusCode(200)
                .body(notNullValue())
                .extract()
                .body()
                .asString();

        return Integer.parseInt(response);
    }

    @Test
    @Story("Чтение сущности")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверка получения данных сущности")
    public void getEntityTest() {
        Entity testEntity = createTestEntity();
        Integer entityId = createEntityAndGetId(testEntity);

        given()
                .spec(requestSpec)
                .pathParam("id", entityId)
                .when()
                .get(APIEndpoints.GET_ENDPOINT)
                .then()
                .statusCode(200)
                .body("title", equalTo(testEntity.getTitle()))
                .body("verified", equalTo(testEntity.getVerified()))
                .body("important_numbers", not(empty()))
                .body("addition.additional_info", equalTo(testEntity.getAddition().getAdditionalInfo()));

        given()
                .spec(requestSpec)
                .pathParam("id", entityId)
                .delete(APIEndpoints.DELETE_ENDPOINT)
                .then()
                .statusCode(204);
    }

    @Test
    @Story("Список сущностей")
    @Severity(SeverityLevel.MINOR)
    @Description("Проверка получения списка сущностей")
    public void getAllEntitiesTest() {
        given()
                .spec(requestSpec)
                .queryParam("page", 1)
                .queryParam("perPage", 10)
                .when()
                .get(APIEndpoints.GET_ALL_ENDPOINT)
                .then()
                .statusCode(200)
                .body("", is(notNullValue()))
                .body("size()", greaterThanOrEqualTo(0));
    }
}
