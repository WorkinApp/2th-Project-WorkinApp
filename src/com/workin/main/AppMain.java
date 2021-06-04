package com.workin.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.workin.chat.ChatClient;
import com.workin.chat.Member;
import com.workin.chat.ServerMsgThread;


public class AppMain extends JFrame{
	AppMain appMain;
	
	
	//서쪽 영역
	JPanel p_west;
	JPanel p_profile; //프로필 사진
	JLabel la_name; //이름
	JLabel la_dept;	//부서명
	String[] menu_title= {"홈","타임라인","나의일정","클라우드","설정"};
	CustomButton[] bt_menu=new CustomButton[menu_title.length]; //배열생성

	//센터 영역
	JPanel p_center;
	JPanel p_north; //북쪽 메뉴바
	JLabel la_project_name;
	JButton bt_chat;
	JButton bt_config;
	private Member member;
	
	//서버관련
	ServerSocket server;
	Thread serverThread;
	Thread chatThread;
	private Vector<ServerMsgThread> clientList = new Vector<ServerMsgThread>();
	boolean serverFlag = true;
	
	public AppMain(Member member) {
		this.member=member;
		
		Color c = new Color(44, 62, 80);
		//생성
		p_west = new JPanel();
		p_profile = new JPanel();
		la_name = new JLabel("홍길동");
		la_dept = new JLabel("마케팅부");
		
		for(int i=0;i< menu_title.length;i++) {
			bt_menu[i] = new CustomButton(menu_title[i]);
			bt_menu[i].setId(i); //반복문의 i 를 각 버튼의 식별  id 로 할당!!!
			bt_menu[i].setPreferredSize(new Dimension(180,50));
			bt_menu[i].setBorderPainted(false);
			bt_menu[i].setBackground(c);
			bt_menu[i].setForeground(Color.WHITE);
			bt_menu[i].setFont(new Font("맑은 고딕", Font.BOLD, 18));
			bt_menu[i].setHorizontalAlignment(SwingConstants.LEFT);
		}
		
		p_center = new JPanel();
		p_north = new JPanel();
		la_project_name = new JLabel("Work IN");
		bt_chat = new JButton("채팅");
		bt_config = new JButton("설정");
		
		//스타일 및 레이아웃
		
		//서쪽 디자인
		p_west.setPreferredSize(new Dimension(180, 720));
		p_west.setBackground(c);
		p_profile.setPreferredSize(new Dimension(180,60));
		
		//센터 디자인
		p_center.setPreferredSize(new Dimension(1020, 680));
		p_center.setBackground(Color.WHITE);
		p_north.setPreferredSize(new Dimension(1020, 40));
		p_north.setBackground(Color.WHITE);
		p_north.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY)); //아래쪽 테두리
		
		//조립
		p_profile.add(la_name);
		p_profile.add(la_dept);
		p_west.add(p_profile);		
		for(JButton bt : bt_menu) { // improved for loop : 주로 집합데이터 형식을 대상으로 한 loop
			p_west.add(bt);
		}		
		add(p_west, BorderLayout.WEST);
		
		p_north.add(la_project_name);
		p_north.add(bt_chat);
		p_north.add(bt_config);
		p_center.add(p_north, BorderLayout.NORTH);
		add(p_center);
		
		//이벤트
		
		//서버 가동
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
		
		
		
		
		
		
		bt_chat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chatThread = new Thread() {
					public void run() {
						runChat();
					}
				};
				chatThread.start();
			}
		});
				
		
		//보여주기
		setBounds(400, 100, 1200, 720);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public void runServer() {
		int port = 7777;
		try {
			server = new ServerSocket(port);
			this.setTitle("서버준비 완료");
			while (serverFlag) {
				Socket socket = server.accept();
				System.out.println("누군가 접속시도");
				ServerMsgThread smt = new ServerMsgThread( socket , this);
				smt.start(); 
				getClientList().add(smt); 
				System.out.println("접속자 수는= "+getClientList().size());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void runChat() {
		new ChatClient(AppMain.this);
	}
	
	public Member getMember() {
		return member;
	}

	public Vector<ServerMsgThread> getClientList() {
		return clientList;
	}

	public void setClientList(Vector<ServerMsgThread> clientList) {
		this.clientList = clientList;
	}
}