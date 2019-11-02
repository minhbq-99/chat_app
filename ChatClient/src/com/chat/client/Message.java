package com.chat.client;

public class Message {
	public String msg;
	public boolean isPeer;
	
	public Message(String msg, boolean isPeer)
	{
		this.msg = msg;
		this.isPeer = isPeer;
	}
}
