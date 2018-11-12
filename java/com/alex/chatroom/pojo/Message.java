package com.alex.chatroom.pojo;

import java.sql.Timestamp;

public class Message {

	 private String messageId;
	    private String fromId;
	    private String roomName;
	    public String getRoomName() {
			return roomName;
		}
		public void setRoomName(String roomName) {
			this.roomName = roomName;
		}
		private String fromName;
	    private String toId;
	    private String messageText;
	    private Timestamp messageDate;
		public String getMessageId() {
			return messageId;
		}
		public void setMessageId(String messageId) {
			this.messageId = messageId;
		}
		public String getFromId() {
			return fromId;
		}
		public void setFromId(String fromId) {
			this.fromId = fromId;
		}
		public String getFromName() {
			return fromName;
		}
		public void setFromName(String fromName) {
			this.fromName = fromName;
		}
		public String getToId() {
			return toId;
		}
		public void setToId(String toId) {
			this.toId = toId;
		}
		public String getMessageText() {
			return messageText;
		}
		public void setMessageText(String messageText) {
			this.messageText = messageText;
		}
		public Timestamp getMessageDate() {
			return messageDate;
		}
		public void setMessageDate(Timestamp messageDate) {
			this.messageDate = messageDate;
		}
		@Override
		public String toString() {
			return "Message [messageId=" + messageId + ", fromId=" + fromId + ", roomName=" + roomName + ", fromName="
					+ fromName + ", toId=" + toId + ", messageText=" + messageText + ", messageDate=" + messageDate
					+ "]";
		}
		
	   
}
