package br.com.botmoter.jackson.databind;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.node.IntNode;
import org.codehaus.jackson.node.TextNode;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class IntegerToLocalDateTimeDeserializer extends
		JsonDeserializer<LocalDateTime> {

	@Override
	public LocalDateTime deserialize(JsonParser jsonParser,
			DeserializationContext deserializationContext) throws IOException,
			JsonProcessingException {
		ObjectCodec oc = jsonParser.getCodec();
		IntNode node = (IntNode) oc.readTree(jsonParser);
		int dateUnix = node.getIntValue();

		return LocalDateTime.ofInstant(Instant.ofEpochSecond(dateUnix), ZoneId
				.systemDefault());
	}

}