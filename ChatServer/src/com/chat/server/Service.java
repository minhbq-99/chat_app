package com.chat.server;
import java.net.*;
import java.io.*;

public class Service implements Runnable
{
	private Socket serviceSocket;
	
	public Service(Socket serviceSocket)
	{
		this.serviceSocket = serviceSocket;
	}
	
	public void sendMsg(OutputStream output, String data) throws IOException 
	{
			output.write(data.getBytes());
	}
	
	// readUntil delimiter (exclusively)
	public String readUntil(InputStream input, char delimiter) throws IOException
	{
		String result = ""; 
		while(true)
		{
			char tmp = input.readNBytes(1).toString().charAt(0);
			if (tmp == delimiter)
				break;
			result += tmp;
		}
		return result;
	}
	
	private boolean login(String username, String password)
	{
		for (User user: Server.userList)
		{
			if (user.isEqual(username, password))
			{
				user.setIsOnline(true);
				return true;
			}
		}
		return false;
	}
	
	private boolean register(String username, String password)
	{
		for (User user: Server.userList)
		{
			if (user.isEqual(username, password))
				return false;
		}
		User newUser = new User(username,password);
		Server.userList.add(newUser);
		return true;
	}
	
	@Override
	public void run()
	{
		try 
		{
			InputStream input = this.serviceSocket.getInputStream();
			OutputStream output = this.serviceSocket.getOutputStream();
			while(true)
			{
				String header = readUntil(input,'\n');
				String username, password;
				switch(header)
				{
				case "LOGIN":
					username = readUntil(input,'\n');
					password = readUntil(input,'\n');
					if (login(username,password))
						sendMsg(output, "LOGIN SUCESSFUL\n");
					else 
						sendMsg(output, "LOGIN FAILED\n");
					break;
				case "REGISTER":
					username = readUntil(input,'\n');
					password = readUntil(input,'\n');
					if (register(username,password))
						sendMsg(output, "REGISTER SUCESSFUL\n");
					else 
						sendMsg(output, "REGISTER FAILED\n");
					break;
				}
			}
		}
		catch (IOException e)
		{
			try 
			{
				this.serviceSocket.close();
			}
			catch (IOException ex) {}
		}
	}
}
