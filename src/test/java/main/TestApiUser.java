package main;

import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import base.EnviromentUtil;
import io.restassured.RestAssured;
import io.restassured.path.xml.XmlPath;
import io.restassured.path.xml.XmlPath.CompatibilityMode;
import io.restassured.response.Response;
import model.CreateUserData;
import model.CreateUserResponse;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.testng.annotations.BeforeMethod;

public class TestApiUser {
	String token = null;
	CreateUserResponse userResponse = null;

	@Test(priority = 1)
	public void getKeyToken() {
		RestAssured.basePath = "/access-token";
		Response res = RestAssured.given().header("Cookie", EnviromentUtil.COOKIE_GITHUB).when().get();
		XmlPath doc = new XmlPath(
                CompatibilityMode.HTML,
                res.asString());
//        System.out.println("title"+ doc.getString("html.head.title")); // get title html
        token = doc.getString("html.body.div.find{it.@class == 'container'}.div.find{it.@class=='jumbotron'}.samp.find{it.@class=='user-select-all text-break'}");
//        System.out.println("access token "+token);
//        String content2 = doc.getString("**.findAll { it.@class == 'content' }[1]");
        assert token != null : "Get token fail !";

	}

	@Test(priority = 2)
	public void createUser() {
		//System.out.println(token);
		RestAssured.basePath = "/public-api/users";
		Response res = RestAssured.given()
				.header("Accept", "application/json")
				.header("Content-Type", "application/json")
				.header("Cookie", "token=" + token)
				.header("Authorization", "Bearer "+ token)
				.when()
				.body(getDataCreateUser())
				.post();
		//res.prettyPrint();
		userResponse = new Gson().fromJson(res.asString(), CreateUserResponse.class);
		assert userResponse.getData().getName() != null : "Create User fail !";
	}
	
	@Test(priority = 3)
	public void updateUser() {
		RestAssured.basePath = "/public-api/users/" + userResponse.getData().getId();
		Response res = RestAssured.given()
				.header("Accept", "application/json")
				.header("Content-Type", "application/json")
				.header("Cookie", "token=" + token)
				.header("Authorization", "Bearer "+ token)
				.when()
				.body("{\"name\" : \"New Name\"}")
				.patch();
		//res.prettyPrint();  // truong hop khong log dc res ra la loi link url --> easy =))
		userResponse = new Gson().fromJson(res.asString(), CreateUserResponse.class);
		assert userResponse.getData().getName().equals("New Name") : "Update user fail !";
	}
	
	@Test(priority = 4)
	public void deleteUser() {
		RestAssured.basePath = "/public-api/users/" + userResponse.getData().getId();
		Response res = RestAssured.given()
				.header("Accept", "application/json")
				.header("Content-Type", "application/json")
				.header("Cookie", "token=" + token)
				.header("Authorization", "Bearer "+ token)
				.when()
				.delete();
		assert res.asString().contains("\"code\":204") : "Delete user fail !";
	}

	private String getDataCreateUser() {
		CreateUserData createUserData = new CreateUserData("Tran Trung Phong", "Male", randomeEmail(), "Active");
		return new Gson().toJson(createUserData);
	}

	private String randomeEmail() {
		int random = (int) Math.floor(((Math.random() * 9999) + 1));
		return "hoa.golden"+random+"@gmail.com";
	}

	@BeforeMethod
	public void beforeMethod() {
		RestAssured.baseURI = EnviromentUtil.URL;
	}

}
