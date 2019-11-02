package com.chat.client;

import java.net.*;
import java.util.LinkedList;
import java.util.List;

public class Peer {
	public String name;
	public Socket peerSocket;
	public boolean isOnline;
	public List<Message> listMsg;
	
	public Peer(String name, boolean isOnline)
	{
		this.name = name;
		this.isOnline = isOnline;
		this.peerSocket = null;
		this.listMsg = new LinkedList<>();
	}
	
	public String toString()
	{
		if (isOnline)
			return name + "------------------online"; 
		else
			return name;
	}
}

