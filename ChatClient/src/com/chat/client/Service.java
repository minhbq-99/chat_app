package com.chat.client;

import java.net.*;

import javax.swing.JOptionPane;

import java.io.*;

public interface Service {
	public static Socket createServerSocket(String addr, int port)
	{
		Socket socket;
		try
		{
			socket = new Socket(addr,port);
			//Client.localPort = socket.getLocalPort();
			socket.setSoTimeout(1000);
			socket.setReuseAddress(true);
		}
		catch (IOException e)
		{
			socket = null;
		}
		return socket;
	}
	
	public static ServerSocket createBindSocket()
	{
		ServerSocket peerServerSocket = null;
		try 
		{
			peerServerSocket = new ServerSocket(0);
		}
		catch (IOException e)
		{
			System.out.println(e);
		}
		return peerServerSocket;
	}
	
	public static boolean sendLogin(String username, String password)
	{
		try 
		{
			if (Client.serverSocket == null) Client.serverSocket = createServerSocket(Client.SERVERADDR,Client.SERVERPORT);
			OutputStream output = Client.serverSocket.getOutputStream();
			String message = "LOGIN\n" + username + "\n" + password + "\n";
			output.write(message.getBytes());
			return true;
		}
		catch (IOException e)
		{
			Client.serverSocket = null;
			return false;
		}
		catch (NullPointerException e)
		{
			return false;
		}
	}
	
	public static boolean sendRegister(String username, String password)
	{
		try
		{
			if (Client.serverSocket == null) Client.serverSocket = createServerSocket(Client.SERVERADDR,Client.SERVERPORT);
			OutputStream output = Client.serverSocket.getOutputStream();
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
		catch (IOException e)
		{
			Client.serverSocket = null;
			return false;
		}
		catch (NullPointerException e)
		{
			return false;
		}
	}
	
	public static boolean sendShowFriend()
	{
		try
		{
			if (Client.serverSocket == null) Client.serverSocket = createServerSocket(Client.SERVERADDR,Client.SERVERPORT);
			OutputStream output = Client.serverSocket.getOutputStream();
			String message = "SHOW FRIEND\n";
			output.write(message.getBytes());
			return true;
		}
		catch (IOException e)
		{
			Client.serverSocket = null;
			return false;
		}
		catch (NullPointerException e)
		{
			return false;
		}
	}
	public static boolean sendAddFriend(String username)
	{
		try
		{
			if (Client.serverSocket == null) Client.serverSocket = createServerSocket(Client.SERVERADDR,Client.SERVERPORT);
			OutputStream output = Client.serverSocket.getOutputStream();
			String message = "ADD FRIEND\n" +username+ "\n";
			output.write(message.getBytes());
			return true;
		}
		catch (IOException e)
		{
			Client.serverSocket = null;
			return false;
		}
		catch (NullPointerException e)
		{
			return false;
		}
	}
	public static boolean sendAddFriendAccepted()
	{
		
		try
		{
			if (Client.serverSocket == null) Client.serverSocket = createServerSocket(Client.SERVERADDR,Client.SERVERPORT);
			OutputStream output = Client.serverSocket.getOutputStream();
			String message = "FRIEND REQUEST ACCEPTED\n";
			output.write(message.getBytes());
			return true;
		}
		catch (IOException e)
		{
			Client.serverSocket = null;
			return false;
		}
		catch (NullPointerException e)
		{
			return false;
		}
	}
	public static boolean sendAddFriendRejected()
	{
		try
		{
			if (Client.serverSocket == null) Client.serverSocket = createServerSocket(Client.SERVERADDR,Client.SERVERPORT);
			OutputStream output = Client.serverSocket.getOutputStream();
			String message = "FRIEND REQUEST REJECTED\n";
			output.write(message.getBytes());
			return true;
		}
		catch (IOException e)
		{
			Client.serverSocket = null;
			return false;
		}
		catch (NullPointerException e)
		{
			return false;
		}
	}
	
	public static boolean sendChatRequest(String username)
	{
		try
		{
			if (Client.serverSocket == null) Client.serverSocket = createServerSocket(Client.SERVERADDR,Client.SERVERPORT);
			OutputStream output = Client.serverSocket.getOutputStream();
			synchronized(Client.listBind)
			{
				Client.listBind.add(createBindSocket());
			}
			String message = "CHAT\n" + username+"\n" + Client.listBind.peek().getLocalPort() + "\n";
			output.write(message.getBytes());
			return true;
		}
		catch (IOException e)
		{
			Client.serverSocket = null;
			return false;
		}
		catch (NullPointerException e)
		{
			return false;
		}
	}
	
	public static boolean sendChatRequestAccepted()
	{
		try
		{
			if (Client.serverSocket == null) Client.serverSocket = createServerSocket(Client.SERVERADDR,Client.SERVERPORT);
			OutputStream output = Client.serverSocket.getOutputStream();
			String message = "CHAT REQUEST ACCEPTED\n";
			output.write(message.getBytes());
			return true;
		}
		catch (IOException e)
		{
			Client.serverSocket = null;
			return false;
		}
		catch (NullPointerException e)
		{
			return false;
		}
	}
	
	public static boolean sendChatRequestRejected()
	{
		try
		{
			if (Client.serverSocket == null) Client.serverSocket = createServerSocket(Client.SERVERADDR,Client.SERVERPORT);
			OutputStream output = Client.serverSocket.getOutputStream();
			String message = "CHAT REQUEST REJECTED\n";
			output.write(message.getBytes());
			return true;
		}
		catch (IOException e)
		{
			Client.serverSocket = null;
			return false;
		}
		catch (NullPointerException e)
		{
			return false;
		}
	}
	
	public static String readUntil(char delimiter, String result, boolean notBlock)
	{
		InputStream input;
		try {
			if (Client.serverSocket == null) Client.serverSocket = createServerSocket(Client.SERVERADDR,Client.SERVERPORT);
			input = Client.serverSocket.getInputStream();
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
					return readUntil(delimiter,result,notBlock);
				else
					return result;
			}
			else
				return readUntil(delimiter,result,notBlock);
		}
		catch (NullPointerException e)
		{
			return "";
		}
		catch (IOException e)
		{
			return "";
		}
	}
	
