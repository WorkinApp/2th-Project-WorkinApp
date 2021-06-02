package com.workin.chat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatClient extends JFrame{
	//상단부분
	JPanel p_north;
	JButton bt_back;
	JLabel la_center;
	JButton bt_exit;
//	JLabel la_exit;
//	JLabel la_back;
	
	//센터부분
	JTextArea area;
	JScrollPane scroll;
	
	
	//하단부분
	JPanel p_south;
	JButton bt_file;
	JTextField t_input;
	JButton bt_send;
	
	
	
	
	public ChatClient() {
		
		//생성
		
		//상단부분
		p_north = new JPanel();
		bt_back = new JButton("<");
		la_center = new JLabel("홍길동(추후변경)");
		la_center.setHorizontalAlignment(JLabel.CENTER);
//		la_exit = new JLabel(null);
		bt_exit = new JButton("X");
//		la_back = new JLabel(null);
		
		//센터부분
		area = new JTextArea();
		scroll = new JScrollPane(area);
		
		//하단부분
		Color b = new Color(44,62,80);
		p_south = new JPanel();
		bt_file = new JButton("+");
		t_input = new JTextField(15);
		bt_send = new JButton("전송");
		
		//레이아웃,스타일

		p_south.setBackground(b);
		t_input.setPreferredSize(new Dimension(30, 27));
		
		
		//조립
		p_north.setLayout(new BorderLayout());
		p_north.add(bt_back, BorderLayout.WEST);
		p_north.add(la_center);
		p_north.add(bt_exit, BorderLayout.EAST);
		add(p_north, BorderLayout.NORTH);
		add(scroll);
		p_south.add(bt_file);
		p_south.add(t_input);
		p_south.add(bt_send);
		add(p_south, BorderLayout.SOUTH);
		
		
		//붙이기
		
		
		
		//보이기
//		setUndecorated(true);
		setVisible(true);
		setBounds(800,300,330,500);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	
	public static void main(String[] args) {
		new ChatClient();
	}
}
