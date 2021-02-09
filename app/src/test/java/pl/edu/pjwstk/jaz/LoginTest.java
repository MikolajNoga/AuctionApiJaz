package pl.edu.pjwstk.jaz;


import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import pl.edu.pjwstk.jaz.config.IntegrationTest;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@IntegrationTest
public class LoginTest {

    @BeforeClass
    public static void adminRegistration() throws JSONException {
        JSONObject requestParams = new JSONObject();
        requestParams.put("username", "admin");
        requestParams.put("password", "admin");
        requestParams.put("firstname", "admin");
        requestParams.put("lastname", "admin");
        given()
                .contentType("application/json")
                .body(requestParams.toString())
                .when()
                .post("/api/register")
                .thenReturn();
    }

   @BeforeClass
    public static void userRegistration() throws JSONException {
       JSONObject requestParams = new JSONObject();
       requestParams.put("username", "user");
       requestParams.put("password", "user");
       requestParams.put("firstname", "user");
       requestParams.put("lastname", "user");
       given()
               .contentType("application/json")
               .body(requestParams.toString())
               .when()
               .post("/api/register")
               .thenReturn();
    }

    @Test
    public void testRegistrationShouldReturn200AndGetRoleAsUser() throws JSONException {
        JSONObject requestParams = new JSONObject();
        requestParams.put("username", "test");
        requestParams.put("password", "test");
        requestParams.put("firstname", "test");
        requestParams.put("lastname", "test");
        given()
                .contentType("application/json")
                .body(requestParams.toString())
                .when()
                .post("/api/register")
                .then()
                .statusCode(200);
    }

    @Test
    public void wrongLoginParamsShouldReturnStatusCode401() throws JSONException {
        JSONObject requestParams = new JSONObject();
        requestParams.put("username", "afefwv");
        requestParams.put("password", "vaddbqbqb");
        given()
                .contentType("application/json")
                .body(requestParams.toString())
                .when()
                .post("/api/login")
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    public void registrationShouldReturn400WhenParamIsNotGiven() throws JSONException {
        JSONObject requestParams = new JSONObject();
        requestParams.put("username", "user");
        requestParams.put("password", "");
        requestParams.put("firstname", "userFirstname");
        requestParams.put("lastname", "userLastname");
        given()
                .contentType("application/json")
                .body(requestParams.toString())
                .when()
                .post("/api/register")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void shouldRespondToReadinessRequestWithoutLogin(){
        given()
                .get("api/auth0/is-ready")
                .then()
                .statusCode(200)
                .body(equalTo("ready"));
    }

    @Test
    public void shouldReturn401ToExampleOnAuth0(){
        given()
                .get("api/auth0/example")
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    public void shouldGetStatusCode200AsAdmin() throws JSONException {
        JSONObject requestParams = new JSONObject();
        requestParams.put("username", "admin");
        requestParams.put("password", "admin");
        var response = given()
                .contentType("application/json")
                .body(requestParams.toString())
                .when()
                .post("/api/login")
                .thenReturn();
        given()
                .cookies(response.getCookies())
                .get("/api/forAdmin")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(equalTo("forAdmin"));
    }

    @Test
    public void shouldGetStatusCode403AsUser() throws JSONException {
        JSONObject requestParams = new JSONObject();
        requestParams.put("username", "user");
        requestParams.put("password", "user");
        var response = given()
                .contentType("application/json")
                .body(requestParams.toString())
                .when()
                .post("/api/login")
                .thenReturn();
        given()
                .cookies(response.getCookies())
                .get("/api/forAdmin")
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    public void shouldGetStatusCode200AsUserAndAverage() throws JSONException {
        JSONObject requestParams = new JSONObject();
        requestParams.put("username", "user");
        requestParams.put("password", "user");
        var response = given()
                .contentType("application/json")
                .body(requestParams.toString())
                .when()
                .post("/api/login")
                .thenReturn();
        given()
                .cookies(response.getCookies())
                .param("numbers","1,2,3,4")
                .when()
                .get("/api/average")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(equalTo("Average equals: 2.5"));
    }

    @Test
    public void shouldGetStatusCode200AsAdminAndAverage() throws JSONException {
        JSONObject requestParams = new JSONObject();
        requestParams.put("username", "admin");
        requestParams.put("password", "admin");
        var response = given()
                .contentType("application/json")
                .body(requestParams.toString())
                .when()
                .post("/api/login")
                .thenReturn();
        given()
                .cookies(response.getCookies())
                .param("numbers","1,2,3,4")
                .when()
                .get("/api/average")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(equalTo("Average equals: 2.5"));
    }

    @Test
    public void shouldGetStatusCode403WithoutLogin(){
        given()
                .param("numbers","1,2,3,4")
                .when()
                .get("/api/average")
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    public void shouldGetStatusCode422WhenRegisterButUserAlreadyExist() throws JSONException {
        JSONObject requestParams = new JSONObject();
        requestParams.put("username", "user");
        requestParams.put("password", "user");
        requestParams.put("firstname", "user");
        requestParams.put("lastname", "user");
        given()
                .contentType("application/json")
                .body(requestParams.toString())
                .when()
                .post("/api/register")
                .then()
                .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }
}
