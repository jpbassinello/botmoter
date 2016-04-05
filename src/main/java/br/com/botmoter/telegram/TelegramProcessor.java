package br.com.botmoter.telegram;

import br.com.botmoter.telegram.model.Update;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
public interface TelegramProcessor {

	public void processUpdate(Update update);
}
