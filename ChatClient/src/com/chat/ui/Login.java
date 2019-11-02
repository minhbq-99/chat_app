package com.chat.ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;

import com.chat.client.*;

public class Login extends JFrame {

	private JPanel contentPane;
	private JTextField textUsername;
	private JTextField textPassword;
	private static Login frame;
	public static String name;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Login() {
		setTitle("Chat");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 618, 429);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(70, 130, 180));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textUsername = new JTextField();
		textUsername.setBounds(126, 138, 384, 42);
		contentPane.add(textUsername);
		textUsername.setColumns(20);
		
		textPassword = new JTextField();
		textPassword.setColumns(20);
		textPassword.setBounds(126, 208, 384, 42);
		contentPane.add(textPassword);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setBounds(101, 60, -1, 27);
		contentPane.add(lblNewLabel);
		
		JLabel lblLogin = new JLabel("LOGIN");
		lblLogin.setHorizontalAlignment(SwingConstants.CENTER);
		lblLogin.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblLogin.setBounds(126, 60, 384, 42);
		contentPane.add(lblLogin);
		
		JButton btnNewButton = new JButton("LOGIN");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				name = textUsername.getText();
				boolean result = Service.sendLogin(name, textPassword.getText());
				if (!result)
				{
					JOptionPane.showMessageDialog(frame,
						    "Connection Error",
						    "Error",
						    JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		btnNewButton.setBounds(126, 290, 97, 25);
		contentPane.add(btnNewButton);
		
		JButton btnRegister = new JButton("REGISTER");
		btnRegister.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				name = textUsername.getText();
				boolean result = Service.sendRegister(textUsername.getText(), textPassword.getText());
				if (!result)
				{
					JOptionPane.showMessageDialog(frame,
						    "Connection Error",
						    "Error",
						    JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		btnRegister.setBounds(413, 290, 97, 25);
		contentPane.add(btnRegister);
		
		JLabel lblNewLabel_1 = new JLabel("Username");
		lblNewLabel_1.setBounds(47, 139, 80, 40);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(47, 208, 80, 40);
		contentPane.add(lblPassword);
		
	}
}
