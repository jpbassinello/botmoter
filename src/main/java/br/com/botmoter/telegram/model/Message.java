package br.com.botmoter.telegram.model;

import br.com.botmoter.jackson.databind.IntegerToLocalDateTimeDeserializer;
import br.com.botmoter.jackson.databind.IntegerToLocalDateTimeSerializer;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.time.LocalDateTime;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Message {

	@JsonProperty("message_id")
	private int messageId;
	@JsonProperty("from")
	private User from;
	@JsonProperty("date")
	@JsonSerialize(using = IntegerToLocalDateTimeSerializer.class)
	@JsonDeserialize(using = IntegerToLocalDateTimeDeserializer.class)
	private LocalDateTime dateTime;
	@JsonProperty("chat")
	private Chat chat;
	@JsonProperty("text")
	private String text;
	@JsonProperty("location")
	private Location location;
	@JsonProperty("caption")
	private String caption;

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public User getFrom() {
		return from;
	}

	public void setFrom(User from) {
		this.from = from;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public Chat getChat() {
		return chat;
	}

	public void setChat(Chat chat) {
		this.chat = chat;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}
}
