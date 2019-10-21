package com.chat.client;
import java.net.*;
import java.io.*;

public class Client {
	public static boolean sendLogin(Socket socket, String username, String password)
	{
		try
		{
			String message = "LOGIN\n" + username + "\n" + password + "\n";
			OutputStream output = socket.getOutputStream();
			output.write(message.getBytes());
		}
		catch(IOException ex)
		{
			return false;
		}
		return true;
	}
}
