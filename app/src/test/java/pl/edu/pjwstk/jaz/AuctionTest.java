package pl.edu.pjwstk.jaz;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import pl.edu.pjwstk.jaz.config.IntegrationTest;
import pl.edu.pjwstk.jaz.requests.*;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

@RunWith(SpringRunner.class)
@IntegrationTest
public class AuctionTest {

    @BeforeClass
    public static void usersRegistration() {
        List<PhotoRequest> photos = new ArrayList<>();
        photos.add(new PhotoRequest("link1", 1));
        photos.add(new PhotoRequest("link2", 2));

        List<ParameterRequest> parameters = new ArrayList<>();
        parameters.add(new ParameterRequest("key1", "value1"));
        parameters.add(new ParameterRequest("key2", "value2"));

        given()
                .contentType(ContentType.JSON)
                .body(new RegisterRequest("admin", "admin", "admin", "admin"))
                .when()
                .post("/api/register")
                .thenReturn();

        given()
                .contentType(ContentType.JSON)
                .body(new RegisterRequest("user", "user", "user", "user"))
                .when()
                .post("/api/register")
                .thenReturn();

        var response = given()
                .contentType(ContentType.JSON)
                .body(new LoginRequest("admin", "admin"))
                .when()
                .post("/api/login")
                .thenReturn();

        given()
                .cookies(response.getCookies())
                .when()
                .post("/api/addSection/section")
                .thenReturn();

        given()
                .cookies(response.getCookies())
                .when()
                .post("/api/addCategory/3/category")
                .thenReturn();

        given()
                .cookies(response.getCookies())
                .contentType(ContentType.JSON)
                .body(new AuctionRequest("title","description",10.00,4,photos,parameters))
                .when()
                .post("/api/addAuction")
                .thenReturn();
    }

