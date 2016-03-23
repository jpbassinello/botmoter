package br.com.botmoter.web.controller;

import br.com.botmoter.web.BotmoterApplication;
import com.jayway.restassured.RestAssured;
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

	@Test
	public void firstEchoTest() {
		RestAssured.get(TelegramRestController.UPDATES_REST_PATH).then()
				.assertThat().body("message", Matchers.equalTo("hello"));
	}
}