	public static String readUntil(Peer peer, char delimiter, String result, boolean notBlock)
	{
		InputStream input;
		try {
			if (peer.peerSocket == null) return "";
			input = peer.peerSocket.getInputStream();
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
					return readUntil(delimiter,result,notBlock);
				else
					return result;
			}
			else
				return readUntil(delimiter,result,notBlock);
		}
		catch (NullPointerException e)
		{
			return "";
		}
		catch (IOException e)
		{
			return "";
		}
	}
	
	public static boolean sendMsgToPeer(String msg)
	{
		try
		{
			OutputStream output = Client.currentPeer.peerSocket.getOutputStream();
			String message = "MESSAGE\n" + msg.length() + "\n" + msg;
			output.write(message.getBytes());
			Client.currentPeer.listMsg.add(new Message(msg,false));
			Client.ChatWindowUI.updateListMsg(Client.currentPeer);
			return true;
		}
		catch (IOException e)
		{
			Client.currentPeer.peerSocket = null;
			return false;
		}
		catch (NullPointerException e)
		{
			return false;
		}
	}
	
	public static boolean sendFileToPeer(byte[] data, String fileName)
	{
		try
		{
			OutputStream output = Client.currentPeer.peerSocket.getOutputStream();
			String message = "FILE\n" + fileName +"\n" + data.length + "\n" + new String(data);
			output.write(message.getBytes());
			Client.currentPeer.listMsg.add(new Message("[File] " + fileName,false));
			Client.ChatWindowUI.updateListMsg(Client.currentPeer);
			return true;
		}
		catch (IOException e)
		{
			Client.currentPeer.peerSocket = null;
			return false;
		}
		catch (NullPointerException e)
		{
			return false;
		}
	}
	
	public static void recvPeerMsg(Peer peer)
	{
		String header = Service.readUntil(peer,'\n',new String(""),true);
		switch (header)
		{
		case "":
			break;
		case "MESSAGE":
			//System.out.println("Messsssssssage");
			int len = Integer.parseInt(Service.readUntil(peer,'\n',new String(""),false));
			String msg = Service.readUntil(peer,'\n',new String(""),false);
			peer.listMsg.add(new Message(msg,true));
			Client.ChatWindowUI.updateListMsg(peer);
			// move peer to first of friend list
			Client.peerList.remove(peer);
			Client.peerList.add(0,peer);
			break;
		case "FILE":
			String fileName = Service.readUntil(peer,'\n',new String(""),false);
			len = Integer.parseInt(Service.readUntil(peer,'\n',new String(""),false));
			int dialogButton = JOptionPane.YES_NO_OPTION;
			int dialogResult = JOptionPane.showConfirmDialog(Client.ChatWindowUI, "Do you know want to download " + fileName, "Download", dialogButton);
			if (dialogResult == 0)
			{
				peer.listMsg.add(new Message("[File] " + fileName,true));
				Client.ChatWindowUI.updateListMsg(peer);
				try 
				{
					InputStream input = peer.peerSocket.getInputStream();
					peer.peerSocket.setSoTimeout(1000000);
					byte[] data = new byte[len];
					input.read(data,0,len);
					FileOutputStream output = new FileOutputStream(new File("").getAbsolutePath() + Client.downloadDir + fileName);
					output.write(data);
					output.close();
				}
				catch (IOException e)
				{
					System.out.println(e);
					try
					{
						peer.peerSocket.setSoTimeout(1000);
					}
					catch (SocketException ex) {}
					JOptionPane.showMessageDialog(Client.ChatWindowUI,
						    "Cannot download file",
						    "Error",
						    JOptionPane.WARNING_MESSAGE);
				}
			}
			break;
		}
	}
}
