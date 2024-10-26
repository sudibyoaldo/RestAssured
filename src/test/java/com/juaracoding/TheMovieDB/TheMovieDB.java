package com.juaracoding.TheMovieDB;
import com.juaracoding.drivers.DriverSingleton;
import com.juaracoding.pages.HomePage;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import static io.restassured.RestAssured.given;

public class TheMovieDB {

    JSONObject requestBody;
    Response response = null;
    String token = null;
    String movieId;
    String movieName;
    String movieIdResult;
    private WebDriver driver;
    private HomePage homePage;
    private boolean isMovieNameDisplayed=false;
    String statusMessage;
    int statusCode;
    boolean status;

    @BeforeClass
    @Parameters({"URL","Browser"})
    public void setUp(String URL, String Browser){
        requestBody = new JSONObject();
        RestAssured.baseURI="https://api.themoviedb.org/3/movie";
        DriverSingleton.getInstance(Browser);
        driver = DriverSingleton.getDriver();
        driver.get(URL);
        homePage = new HomePage();
    }

    @AfterClass
    public void finish(){
        DriverSingleton.closeObjectInstance();
    }

    @Test (priority = 1)
    public void testGetMoviesNowPlayingWithoutAuth(){
        RequestSpecification request = given();
        response = request.get("/now_playing");
        status = response.getBody().jsonPath().getBoolean("success");
        statusCode = response.getBody().jsonPath().getInt("status_code");
        statusMessage = response.getBody().jsonPath().getString("status_message");
        status = response.getBody().jsonPath().getBoolean("success");
        statusCode = response.getBody().jsonPath().getInt("status_code");
        statusMessage = response.getBody().jsonPath().getString("status_message");
        Assert.assertEquals(response.getStatusCode(),401);
        Assert.assertFalse(status);
        Assert.assertEquals(statusMessage,"Invalid API key: You must be granted a valid key.");
        Assert.assertEquals(statusCode,7);
    }

    @Test (priority = 2)
    public void testGetMoviesNowPlaying(){
        RequestSpecification request = given();
        request.header("Authorization","Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI3MTNkZmQyMGEwOGIxMTM5ZTM0YjQyYTQ2YjU2NjVhYyIsIm5iZiI6MTcyOTg2OTU4My42MDU2MjUsInN1YiI6IjY3MWJiNDQ2OWZmNjgxZDllMGE0MDkxMyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.x_nn493dOgBUJdc7satzuTnP_djI1ZKpbZ3QIR1HzqM");
        request.header("accept","application/json");
        response = request.get("/now_playing");
        movieId = response.getBody().jsonPath().getString("results[0].id");
        movieName = response.getBody().jsonPath().getString("results[0].original_title");
        homePage.moviesBtn();
        homePage.getMoviesCategory("Now Playing");
        isMovieNameDisplayed = driver.findElement(By.xpath("//img[@alt='"+movieName+"']")).isDisplayed();
        Assert.assertEquals(response.getStatusCode(),200);
        Assert.assertNotNull(movieName);
        Assert.assertTrue(isMovieNameDisplayed);
    }

    @Test(priority = 3)
    public void testGetMoviesPopular(){
        RequestSpecification request = given();
        request.header("Authorization","Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI3MTNkZmQyMGEwOGIxMTM5ZTM0YjQyYTQ2YjU2NjVhYyIsIm5iZiI6MTcyOTg2OTU4My42MDU2MjUsInN1YiI6IjY3MWJiNDQ2OWZmNjgxZDllMGE0MDkxMyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.x_nn493dOgBUJdc7satzuTnP_djI1ZKpbZ3QIR1HzqM");
        request.header("accept","application/json");
        response = request.get("/popular");
        movieId = response.getBody().jsonPath().getString("results[0].id");
        movieName = response.getBody().jsonPath().getString("results[0].original_title");
        homePage.moviesBtn();
        homePage.getMoviesCategory("Popular");
        isMovieNameDisplayed = driver.findElement(By.xpath("//img[@alt='"+movieName+"']")).isDisplayed();
        Assert.assertEquals(response.getStatusCode(),200);
        Assert.assertNotNull(movieName);
        Assert.assertTrue(isMovieNameDisplayed);
    }