    @Test
    public void addSectionShouldReturn200AsAdmin() {
        var response = given()
                .contentType(ContentType.JSON)
                .body(new LoginRequest("admin","admin"))
                .when()
                .post("/api/login")
                .thenReturn();
        given()
                .cookies(response.getCookies())
                .post("/api/addSection/test")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void addSectionShouldReturn403AsUser() {
        var response = given()
                .contentType(ContentType.JSON)
                .body(new LoginRequest("user","user"))
                .when()
                .post("/api/login")
                .thenReturn();
        given()
                .cookies(response.getCookies())
                .post("/api/addSection/section1")
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    public void addCategoryShouldReturn200AsAdmin() {
        var response = given()
                .contentType(ContentType.JSON)
                .body(new LoginRequest("admin","admin"))
                .when()
                .post("/api/login")
                .thenReturn();
        given()
                .cookies(response.getCookies())
                .post("/api/addCategory/3/test")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void addCategoryShouldReturn403AsUser() {
        var response = given()
                .contentType(ContentType.JSON)
                .body(new LoginRequest("user","user"))
                .when()
                .post("/api/login")
                .thenReturn();
        given()
                .cookies(response.getCookies())
                .post("/api/addCategory/3/category1")
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    public void editSectionShouldReturn200AsAdmin() {
        var response = given()
                .contentType(ContentType.JSON)
                .body(new LoginRequest("admin","admin"))
                .when()
                .post("/api/login")
                .thenReturn();
        given()
                .cookies(response.getCookies())
                .put("/api/editSection/3/section1")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void editSectionShouldReturn403AsUser() {
        var response = given()
                .contentType(ContentType.JSON)
                .body(new LoginRequest("user","user"))
                .when()
                .post("/api/login")
                .thenReturn();
        given()
                .cookies(response.getCookies())
                .put("/api/editSection/3/section1")
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    public void editCategoryShouldReturn200AsAdmin() {
        var response = given()
                .contentType(ContentType.JSON)
                .body(new LoginRequest("admin","admin"))
                .when()
                .post("/api/login")
                .thenReturn();
        given()
                .cookies(response.getCookies())
                .put("/api/editCategory/4/category1")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void editCategoryShouldReturn403AsUser() {
        var response = given()
                .contentType(ContentType.JSON)
                .body(new LoginRequest("user","user"))
                .when()
                .post("/api/login")
                .thenReturn();
        given()
                .cookies(response.getCookies())
                .put("/api/editCategory/4/category1")
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    public void addSectionShouldReturn422WhenSectionAlreadyExist() {
        var response = given()
                .contentType(ContentType.JSON)
                .body(new LoginRequest("admin","admin"))
                .when()
                .post("/api/login")
                .thenReturn();
        given()
                .cookies(response.getCookies())
                .post("/api/addSection/section")
                .then()
                .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }

    @Test
    public void editSectionShouldReturn404WhenSectionNotFound() {
        var response = given()
                .contentType(ContentType.JSON)
                .body(new LoginRequest("admin","admin"))
                .when()
                .post("/api/login")
                .thenReturn();
        given()
                .cookies(response.getCookies())
                .put("/api/editSection/1/test")
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void editCategoryShouldReturn404WhenCategoryNotFound() {
        var response = given()
                .contentType(ContentType.JSON)
                .body(new LoginRequest("admin","admin"))
                .when()
                .post("/api/login")
                .thenReturn();
        given()
                .cookies(response.getCookies())
                .put("/api/editCategory/1/test")
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void addAuctionShouldReturn200() {
        List<PhotoRequest> photos = new ArrayList<>();
        photos.add(new PhotoRequest("link1", 1));
        photos.add(new PhotoRequest("link2", 2));

        List<ParameterRequest> parameters = new ArrayList<>();
        parameters.add(new ParameterRequest("key1", "value1"));
        parameters.add(new ParameterRequest("key2", "value2"));

        var response = given()
                .contentType(ContentType.JSON)
                .body(new LoginRequest("admin","admin"))
                .when()
                .post("/api/login")
                .thenReturn();
        given()
                .cookies(response.getCookies())
                .contentType(ContentType.JSON)
                .body(new AuctionRequest("title","description",10.00,4,photos,parameters))
                .when()
                .post("/api/addAuction")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void addAuctionShouldReturn400WhenCategoryDoesNotExist() {
        List<PhotoRequest> photos = new ArrayList<>();
        photos.add(new PhotoRequest("link1", 1));
        photos.add(new PhotoRequest("link2", 2));

        List<ParameterRequest> parameters = new ArrayList<>();
        parameters.add(new ParameterRequest("key1", "value1"));
        parameters.add(new ParameterRequest("key2", "value2"));

        var response = given()
                .contentType(ContentType.JSON)
                .body(new LoginRequest("admin","admin"))
                .when()
                .post("/api/login")
                .thenReturn();
        given()
                .cookies(response.getCookies())
                .contentType(ContentType.JSON)
                .body(new AuctionRequest("title","description",10.00,1,photos,parameters))
                .when()
                .post("/api/addAuction")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void listAllAuctionShouldReturn200() {
        var response = given()
                .contentType(ContentType.JSON)
                .body(new LoginRequest("admin","admin"))
                .when()
                .post("/api/login")
                .thenReturn();
        given()
                .cookies(response.getCookies())
                .when()
                .get("/api/auctionList")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void showAuctionShouldReturn200() {
        var response = given()
                .contentType(ContentType.JSON)
                .body(new LoginRequest("admin","admin"))
                .when()
                .post("/api/login")
                .thenReturn();
        given()
                .cookies(response.getCookies())
                .when()
                .get("/api/showAuction/5")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void editAuctionShouldReturn200WhenEditedByCreator() {
        List<PhotoRequest> photos = new ArrayList<>();
        photos.add(new PhotoRequest("link1", 1));
        photos.add(new PhotoRequest("link2", 2));
        photos.add(new PhotoRequest("link3", 3));

        List<ParameterRequest> parameters = new ArrayList<>();
        parameters.add(new ParameterRequest("key1", "value1"));
        parameters.add(new ParameterRequest("key2", "value5"));
        parameters.add(new ParameterRequest("key3", "value3"));

        var response = given()
                .contentType(ContentType.JSON)
                .body(new LoginRequest("admin","admin"))
                .when()
                .post("/api/login")
                .thenReturn();
        given()
                .cookies(response.getCookies())
                .contentType(ContentType.JSON)
                .body(new AuctionRequest("test","test",10.00,4,photos,parameters))
                .when()
                .put("/api/editAuction/5/1")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void editAuctionShouldReturn403WhenEditedByOtherUser() {
        List<PhotoRequest> photos = new ArrayList<>();
        photos.add(new PhotoRequest("link1", 1));
        photos.add(new PhotoRequest("link2", 2));

        List<ParameterRequest> parameters = new ArrayList<>();
        parameters.add(new ParameterRequest("key1", "value1"));
        parameters.add(new ParameterRequest("key2", "value2"));

        var response = given()
                .contentType(ContentType.JSON)
                .body(new LoginRequest("user","user"))
                .when()
                .post("/api/login")
                .thenReturn();
        given()
                .cookies(response.getCookies())
                .contentType(ContentType.JSON)
                .body(new AuctionRequest("title","description",10.00,4,photos,parameters))
                .when()
                .put("/api/editAuction/5/1")
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    public void editAuctionShouldReturn400WhenCategoryNotFound() {
        List<PhotoRequest> photos = new ArrayList<>();
        photos.add(new PhotoRequest("link1", 1));
        photos.add(new PhotoRequest("link2", 2));

        List<ParameterRequest> parameters = new ArrayList<>();
        parameters.add(new ParameterRequest("key1", "value1"));
        parameters.add(new ParameterRequest("key2", "value2"));

        var response = given()
                .contentType(ContentType.JSON)
                .body(new LoginRequest("admin","admin"))
                .when()
                .post("/api/login")
                .thenReturn();
        given()
                .cookies(response.getCookies())
                .contentType(ContentType.JSON)
                .body(new AuctionRequest("title","description",10.00,1,photos,parameters))
                .when()
                .put("/api/editAuction/5/1")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void editAuctionAtTheSameTimeShouldReturn409() {
        List<PhotoRequest> photos = new ArrayList<>();
        photos.add(new PhotoRequest("link1", 1));
        photos.add(new PhotoRequest("link2", 2));

        List<ParameterRequest> parameters = new ArrayList<>();
        parameters.add(new ParameterRequest("key1", "value1"));
        parameters.add(new ParameterRequest("key2", "value2"));

        var response = given()
                .contentType(ContentType.JSON)
                .body(new LoginRequest("admin","admin"))
                .when()
                .post("/api/login")
                .thenReturn();

        given()
                .cookies(response.getCookies())
                .contentType(ContentType.JSON)
                .body(new AuctionRequest("title","description",10.00,4,photos,parameters))
                .when()
                .put("/api/editAuction/5/1")
                .thenReturn();

        given()
                .cookies(response.getCookies())
                .contentType(ContentType.JSON)
                .body(new AuctionRequest("title","description",10.00,4,photos,parameters))
                .when()
                .put("/api/editAuction/5/1")
                .then()
                .statusCode(HttpStatus.SC_CONFLICT);
    }

    @Test
    public void editAuctionOneByOneShouldReturn200() {
        List<PhotoRequest> photos = new ArrayList<>();
        photos.add(new PhotoRequest("link1", 1));
        photos.add(new PhotoRequest("link2", 2));

        List<ParameterRequest> parameters = new ArrayList<>();
        parameters.add(new ParameterRequest("key1", "value1"));
        parameters.add(new ParameterRequest("key2", "value2"));

        var response = given()
                .contentType(ContentType.JSON)
                .body(new LoginRequest("admin","admin"))
                .when()
                .post("/api/login")
                .thenReturn();

        given()
                .cookies(response.getCookies())
                .contentType(ContentType.JSON)
                .body(new AuctionRequest("title","description",10.00,4,photos,parameters))
                .when()
                .put("/api/editAuction/5/1")
                .thenReturn();

        given()
                .cookies(response.getCookies())
                .contentType(ContentType.JSON)
                .body(new AuctionRequest("title","description",10.00,4,photos,parameters))
                .when()
                .put("/api/editAuction/5/2")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

}