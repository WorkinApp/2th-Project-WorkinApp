package com.workin.chat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.workin.main.AppMain;
import com.workin.util.ImageBox;



public class ChatClient extends JFrame {
	// 상단부분
	JPanel p_north;
	JPanel p_back;

	JLabel la_center;

	JPanel p_exit;
	JLabel la_exit;
	JLabel la_back;

	// 센터부분
	JTextArea area;
	JScrollPane scroll;

	// 하단부분
	JPanel p_south;
	JPanel p_file;
	JTextField t_input;
	JButton bt_send;
	
	ImageBox imageBox;
	ImageBox imagebox1;
	ImageBox imagebox2;
	ImageBox imagebox3;

	Socket socket;
	ClientMsgThread msgThread;
	
	AppMain appMain;
	
	Image img;
	
	boolean serverFlag = true;

	public ChatClient(AppMain appMain) {
		this.appMain=appMain;

		// 생성

		// 상단부분
		p_north = new JPanel();
		p_back = new JPanel();
		imagebox1 = new ImageBox(Color.white, 30,20, new ImageIcon(getIcon("back.png")).getImage());
		
		la_center = new JLabel(appMain.getMember().getUser_name());
		la_center.setHorizontalAlignment(JLabel.CENTER);
//		la_exit = new JLabel("X");
		p_exit = new JPanel();
		imagebox2 = new ImageBox(Color.white, 30,20, new ImageIcon(getIcon("exit.png")).getImage());
//		la_back = new JLabel("<");

		// 센터부분
		area = new JTextArea();
		scroll = new JScrollPane(area);

		// 하단부분
		Color b = new Color(44, 62, 80);
		p_south = new JPanel();
		p_file = new JPanel();
		imagebox3 = new ImageBox(Color.white, 30,20, new ImageIcon(getIcon("file.png")).getImage());
		t_input = new JTextField(15);
		bt_send = new JButton("전송");

		// 레이아웃,스타일
		p_north.setBackground(Color.white);
		p_south.setBackground(b);
		t_input.setPreferredSize(new Dimension(30, 27));
//		p_back.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		p_back.setBackground(Color.white);
		p_exit.setBackground(Color.white);
		p_file.setBackground(b);

		// 조립
		p_back.add(imagebox1);
		p_exit.add(imagebox2);
		p_north.setLayout(new BorderLayout());
		p_north.add(p_back, BorderLayout.WEST);
		p_north.add(la_center);
		p_north.add(p_exit, BorderLayout.EAST);
		add(p_north, BorderLayout.NORTH);
		add(scroll);
		p_file.add(imagebox3);
		p_south.add(p_file);
		p_south.add(t_input);
		p_south.add(bt_send);
		add(p_south, BorderLayout.SOUTH);

		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				msgThread.flag = false;
//				System.exit(0);
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

		// 전송 버튼 누르면..
		bt_send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendMsg();
			}
		});

		// 보이기
//		setUndecorated(true);
		setResizable(false);
		setVisible(true);
		setBounds(800, 300, 330, 500);
//		setDefaultCloseOperation(EXIT_ON_CLOSE);
		

		connect();
		sendAllData();

	}
	

	
	public Image getIcon(String filename) {
		URL url = this.getClass().getClassLoader().getResource(filename);
		ImageIcon icon=new ImageIcon(url);
		return icon.getImage();
	}

	public void sendMsg() {
		String msg = t_input.getText();
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\"cmd\":\"chat\", ");
		sb.append("\"message\": \"" + msg + "\"");
		sb.append("}");

		msgThread.send(sb.toString());
		t_input.setText("");
	}

	public void sendAllData() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\"cmd\" : \"login\","); // cmd 요청명령을 구분하는 값!!
		sb.append("\"member\":{");
		sb.append("\"user_name\" :\"" + appMain.getMember().getUser_name() + "\",");
		sb.append("\"user_id\" :  \"" + appMain.getMember().getUser_id() + "\",");
		sb.append("\"regdate\" :\"" + appMain.getMember().getRegdate() + "\"");
		sb.append("\"img\" :\"" + appMain.getMember().getImg() + "\"");
		sb.append("}");
		sb.append("}");

		// 서버에 메시지 전송
		System.out.println(sb);
		msgThread.send(sb.toString());
	}

	public void connect() {
		String ip = "192.168.0.3";
		int port = 7777;

		try {
			socket = new Socket(ip, port); // 접속!!!!!!!
			// 클라이언트 측의 대화용 쓰레드 생성
			msgThread = new ClientMsgThread(socket, this);
			msgThread.start(); // 서버의 메시지 실시간 청취 시작
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}