    @Test(priority = 4)
    public void testSetMovieAddRatingWithInvalidMovieId(){
        RequestSpecification request = given();
        request.header("Authorization","Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI3MTNkZmQyMGEwOGIxMTM5ZTM0YjQyYTQ2YjU2NjVhYyIsIm5iZiI6MTcyOTg2OTU4My42MDU2MjUsInN1YiI6IjY3MWJiNDQ2OWZmNjgxZDllMGE0MDkxMyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.x_nn493dOgBUJdc7satzuTnP_djI1ZKpbZ3QIR1HzqM");
        request.header("accept","application/json");
        request.header("Content-Type","application/json;charset=utf-8");
        requestBody.put("value","10");
        request.body(requestBody.toJSONString());
        response = request.post("/InvalidId/rating");
        status = response.getBody().jsonPath().getBoolean("success");
        statusCode = response.getBody().jsonPath().getInt("status_code");
        statusMessage = response.getBody().jsonPath().getString("status_message");
        Assert.assertEquals(response.getStatusCode(),404);
        Assert.assertFalse(status);
        Assert.assertEquals(statusMessage,"Invalid id: The pre-requisite id is invalid or not found.");
        Assert.assertEquals(statusCode,6);
    }

    @Test(priority = 5)
    public void testSetMovieAddRatingWithInvalidValue(){
        RequestSpecification request = given();
        request.header("Authorization","Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI3MTNkZmQyMGEwOGIxMTM5ZTM0YjQyYTQ2YjU2NjVhYyIsIm5iZiI6MTcyOTg2OTU4My42MDU2MjUsInN1YiI6IjY3MWJiNDQ2OWZmNjgxZDllMGE0MDkxMyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.x_nn493dOgBUJdc7satzuTnP_djI1ZKpbZ3QIR1HzqM");
        request.header("accept","application/json");
        request.header("Content-Type","application/json;charset=utf-8");
        requestBody.put("value","Good");
        request.body(requestBody.toJSONString());
        response = request.post("/"+movieId+"/rating");
        status = response.getBody().jsonPath().getBoolean("success");
        statusCode = response.getBody().jsonPath().getInt("status_code");
        statusMessage = response.getBody().jsonPath().getString("status_message");
        Assert.assertEquals(response.getStatusCode(),400);
        Assert.assertFalse(status);
        Assert.assertEquals(statusMessage,"Value too low: Value must be greater than 0.0.");
        Assert.assertEquals(statusCode,18);
    }

    @Test(priority = 6)
    public void testSetMovieAddRating(){
        RequestSpecification request = given();
        request.header("Authorization","Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI3MTNkZmQyMGEwOGIxMTM5ZTM0YjQyYTQ2YjU2NjVhYyIsIm5iZiI6MTcyOTg2OTU4My42MDU2MjUsInN1YiI6IjY3MWJiNDQ2OWZmNjgxZDllMGE0MDkxMyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.x_nn493dOgBUJdc7satzuTnP_djI1ZKpbZ3QIR1HzqM");
        request.header("accept","application/json");
        request.header("Content-Type","application/json;charset=utf-8");
        requestBody.put("value","8.5");
        request.body(requestBody.toJSONString());
        response = request.post("/"+movieId+"/rating");
        status = response.getBody().jsonPath().getBoolean("success");
        statusCode = response.getBody().jsonPath().getInt("status_code");
        statusMessage = response.getBody().jsonPath().getString("status_message");
        //Warning : Please Run DeleteRating if you want pass an Assert for the second test
        Assert.assertEquals(response.getStatusCode(),201);
        Assert.assertTrue(status);
        Assert.assertEquals(statusMessage,"Success.");
        Assert.assertEquals(statusCode,1);
    }

    @Test(priority = 7)
    public void testGetMovieDetails(){
        RequestSpecification request = given();
        request.header("Authorization","Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI3MTNkZmQyMGEwOGIxMTM5ZTM0YjQyYTQ2YjU2NjVhYyIsIm5iZiI6MTcyOTg2OTU4My42MDU2MjUsInN1YiI6IjY3MWJiNDQ2OWZmNjgxZDllMGE0MDkxMyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.x_nn493dOgBUJdc7satzuTnP_djI1ZKpbZ3QIR1HzqM");
        request.header("accept","application/json");
        response = request.get("/"+movieId);
        movieIdResult = response.getBody().jsonPath().getString("id");
        movieName = response.getBody().jsonPath().getString("original_title");
        Assert.assertEquals(response.getStatusCode(),200);
        Assert.assertEquals(movieId,movieIdResult);
        Assert.assertNotNull(movieName);
    }
}