package com.workin.chat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.workin.chat.Member;

public class ChatClient extends JFrame {
	// 상단부분
	JPanel p_north;
	JButton bt_back;
	JLabel la_center;
	JButton bt_exit;
	JLabel la_exit;
	JLabel la_back;

	// 센터부분
	JTextArea area;
	JScrollPane scroll;

	// 하단부분
	JPanel p_south;
	JButton bt_file;
	JTextField t_input;
	JButton bt_send;

	Socket socket;
	ClientMsgThread msgThread;
	Member member;
	
	boolean serverFlag = true;

	public ChatClient() {

		// 생성

		// 상단부분
		p_north = new JPanel();
		bt_back = new JButton("<");
		la_center = new JLabel("홍길동(추후변경)");
		la_center.setHorizontalAlignment(JLabel.CENTER);
		la_exit = new JLabel("X");
		bt_exit = new JButton("X");
		la_back = new JLabel("<");

		// 센터부분
		area = new JTextArea();
		scroll = new JScrollPane(area);

		// 하단부분
		Color b = new Color(44, 62, 80);
		p_south = new JPanel();
		bt_file = new JButton("+");
		t_input = new JTextField(15);
		bt_send = new JButton("전송");

		// 레이아웃,스타일

		p_south.setBackground(b);
		t_input.setPreferredSize(new Dimension(30, 27));

		// 조립
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

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				msgThread.flag = false; 
				System.exit(0);
			}
		});

		// 메시지 입력 엔터처리
		t_input.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) { 
					sendMsg();
				}
			}
		});
		
		//전송 버튼 누르면..
				bt_send.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						sendMsg();
					}
				});

		// 보이기
//		setUndecorated(true);
		setVisible(true);
		setBounds(800, 300, 330, 500);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		connect();
	}

	public void sendMsg() {
		String msg =t_input.getText();
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\"cmd\":\"chat\", ");
		sb.append("\"message\": \""+msg+"\"");
		sb.append("}");
		
		msgThread.send(sb.toString()); 
		t_input.setText(""); 
	}
	

	public void sendAllData() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\"cmd\" : \"login\",");  //cmd 요청명령을 구분하는 값!!
		sb.append("\"member\":{");
		sb.append("\"user_id\" :  \""+member.getUser_id()+"\",");
		sb.append("\"name\" :\""+member.getName()+"\",");
		sb.append("\"regdate\" :\""+member.getRegdate()+"\"");
		sb.append("}");
		sb.append("}");
		
		//서버에 메시지 전송 
		msgThread.send(sb.toString());
	}

	
	public void connect() {
		String ip="192.168.0.3";
		int port=7777;
		
		try {
			socket = new Socket(ip, port); //접속!!!!!!!
			//클라이언트 측의 대화용 쓰레드 생성 
			msgThread = new ClientMsgThread(socket, this);
			msgThread.start(); //서버의 메시지 실시간 청취 시작
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}