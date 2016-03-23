package br.com.botmoter.web.controller;

import com.jayway.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
public class TelegramRestIntegrationTest {

	@Test
	public void firstEchoTest() {
		RestAssured.get("/echo/hello").then().assertThat().body("message",
				Matchers.equalTo("hello"));
	}
}
