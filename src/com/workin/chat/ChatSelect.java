package com.workin.chat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;

import com.workin.main.AppMain;

public class ChatSelect extends JFrame{
	BevelBorder border;
	JPanel p_north;
	JLabel la_north;
	
	JPanel p_center;
	JLabel la_online;
	JScrollPane scroll;
	
	JPanel p_list;
	
	
	public ChatSelect(AppMain appMain,Vector<ServerMsgThread> clientList) {
		//생성
		p_north = new JPanel();
		la_north = new JLabel("오픈채팅방");
		
		p_center = new JPanel();
		la_online= new  JLabel("오픈채팅방 참여인원 "+clientList.size()+"명");
		
		p_list = new JPanel();
		scroll = new JScrollPane(p_list);
		
		//디자인,레이아웃

		la_north.setPreferredSize(new Dimension(80, 35));
		la_north.setHorizontalAlignment(JLabel.CENTER);
//		p_la.setBackground(Color.yellow);
		border=new BevelBorder(BevelBorder.RAISED);//3차원적인 테두리 효과를 위한것이고 양각의 옵션을 준다.
		la_north.setBorder(border);//라벨에 적용시킨다.


		p_north.setPreferredSize(new Dimension(300, 40));
//		p_north.setLayout(new BorderLayout());
//		p_north.setBackground(Color.LIGHT_GRAY);
		p_center.setPreferredSize(new Dimension(300, 30));
//		p_center.setBackground(Color.CYAN);
//		la_online.setHorizontalAlignment(JLabel.WEST);
		p_list.setBackground(Color.BLUE);
		
		scroll.setPreferredSize(new Dimension(300, 370));
		
		
		
		
		//조립
		setLayout(new FlowLayout());
		p_north.add(la_north);
		add(p_north);
		
		p_center.add(la_online);
		add(p_center);
		
		add(scroll);
		
		
		
		//이벤트
		la_north.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				System.out.println("단체방에 입장합니다");
				new ChatClient(appMain);
			}
		});
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
//				System.exit(0);
			}
		});
		
		
		
		//보이기
		setResizable(false);
		setVisible(true);
		setBounds(1270, 320, 330, 500);
		
		
		
	}
	

}
