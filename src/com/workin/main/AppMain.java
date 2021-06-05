package com.workin.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


import com.workin.calendar.CalendarMain;
import com.workin.chat.ChatClient;
import com.workin.chat.ChatSelect;
import com.workin.chat.Member;
import com.workin.chat.ServerMsgThread;
import com.workin.cloud.CloudMain;
import com.workin.list.ListMain;



public class AppMain extends JFrame implements ActionListener{
	AppMain appMain;
	
	//서쪽 영역
	JPanel p_west;
	JPanel p_profile;
	JPanel p_img;
	JLabel la_name;
	JLabel la_dept;
	
	String[] menu_title= {"홈","타임라인","나의일정","클라우드","설정"};
	CustomButton[] bt_menu=new CustomButton[menu_title.length]; //배열생성

	//센터 영역
	JPanel p_center;
	JPanel p_north; //북쪽 메뉴바
	JLabel la_title;
	JButton bt_chat;
	JButton bt_config;
	
	//페이지 선언
	Page[] pages = new Page[4];
	Color c = new Color(34, 45, 50);
	
	//서버관련
	private Member member;
	ServerSocket server;
	Thread serverThread;
	Thread chatThread;
	private Vector<ServerMsgThread> clientList = new Vector<ServerMsgThread>();
	boolean serverFlag = true;

	public AppMain(Member member) {
		System.out.println(member);
		this.member=member;
		//생성
		p_west = new JPanel();
		p_profile = new JPanel();
		p_img = new JPanel();
		la_name = new JLabel(getMember().getUser_name(),SwingConstants.CENTER);
		la_dept = new JLabel("A사업팀",SwingConstants.CENTER);
		
		for(int i=0;i< menu_title.length;i++) {
			bt_menu[i] = new CustomButton(menu_title[i]);
			bt_menu[i].setId(i); //반복문의 i 를 각 버튼의 식별  id 로 할당!!!
			bt_menu[i].setPreferredSize(new Dimension(180,50));
			bt_menu[i].setBorderPainted(false);
			bt_menu[i].setFocusPainted(false);
			bt_menu[i].setBackground(c);
			bt_menu[i].setForeground(Color.WHITE);
			bt_menu[i].setFont(new Font("맑은 고딕", Font.BOLD, 16));
			bt_menu[i].setHorizontalAlignment(SwingConstants.LEFT);
			bt_menu[0].setBackground(Color.DARK_GRAY);
		}
		
		p_center = new JPanel();
		p_north = new JPanel();
		la_title = new JLabel("Workin");
		bt_chat = new JButton("채팅");
		bt_config = new JButton("설정");
		
		//페이지들 생성
		p_center = new JPanel();
		
		pages[0] = new HomeMain(this); //메인홈
		pages[1] = new ListMain(this);//게시판 리스트
		pages[2] = new CalendarMain(this);//나의일정
		pages[3] = new CloudMain(this);//클라우드
		//pages[4] = new JoinForm(this);//회원가입

		//스타일 및 레이아웃
		
		//서쪽 디자인
		p_west.setPreferredSize(new Dimension(180, 720));
		p_west.setBackground(c);
		p_profile.setPreferredSize(new Dimension(180, 120));
		p_profile.setBackground(c);
		p_img.setPreferredSize(new Dimension(60,60));
		p_img.setBackground(Color.WHITE);
		la_name.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		la_name.setForeground(Color.WHITE);
		la_name.setPreferredSize(new Dimension(180,20));
		la_dept.setFont(new Font("맑은 고딕", Font.BOLD, 14));
		la_dept.setForeground(Color.WHITE);
		
		//센터 디자인
		p_center.setPreferredSize(new Dimension(1020, 680));
		p_north.setPreferredSize(new Dimension(1020, 40));
		p_north.setBackground(Color.WHITE);
		p_north.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY)); //아래쪽 테두리
		la_title.setPreferredSize(new Dimension(840,30));
		la_title.setFont(new Font("맑은 고딕", Font.BOLD, 22));
		bt_chat.setBorderPainted(false);
		bt_chat.setFocusPainted(false);
		bt_chat.setBackground(Color.DARK_GRAY);
		bt_chat.setForeground(Color.WHITE);
		bt_chat.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		bt_config.setBorderPainted(false);
		bt_config.setFocusPainted(false);
		bt_config.setBackground(Color.DARK_GRAY);
		bt_config.setForeground(Color.WHITE);
		bt_config.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		
		//조립
		p_profile.add(p_img);
		p_profile.add(la_name);
		p_profile.add(la_dept);
		//p_west.add(p_title);	
		p_west.add(p_profile);	
		for(JButton bt : bt_menu) { // improved for loop : 주로 집합데이터 형식을 대상으로 한 loop
			p_west.add(bt);
		}		
		add(p_west, BorderLayout.WEST);
		
		p_north.add(la_title);
		p_north.add(bt_chat);
		p_north.add(bt_config);
		p_center.add(p_north, BorderLayout.NORTH);
		for(Page p : pages) {
			p_center.add(p);
		}
		add(p_center);
		
		//이벤트
		for(int i=0;i<bt_menu.length;i++) {
			bt_menu[i].addActionListener(this);
		}
		
		
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

	public void actionPerformed(ActionEvent e) {
		//어떤 버튼이 눌렸는지? - 이벤트가 연결된 컴포넌트를 가리켜 이벤트 소스 
		Object obj = e.getSource();
		//obj는 오브젝트 자료형이기 때문에, 버튼을 가리킬수는 있지만, 버튼 보다는 보편적인 기능만을 가지고 
		//있기에, 즉 가진게 별로 없기에 버튼의 특징을 이용하기 위해서는 버튼 형으로 변환해서 사용하자!!
		CustomButton bt=(CustomButton)obj; //down casting
		showHide(bt.getId());
	}
	
	public void runChat() {
		new ChatSelect(AppMain.this, clientList);
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
	
	public void showHide(int n) { //보여주고 싶은 페이지의 번호를 넘기면 된다..
		//내가 누른 버튼에 해당하는 페이지만 setVisible() 을 true로 놓고 나머지는 false로 놓자!!
		for(int i=0;i<pages.length;i++) {
			if(n==i) {
				pages[i].setVisible(true); //현재 선택한 버튼과 같은 인덱스를 갖는 페이지라면..
				bt_menu[i].setBackground(Color.DARK_GRAY);
			}else {
				pages[i].setVisible(false);
				bt_menu[i].setBackground(c);
			}
		}
	}
	
//	public static void main(String[] args) {
//		new AppMain();
//	}
}
