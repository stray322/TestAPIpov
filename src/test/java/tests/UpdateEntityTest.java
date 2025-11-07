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
@Feature("Обновление сущностей")
public class UpdateEntityTest extends BaseTest {

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
    @Story("Обновление сущности")
    @Severity(SeverityLevel.NORMAL)
    @Description("Проверка обновления данных сущности")
    public void updateEntityTest() {
        Entity testEntity = createTestEntity();
        Integer entityId = createEntityAndGetId(testEntity);

        Entity updateData = testEntity.toBuilder()
                .title("Обновленный заголовок")
                .verified(false)
                .importantNumbers(List.of(4, 5, 6))
                .build();

        given()
                .spec(requestSpec)
                .pathParam("id", entityId)
                .body(updateData)
                .when()
                .patch(APIEndpoints.UPDATE_ENDPOINT)
                .then()
                .statusCode(204);

        given()
                .spec(requestSpec)
                .pathParam("id", entityId)
                .when()
                .get(APIEndpoints.GET_ENDPOINT)
                .then()
                .statusCode(200)
                .body("title", equalTo("Обновленный заголовок"))
                .body("verified", equalTo(false))
                .body("important_numbers", contains(4, 5, 6));

        given()
                .spec(requestSpec)
                .pathParam("id", entityId)
                .delete(APIEndpoints.DELETE_ENDPOINT)
                .then()
                .statusCode(204);
    }
}
