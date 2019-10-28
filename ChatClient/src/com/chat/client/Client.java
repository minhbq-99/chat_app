
package com.chat.client;
import java.net.*;
import java.io.*;

public class Client {
	public static boolean sendLogin(OutputStream output, String username, String password) throws IOException
	{
			String message = "LOGIN\n" + username + "\n" + password + "\n";
			output.write(message.getBytes());
			return true;
	}
	
	public static boolean sendRegister(OutputStream output, String username, String password) throws IOException
	{
		
		boolean isFound=username.indexOf(":")!=-1?true:false;
		boolean isFound1=username.indexOf("\n")!=-1?true:false;
		boolean isFound2=username.matches("[0-9a-zA-Z]+");

		if (!isFound&&!isFound1&&isFound2) {
			String message = "REGISTER\n" + username + "\n" + password + "\n";
			output.write(message.getBytes());
			return true;
			}
		return false;
	}
	
	public static boolean sendFriend_list(OutputStream output) throws IOException
	{
			String message = "SHOW FRIEND\n";
			output.write(message.getBytes());
			return true;
	}
	public static boolean sendAdd_friend(OutputStream output,String username) throws IOException
	{
			String message = "ADD FRIEND\n" +username+ "\n";
			output.write(message.getBytes());
			return true;
	}
	public static boolean sendAdd_friend_accept(OutputStream output) throws IOException
	{
			String message = "FRIEND REQUEST ACCEPTED\n";
			output.write(message.getBytes());
			return true;
	}
	public static boolean sendAdd_friend_rejected(OutputStream output) throws IOException
	{
			String message = "FRIEND REQUEST REJECTED\n";
			output.write(message.getBytes());
			return true;
	}
	public static boolean sendChoose_friend(OutputStream output,String username) throws IOException
	{
			String message = "CHAT\n" + username+"\n";
			output.write(message.getBytes());
			return true;
	}
	public static boolean sendChoose_friend_accepted(OutputStream output) throws IOException
	{
			String message = "CHAT REQUEST ACCEPTED\n";
			output.write(message.getBytes());
			return true;
	}
	public static boolean sendChoose_friend_rejected(OutputStream output) throws IOException
	{
			String message = "CHAT REQUEST REJECTED\n";
			output.write(message.getBytes());
			return true;
	}
	
	public static String readUntil(InputStream input, char delimiter, String result, boolean notBlock) throws IOException
	{
		try {
			while(true)
			{
				char tmp = new String(input.readNBytes(1)).charAt(0);
				if (tmp == delimiter)
					break;
				result += tmp;
			}
			return result;
		}
		catch (SocketTimeoutException e)
		{
			if(notBlock)
			{
				if (!result.equals(""))
					return readUntil(input,delimiter,result,notBlock);
				else
					return result;
			}
			else
				return readUntil(input,delimiter,result,notBlock);
		}
	}
	
	private static final String SERVERADDR = "localhost";
	private static final int SERVERPORT = 2222;
	
	public static void peerToPeer(InetAddress addr, int port, boolean isServer)
	{
		
		try 
		{
			if (isServer)
			{
				ServerSocket peerServerSocket = new ServerSocket(localPort);
				while (true)
				{
					peerSocket = peerServerSocket.accept();
					if (peerSocket.getInetAddress().equals(addr) && peerSocket.getPort() == port)
						break;
				}
				peerServerSocket.close();
			}
			else
			{
				peerSocket = new Socket(addr,port);
			}
			InputStream input = peerSocket.getInputStream();
			OutputStream output = peerSocket.getOutputStream();
			String header = readUntil(input,'\n',new String(""),true);
			int len = 0;
			switch (header)
			{
			case "MESSAGE":
				len = 0;
				try
				{
					len = Integer.parseInt(readUntil(input,'\n',new String(""),false));
				}
				catch (NumberFormatException e)
				{
					System.out.println("[CRITICAL] Malicious peer response");
					break;
				}
				String message = new String(input.readNBytes(len));
				break;
			case "FILE":
				len = 0;
				try
				{
					len = Integer.parseInt(readUntil(input,'\n',new String(""),false));
				}
				catch (NumberFormatException e)
				{
					System.out.println("[CRITICAL] Malicious peer response");
					break;
				}
				byte[] data = input.readNBytes(len);
				break;
			default:
				System.out.println("[CRITICAL] Malicious peer message");
			}
		}
		catch (IOException e)
		{
			System.out.println("[ERROR] Cannot establish connection");
		}
	}
	
	public static void clientServer()
	{
		try
		{
			serverSocket = new Socket(SERVERADDR,SERVERPORT);
			InputStream input = serverSocket.getInputStream();
			OutputStream output = serverSocket.getOutputStream();
			serverSocket.setSoTimeout(1000);
			serverSocket.setReuseAddress(true);
			localPort = serverSocket.getLocalPort();
			
			while (true)
			{
				String header = readUntil(input,'\n',new String(""),true);
				switch (header)
				{
				case "LOGIN SUCESSFUL":
					break;
				case "LOGIN FAILED":
					break;
				case "REGISTER SUCCESSFUL":
					break;
				case "REGISTER FAILED":
					break;
				case "FRIEND STATUS":
					int numOfFriends = 0;
					try
					{
						numOfFriends = Integer.parseInt(readUntil(input,'\n',new String(""),false));
						for (int i = 0; i < numOfFriends; i++)
						{
							String[] friends = readUntil(input,'\n',new String(""),false).split(": ");
							if (friends[1].equals("online"))
							{
								
							}
							else
							{
								
							}
						}
					}
					catch (NumberFormatException e)
					{
						System.out.println("[CRITICAL] Malicious server response " + e);
					}
					break;
				case "FRIEND REQUEST":
					String friendName = readUntil(input,'\n',new String(""),false);
					break;
				case "CHAT NOT AVAILABLE":
					break;
				case "CHAT REJECTED":
					break;
				case "CHAT INFO":
					String[] response = readUntil(input,'\n',new String(""),false).split(":");
					InetAddress addr = InetAddress.getByName(response[0]);
					int port = 0;
					int isServer = 0;
					try
					{
						port = Integer.parseInt(response[1]);
						isServer = Integer.parseInt(response[2]);
						peerToPeer(addr,port,isServer==1);
					}
					catch (NumberFormatException e)
					{
						System.out.println("[CRITICAL] Malicious server response " + e);
					}			
					break;
				default:
					System.out.println("[CRITICAL] Malicious server message ");
				}
			}
		}
		catch (IOException e)
		{
			System.out.println("[ERROR] Cannot connect to the server");
		}
	}
	
	public static void main(String[] argv)
	{
		clientServer();
	}
	
	private static int localPort;
	public static Socket serverSocket;
	public static Socket peerSocket;
}
