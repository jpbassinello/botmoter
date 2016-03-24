package br.com.botmoter.web.controller;

import br.com.botmoter.web.BotmoterApplication;
import com.jayway.restassured.RestAssured;
import org.codehaus.jackson.map.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(BotmoterApplication.class)
@WebIntegrationTest
public class TelegramRestIntegrationTest {

	private static final String BODY_WITH_TEXT = "{\"update_id\":344901420," +
			"\"message\":{\"message_id\":8,\"from\":{\"id\":98891960," +
			"\"first_name\":\"Felipe\"," +
			"\"last_name\":\"Mir\",\"username\":\"Felipe_Mir\"}," +
			"\"chat\":{\"id\":98891960," +
			"\"first_name\":\"Felipe\",\"last_name\":\"Mir\"," +
			"\"username\":\"Felipe_Mir\"," +
			"\"type\":\"private\"},\"date\":1458404679,\"text\":\"Mensagem " +
			"de Texto com ACENTUAÇÃO\"}}";

	private static final String BODY_WITH_LOCATION = "{\"update_id\":344901419, " +
			"\"message\":{\"message_id\":7," +
			"\"from\":{\"id\":192468144,\"first_name\":\"Lucas\"," +
			"\"last_name\":\"Laurindo dos Santos\"}," +
			"\"chat\":{\"id\":192468144," +
			"\"first_name\":\"Lucas\",\"last_name\":\"Laurindo dos " +
			"Santos\",\"type\":\"private\"}," +
			"\"date\":1458403367,\"location\":{\"longitude\":-47.047745," +
			"\"latitude\":-22.893990}}}";

	@Test
	public void updatesRestTest() {
		ObjectMapper objectMapper = new ObjectMapper();

		RestAssured.given().body(BODY_WITH_TEXT).post(TelegramRestController.UPDATES_REST_PATH)
				.then().body("updateId", Matchers.equalTo(344901420)).body("message.from" + "" +
				".userName", Matchers.equalTo("Felipe_Mir")).body("message.text", Matchers.equalTo
				("Mensagem de Texto com ACENTUAÇÃO"));

		RestAssured.given().body(BODY_WITH_LOCATION).post(TelegramRestController
				.UPDATES_REST_PATH).then().body("updateId", Matchers.equalTo(344901419)).body
				("message.messageId", Matchers.equalTo(7)).body("message.location.longitude",
				Matchers.equalTo(-47.047745F)).body("message.location.latitude", Matchers.equalTo
				(-22.893990F));
	}
}
