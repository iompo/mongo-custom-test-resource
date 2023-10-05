package org.acme;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusIntegrationTest
@QuarkusTestResource(MongoResource.class)
public class UsersTest {

    @Test
    public void shouldRetrieveUsers() {
        Response response = given().get("/users")
                .thenReturn();

        assertEquals(200, response.getStatusCode());
    }
}
