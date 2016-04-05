package br.com.botmoter.jackson.databind;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.node.IntNode;
import org.joda.time.LocalDateTime;

import java.io.IOException;

public class IntegerToLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

	@Override
	public LocalDateTime deserialize(JsonParser jsonParser,
			DeserializationContext deserializationContext) throws IOException,
			JsonProcessingException {
		ObjectCodec oc = jsonParser.getCodec();
		IntNode node = (IntNode) oc.readTree(jsonParser);
		int dateUnix = node.getIntValue();

		return new LocalDateTime(dateUnix * 1000L);
	}

}