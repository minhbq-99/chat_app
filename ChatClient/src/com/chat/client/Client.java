
package com.chat.client;
import java.net.*;
import java.io.*;
import java.util.*;

import javax.swing.JOptionPane;

import com.chat.ui.*;


public class Client
{
	public static final String SERVERADDR = "localhost";
	public static final int SERVERPORT = 2222;
	public static Socket serverSocket;
	public static int localPort;
	public static List<Peer> peerList = new LinkedList<>();
	public static Peer currentPeer;
	public static String name;
	public static ChatWindow ChatWindowUI;
	private static int count = 0;
	public static Queue<ServerSocket> listBind = new LinkedList<>();
	public static final String downloadDir = "/download/";
	
	public static void main(String[] argv)
	{
		boolean isLogin = false;
		serverSocket = Service.createServerSocket(Client.SERVERADDR,Client.SERVERPORT);
		Login loginUI = new Login();
		loginUI.setVisible(true);
		ChatWindowUI = null;
		while (true)
		{
			String header = Service.readUntil('\n',new String(""),true);
			switch (header)
			{
			case "":
				break;
			case "LOGIN SUCESSFUL":
			case "REGISTER SUCESSFUL":
				name = Login.name;
				loginUI.setVisible(false);
				loginUI = null;
				ChatWindowUI = new ChatWindow();
				ChatWindowUI.setVisible(true);
				isLogin = true;
				break;
			case "LOGIN FAILED":
				JOptionPane.showMessageDialog(loginUI,
					    "Login Failed",
					    "Message",
					    JOptionPane.PLAIN_MESSAGE);
				break;
			case "REGISTER FAILED":
				JOptionPane.showMessageDialog(loginUI,
					    "Register Failed",
					    "Message",
					    JOptionPane.PLAIN_MESSAGE);
				break;
			case "FRIEND STATUS":
				int numOfFriends = 0;
				boolean isBreak = false;
				try
				{
					numOfFriends = Integer.parseInt(Service.readUntil('\n',new String(""),false));
					for (int i = 0; i < numOfFriends; i++)
					{
						isBreak = false;
						String[] friends = Service.readUntil('\n',new String(""),false).split(": ");
						for (Peer peer: peerList)
						{
							//update peer status
							if (peer.name.equals(friends[0]))
							{
								peer.isOnline = friends[1].equals("online");
								isBreak = true;
								break;
							}
						}
						
						//add new peer
						if (!isBreak)
						{
							peerList.add(new Peer(friends[0],friends[1].equals("online")));
						}
					}
					ChatWindowUI.updateListUser(peerList);
				}
				catch (NumberFormatException e)
				{
					System.out.println("[CRITICAL] Malicious server response " + e);
				}
				break;
			case "FRIEND REQUEST":
				String friendName = Service.readUntil('\n',new String(""),false);
				int dialogButton = JOptionPane.YES_NO_OPTION;
				int dialogResult = JOptionPane.showConfirmDialog(ChatWindowUI, "Do you know " + friendName + "?", "Friend Request", dialogButton);
				if (dialogResult == 0)
					Service.sendAddFriendAccepted();
				else
					Service.sendAddFriendRejected();
				break;
			case "CHAT NOT AVAILABLE":
				JOptionPane.showMessageDialog(ChatWindowUI,
					    "User not available",
					    "Error",
					    JOptionPane.WARNING_MESSAGE);
				break;
			case "CHAT REQUEST":
				friendName = Service.readUntil('\n',new String(""),false);
				dialogButton = JOptionPane.YES_NO_OPTION;
				dialogResult = JOptionPane.showConfirmDialog(ChatWindowUI, "Do you want to chat with " + friendName + "?", "Friend Request", dialogButton);
				if (dialogResult == 0)
				{
					Service.sendChatRequestAccepted();
					for (Peer peer: peerList)
					{
						if (peer.name.equals(friendName))
						{
							Client.currentPeer = peer;
							break;
						}
					}
				}
				else
					Service.sendChatRequestRejected();
				break;
			case "CHAT REJECTED":
				ServerSocket socket = Client.listBind.remove();
				try
				{
					socket.close();
				}
				catch (IOException e) {}
				JOptionPane.showMessageDialog(ChatWindowUI,
					    "Chat request's rejected",
					    "Error",
					    JOptionPane.WARNING_MESSAGE);
				break;
			case "CHAT INFO":
				String[] response = Service.readUntil('\n',new String(""),false).split(":");
				int port = 0;
				int isServer = 0;
				try
				{
					port = Integer.parseInt(response[1]);
					isServer = Integer.parseInt(response[2]);
					if (isServer == 1)
					{
						Socket peer;
						ServerSocket server = Client.listBind.remove();
						do
						{
							//System.out.println("Server: " + server.getLocalPort());
							peer = server.accept();
						} 
						while (!peer.getInetAddress().toString().split("/")[1].equals(response[0]));
						Client.currentPeer.peerSocket = peer;
					}
					else
					{
						int count = 0;
						Client.currentPeer.peerSocket = null;
						while (Client.currentPeer.peerSocket == null)
						{
							//System.out.println(port);
							Client.currentPeer.peerSocket = Service.createServerSocket(response[0],port);
							if (count == 4) break;
							count++;
						}
					}
				}
				catch (NumberFormatException e)
				{
					System.out.println("[CRITICAL] Malicious server response " + e);
				}	
				catch (IOException e)
				{
					Client.currentPeer.peerSocket = null;
					JOptionPane.showMessageDialog(ChatWindowUI,
						    "Cannot connect to peer",
						    "Error",
						    JOptionPane.WARNING_MESSAGE);
				}
				break;
			default:
				System.out.println("[CRITICAL] Malicious server message " + header);
			}
			
			//Update new peer message
			List<Peer> tmpList = new LinkedList<>(peerList);
			for (Peer peer: tmpList)
			{
				if (peer.peerSocket != null)
					Service.recvPeerMsg(peer);
			}
			count++;
			if (count > 2)
			{
				//Update friend status
				if (isLogin) Service.sendShowFriend();
				count = 0;
			}
		}
	}
}