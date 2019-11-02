package com.chat.ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.DefaultListModel;

import java.util.*;
import com.chat.client.*;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;

import javax.swing.JTextArea;

public class ChatWindow extends JFrame {

	private JPanel contentPane;
	private JTextField textFriendName;
	private JTextArea textMsg;
	public JPanel panelUser;
	private static ChatWindow frame;
	public JList<Peer> listUser;
	public JList<String> listMsg;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new ChatWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void updateListUser(List<Peer> listPeer)
	{
		DefaultListModel<Peer> list = new DefaultListModel<>();
		for (Peer peer: listPeer)
			list.addElement(peer);
		synchronized (listUser)
		{
			listUser.setModel(list);
		}
	}
	
	public void updateListMsg(Peer peer)
	{
		DefaultListModel<String> list = new DefaultListModel<>();
		for(Message msg: peer.listMsg)
		{
			if(msg.isPeer)
				list.addElement(peer.name + ": " + msg.msg);
			else
				list.addElement(Client.name + ": " + msg.msg);
		}
		synchronized (listMsg)
		{
			listMsg.setModel(list);
		}
	}
	
	/**
	 * Create the frame.
	 */
	public ChatWindow() {
		setTitle("Chat");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 681, 431);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textFriendName = new JTextField();
		textFriendName.setBounds(0, 330, 168, 29);
		contentPane.add(textFriendName);
		textFriendName.setColumns(20);
		
		JButton btnAddFriend = new JButton("Add Friend");
		btnAddFriend.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				boolean result = Service.sendAddFriend(textFriendName.getText());
				textFriendName.setText("");
				if (!result) 
				{
					JOptionPane.showMessageDialog(frame,
						    "Connection Error",
						    "Error",
						    JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		btnAddFriend.setBounds(0, 363, 168, 29);
		contentPane.add(btnAddFriend);
		
		textMsg = new JTextArea();		
		textMsg.setLineWrap(true);
		textMsg.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				onChange();
			}
			public void removeUpdate(DocumentEvent e) {
				onChange();
			}
			public void insertUpdate(DocumentEvent e) {
				onChange();
			}

			public void onChange() {
				if (Client.currentPeer == null) return;
				if (Client.currentPeer.peerSocket == null) return;
				String msg = textMsg.getText();
				//System.out.println(msg);
				if (msg.length() != 0 && msg.charAt(msg.length()-1) == '\n')
				{
					boolean result = Service.sendMsgToPeer(msg);
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run()
						{
							textMsg.setText("");
						}
					});
					if (!result)
						JOptionPane.showMessageDialog(frame,
							    "Try to connect to peer again",
							    "Error",
							    JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		
		textMsg.setBounds(177, 330, 396, 51);
		contentPane.add(textMsg);
		textMsg.setColumns(40);
		
		JButton btnFile = new JButton("File");
		btnFile.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser chooser = new JFileChooser();
				int r = chooser.showSaveDialog(null);
				if (r == JFileChooser.APPROVE_OPTION)
				{
					String fileName = chooser.getSelectedFile().getName();
					File fileIn = new File(chooser.getSelectedFile().getAbsolutePath());
					try
					{
						FileInputStream input = new FileInputStream(fileIn);
						byte[] data = new byte[(int) fileIn.length()];
						input.read(data, 0, (int) fileIn.length());
						input.close();
						boolean result = Service.sendFileToPeer(data, fileName);
						if (!result)
							JOptionPane.showMessageDialog(frame,
								    "Try to connect to peer again",
								    "Error",
								    JOptionPane.WARNING_MESSAGE);
					}
					catch (FileNotFoundException ex) {}
					catch (IOException ex) {}
				}
			}
		});
		btnFile.setBounds(583, 333, 82, 23);
		contentPane.add(btnFile);
		
		JPanel userPanel = new JPanel();
		userPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		userPanel.setBounds(0, 0, 168, 328);
		contentPane.add(userPanel);
		userPanel.setLayout(null);
		
		listUser = new JList<>();
		listUser.setBorder(new LineBorder(new Color(0, 0, 0)));
		listUser.setBackground(null);
		listUser.setBounds(0, 0, 168, 328);
		userPanel.add(listUser);
		
		listUser.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent evt) {
				if (!evt.getValueIsAdjusting()) {
					Peer peer = listUser.getSelectedValue();
					if (peer != null)
					{
						Client.currentPeer = peer;
						updateListMsg(Client.currentPeer);
					}
				}
			}
		});
		
		JPanel msgPanel = new JPanel();
		msgPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		msgPanel.setBounds(177, 0, 488, 328);
		contentPane.add(msgPanel);
		msgPanel.setLayout(null);
		
		listMsg = new JList<>();
		listMsg.setBorder(new LineBorder(new Color(0, 0, 0)));
		listMsg.setBackground(null);
		listMsg.setBounds(0, 0, 488, 328);
		msgPanel.add(listMsg);
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (Client.currentPeer == null) return;
				if (Client.currentPeer.peerSocket != null) return;
				Service.sendChatRequest(Client.currentPeer.name);
			}
		});
		btnConnect.setBounds(583, 358, 82, 23);
		contentPane.add(btnConnect);
	}
}
