package org.example;

import POJO.*;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;

public class APITest {

    Map<String, String> header = new HashMap<>();
    loginRequest loginRequest = new loginRequest();
    String productOrderId;
    loginResponse loginResponse;
    addProductResponse addProductResponse;

    @Test()
    public void authorization() {
        loginRequest.setUserEmail("krishnaprasath783@gmail.com");
        loginRequest.setUserPassword("Chennai@2025");
        RequestSpecification requestSpecification = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com/api/ecom/auth/login").setContentType(ContentType.JSON).build();
        loginResponse = given().spec(requestSpecification).log().all().body(loginRequest).post()
                .then().statusCode(200).extract().as(loginResponse.class);
        System.out.println(loginResponse.getToken());
        System.out.println(loginResponse.getUserId());
    }

    @Test(dependsOnMethods = {"authorization"})
    public void addProduct() {
        RequestSpecification reqspec = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addHeader("Authorization", loginResponse.getToken()).build();
        RequestSpecification reqbodyspec = given().spec(reqspec).log().all()
                .param("productName", "Long Top")
                .param("productAddedBy", loginResponse.getUserId())
                .param("productCategory", "fashion")
                .param("productSubCategory", "shirts")
                .param("productPrice", "15000")
                .param("productDescription", "Abibas Originals")
                .param("productFor", "women")
                .multiPart("productImage", new File("\\Users\\asus\\Downloads\\pngtree-ladies-dress-png-image_2401151.jpg"));
        addProductResponse = reqbodyspec.when().post("api/ecom/product/add-product").then().statusCode(201).extract().as(addProductResponse.class);
        System.out.println(addProductResponse.getProductId());

    }

    @Test(dependsOnMethods = {"addProduct","authorization"})
    public void Placeorder() {

        orderDetails orderDetails = new orderDetails();
        orderDetails.setCountry("India");
        orderDetails.setProductOrderedId(addProductResponse.getProductId());

        List<orderDetails> orderDetailsList = new ArrayList<orderDetails>();
        orderDetailsList.add(orderDetails);


        order order = new order();
        order.setOrders(orderDetailsList);


        RequestSpecification reqspec = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addHeader("Authorization", loginResponse.getToken()).setContentType(ContentType.JSON).build();
        String response = given().spec(reqspec).when().body(order).post("/api/ecom/order/create-order").then().log().all().statusCode(201).extract().asString();
        System.out.println(response);
       /* JsonPath js = new JsonPath(response);
        productOrderId = js.get("productOrderId");*/
    }

    @Test(dependsOnMethods = {"Placeorder"})
    public void getorderdetails() {
        RequestSpecification specification = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addQueryParam("id", productOrderId).build();
        given().spec(specification).post("api/ecom/order/get-orders-details").then().statusCode(200);

    }

    @Test(dependsOnMethods = {"addProduct","authorization","Placeorder"})
    public void deleteProduct(){
        RequestSpecification requestSpecification1 = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addHeader("Authorization",loginResponse.getToken()).build();
        given().log().all().spec(requestSpecification1).pathParam("productID", addProductResponse.getProductId()).when().delete("api/ecom/product/delete-product/{productID}")
                .then().log().all();
    }
}
