package br.com.botmoter.jackson.databind;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
public class IntegerToLocalDateTimeSerializer extends
		JsonSerializer<LocalDateTime> {
	@Override
	public void serialize(LocalDateTime localDateTime,
			JsonGenerator jsonGenerator,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {

		if (localDateTime == null) {
			jsonGenerator.writeNull();
			return;
		}

		long unix = localDateTime.atZone(ZoneOffset.systemDefault()).toInstant
				().getEpochSecond();
		jsonGenerator.writeNumber(Long.valueOf(unix).intValue());
	}
}
