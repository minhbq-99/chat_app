package com.chat.server;
import java.net.*;
import java.io.*;
import java.util.concurrent.*;
import java.util.ArrayList;

public class Server 
{
	private static final int PORT = 2222;
	private static final int THREADCOUNT = 4;
	public static BlockingQueue<User> userList = new LinkedBlockingDeque<User>();
	public static ArrayList<Service> serviceList = new ArrayList<Service>();
	
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
				 Service service = new Service(serviceSocket);
				 serviceList.add(service);
				 pool.submit(service);
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
