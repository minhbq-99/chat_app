
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
}
