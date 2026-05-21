package csv302;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

public class GradeApiTest {

    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
    }

    @Test
    public void getAllPosts_returnsNonEmptyArray() {
        List<?> posts = given()
                .when()
                .get("/posts")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("$");

        assertFalse(posts.isEmpty(), "Expected /posts to return a non-empty JSON array");
    }

    @Test
    public void getPostById_returnsExpectedEntry() {
        var response = given()
                .when()
                .get("/posts/1")
                .then()
                .statusCode(200)
                .extract()
                .response();

        int id = response.jsonPath().getInt("id");
        String title = response.jsonPath().getString("title");

        assertEquals(id, 1, "Expected /posts/1 to return id=1");
        assertNotNull(title, "Expected title to be present");
        assertFalse(title.trim().isEmpty(), "Expected title to be non-empty");
    }

    @Test
    public void createPost_returnsCreatedResponseWithTitle() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("title", "CSV302 GradeHub API Test");
        payload.put("body", "Created from REST Assured");
        payload.put("userId", 101);

        String responseTitle = given()
                .contentType("application/json")
                .body(payload)
                .when()
                .post("/posts")
                .then()
                .statusCode(201)
                .extract()
                .jsonPath()
                .getString("title");

        assertEquals(responseTitle, payload.get("title").toString(), "Expected response to contain the sent title");
    }

    @Test
    public void getMissingPost_returnsNotFound() {
        given()
                .when()
                .get("/posts/99999")
                .then()
                .statusCode(404);
    }

    @Test
    public void deletePost_returnsOk() {
        given()
                .when()
                .delete("/posts/1")
                .then()
                .statusCode(200);
    }
}

