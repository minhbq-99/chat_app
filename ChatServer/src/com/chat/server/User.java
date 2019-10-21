package com.chat.server;

import java.util.ArrayList;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.*;
import java.security.spec.*;

public class User {
	private String username;
	byte[] passwordHash;
	private ArrayList<String> friendList;
	private boolean isOnline;
	
	private byte[] hashPassword(String password)
	{
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[16];
		random.nextBytes(salt);
		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
		try
		{
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			return factory.generateSecret(spec).getEncoded();
		}
		catch (NoSuchAlgorithmException e) {return null;}
		catch (InvalidKeySpecException e) {return null;}
	}
	
	public User(String username, String password)
	{
		this.username = username;
		this.passwordHash = hashPassword(password);
		this.friendList = new ArrayList<String>();
		this.isOnline = true;	 // a newly created user is online immediately
	}
	
	public boolean isEqual(String username, String password)
	{
		if (this.username == username && this.passwordHash == hashPassword(password))
			return true;
		return false;
	}
	
	public void addFriend(String friendName)
	{
		this.friendList.add(friendName);
	}
	
	public boolean getIsOnline() { return this.isOnline; }
	public void setIsOnline(boolean online) { this.isOnline = online; }
}
