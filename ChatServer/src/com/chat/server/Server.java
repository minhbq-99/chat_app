package com.chat.server;
import java.net.*;
import java.io.*;
import java.util.concurrent.*;
import java.util.ArrayList;

public class Server 
{
	private static final int PORT = 2222;
	private static final int THREADCOUNT = 4;
	public static ArrayList<User> userList = new ArrayList<User>();
	
	public static void main(String[] argv)
	{
		System.out.println("[INFO] Start");
		ExecutorService pool = Executors.newFixedThreadPool(Server.THREADCOUNT);
		ServerSocket serverSocket = null;
		try
		{
			serverSocket = new ServerSocket(Server.PORT);
			while(true)
			{
				 Socket serviceSocket = serverSocket.accept();
				 System.out.println("[INFO] New connection from " + serviceSocket.getInetAddress());
				 Runnable thread = new Service(serviceSocket);
				 pool.submit(thread);
			}
		}
		catch (IOException e)
		{
			if (serverSocket != null) 
			{
				try 
				{
					serverSocket.close();
				}
				catch (IOException ex) {}
			}
		}
	}
}
