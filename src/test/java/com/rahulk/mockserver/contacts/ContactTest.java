package com.rahulk.mockserver.contacts;

import com.google.gson.GsonBuilder;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.rahulk.mockserver.contacts.message.ContactDTO;
import com.rahulk.mockserver.contacts.message.Error.ErrorsDTO;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;

/**
 * Created by rahulk on 14-06-2016.
 */
public class ContactTest {
    String url;
    Properties properties = new Properties();
    InputStream inputStream = ContactTest.class.getClassLoader().getResourceAsStream("url.properties");

    @BeforeClass
    public void getUrl() {
        if (inputStream != null) {
            try {
                properties.load(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        url = properties.getProperty("url");
    }

    @Test(priority = 0)
    public void addContact() {
        String message = "{\"name\": \"surya prasad\", \"email\": \"surya@gmail.com\"}";
        Response response = RestAssured.given().contentType(javax.ws.rs.core.MediaType.APPLICATION_JSON).
                body(message).post(url + "contacts");
        assertEquals(response.statusCode(), 200);
        assertTrue(com.rahulk.mockserver.contacts.utils.JsonUtils.compare(message, response.asString()));
    }

    @Test(priority = 1)
    public void addSameContact() {
        String message = "{\"name\": \"surya prasad\", \"email\": \"surya@gmail.com\"}";
        Response response = RestAssured.given().contentType(javax.ws.rs.core.MediaType.APPLICATION_JSON).
                body(message).post(url + "contacts");
        assertEquals(response.statusCode(), 409);
        ErrorsDTO errorsDTO = new GsonBuilder().serializeNulls().create().fromJson(response.asString(), ErrorsDTO.class);
        assertEquals(errorsDTO.getErrors().get(0).getCode(), 101);
    }

    @Test(priority = 1)
    public void getContactByEmail() {
        Response response = RestAssured.given().param("email", "surya@gmail.com").get(url + "contacts");
        assertEquals(response.statusCode(), 200);
        ContactDTO[] contactDTO = new GsonBuilder().serializeNulls().create().fromJson(response.asString(), ContactDTO[].class);
        assertEquals(contactDTO[0].getEmail(), "surya@gmail.com");
    }

    @Test(priority = 1)
    public void getContactByName() {
        Response response = RestAssured.given().param("name", "surya").get(url + "contacts");
        assertEquals(response.statusCode(), 200);
        ContactDTO[] contactDTO = new GsonBuilder().serializeNulls().create().fromJson(response.asString(), ContactDTO[].class);
        assertEquals(contactDTO[0].getEmail(), "surya@gmail.com");
    }

    @Test(priority = 1)
    public void getContactByEmailAndName() {
        Response response = RestAssured.given().param("email", "surya@gmail.com").param("name", "surya").get(url + "contacts");
        assertEquals(response.statusCode(), 200);
        ContactDTO[] contactDTO = new GsonBuilder().serializeNulls().create().fromJson(response.asString(), ContactDTO[].class);
        assertEquals(contactDTO[0].getEmail(), "surya@gmail.com");
    }

    @Test(priority = 1)
    public void getContactByEmailLessChar() {
        Response response = RestAssured.given().param("email", "su").get(url + "contacts");
        assertEquals(response.statusCode(), 400);
        ErrorsDTO errorsDTO = new GsonBuilder().serializeNulls().create().fromJson(response.asString(), ErrorsDTO.class);
        assertEquals(errorsDTO.getErrors().get(0).getCode(), 112);
        System.out.println("FIRST : " + response.asString());
    }

    @Test(priority = 1)
    public void getContactByNameLessChar() {
        Response response = RestAssured.given().param("name", "su").get(url + "contacts");
        assertEquals(response.statusCode(), 400);
        ErrorsDTO errorsDTO = new GsonBuilder().serializeNulls().create().fromJson(response.asString(), ErrorsDTO.class);
        assertEquals(errorsDTO.getErrors().get(0).getCode(), 112);
    }

    @Test(priority = 1)
    public void getContactByEmptyEmail() {
        Response response = RestAssured.given().param("email", "").get(url + "contacts");
        assertEquals(response.statusCode(), 400);
        ErrorsDTO errorsDTO = new GsonBuilder().serializeNulls().create().fromJson(response.asString(), ErrorsDTO.class);
        assertEquals(errorsDTO.getErrors().get(0).getCode(), 112);
    }

    @Test(priority = 1)
    public void getContactNoParam() {
        Response response = RestAssured.given().get(url + "contacts");
        assertEquals(response.statusCode(), 400);
        ErrorsDTO errorsDTO = new GsonBuilder().serializeNulls().create().fromJson(response.asString(), ErrorsDTO.class);
        assertEquals(errorsDTO.getErrors().get(0).getCode(), 111);
    }

    @Test(priority = 2)
    public void deleteContact() {
        Response response = RestAssured.given().delete(url + "contacts/email/surya@gmail.com");
        assertEquals(response.statusCode(), 200);
        ContactDTO contactDTO = new GsonBuilder().serializeNulls().create().fromJson(response.asString(), ContactDTO.class);
        assertEquals(contactDTO.getEmail(), "surya@gmail.com");
    }

    @Test(priority = 3)
    public void deleteContactAgain() {
        Response response = RestAssured.given().delete(url + "contacts/email/surya@gmail.com");
        assertEquals(response.statusCode(), 400);
        ErrorsDTO errorsDTO = new GsonBuilder().serializeNulls().create().fromJson(response.asString(), ErrorsDTO.class);
        assertEquals(errorsDTO.getErrors().get(0).getCode(), 102);
    }

    @Test(priority = 3)
    public void deleteNonExistingContact() {
        Response response = RestAssured.given().delete(url + "contacts/email/noone@gmail.com");
        assertEquals(response.statusCode(), 400);
        ErrorsDTO errorsDTO = new GsonBuilder().serializeNulls().create().fromJson(response.asString(), ErrorsDTO.class);
        assertEquals(errorsDTO.getErrors().get(0).getCode(), 102);
    }

    @AfterClass
    public void clean() {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
