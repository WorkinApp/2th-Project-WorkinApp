package com.workin.chat;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class ChatServer extends JFrame {
	JLabel running;
	ServerSocket server;
	JButton bt;
	Thread serverThread;
	Vector<ServerMsgThread> clientList = new Vector<ServerMsgThread>();
	boolean serverFlag = true;
	
	public ChatServer() {
		// 생성
		running = new JLabel("서버가동 중...", SwingConstants.CENTER);

		// 붙이기


		// 이벤트
		serverThread = new Thread() {
			public void run() {
				runServer();
			}
		};
		serverThread.start();


		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				serverFlag = false; // 쓰레드 소멸
				System.exit(0); // Process kill
			}
		});

		// view
		setVisible(true);
		setBounds(600, 300, 200, 100);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public void runServer() {
		int port = 7777;
		try {
			server = new ServerSocket(port);
			add(running);
			while (serverFlag) {
				Socket socket = server.accept();
				System.out.println("접속성공");
				ServerMsgThread smt = new ServerMsgThread( socket , this);
				smt.start(); 
				clientList.add(smt); 
				System.out.println(clientList.size());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		new ChatServer();
	}
}
