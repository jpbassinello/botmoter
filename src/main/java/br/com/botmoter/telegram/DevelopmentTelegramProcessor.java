package br.com.botmoter.telegram;

import br.com.botmoter.telegram.model.Update;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
@Component
@Profile("development")
public class DevelopmentTelegramProcessor implements TelegramProcessor {

	public void processUpdate(Update update) {
		// do nothing on development
	}
}