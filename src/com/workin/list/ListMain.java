package com.workin.list;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.workin.main.AppMain;
import com.workin.main.Page;

public class ListMain extends Page{
	AppMain appMain;
	
	//센터관련
	JPanel p_center; // 검색창 게시판 영역
	JPanel p_search;
	Choice ch_category;//검색 카테고리 선택
	JTextField t_keyword;//검색어 입력
	JButton bt_search; 
	JPanel p_list;
	
	//동쪽관련
	JPanel p_east; //공지사항 영역
	JLabel la_notice;
	JPanel p_notice;
	public ListMain(AppMain appMain) {
		super(appMain);
		
		//생성
		p_center = new JPanel();
		p_search = new JPanel();
		ch_category = new Choice();
		t_keyword = new JTextField();
		bt_search = new JButton("검색");
		p_list = new JPanel();
		p_east = new JPanel();
		la_notice = new JLabel("공지사항",SwingConstants.LEFT);
		p_notice = new JPanel();
		
		//스타일 및 레이아웃
		p_center.setPreferredSize(new Dimension(700,650));
		p_search.setPreferredSize(new Dimension(700,35));
		p_search.setBackground(Color.WHITE);
		ch_category.setPreferredSize(new Dimension(160,30));
		t_keyword.setPreferredSize(new Dimension(460,30));
		bt_search.setPreferredSize(new Dimension(60,30));
		p_list.setPreferredSize(new Dimension(700,650));
		p_list.setBackground(Color.WHITE);
		
		p_east.setPreferredSize(new Dimension(280,650));
		la_notice.setPreferredSize(new Dimension(280,20));
		la_notice.setFont(new Font("맑은 고딕", Font.BOLD, 18));
		p_notice.setPreferredSize(new Dimension(280,340));
		p_notice.setBackground(Color.WHITE);
		
		//검색 카테고리 등록
		ch_category.add("Writer");
		ch_category.add("Title");

		//조립
		p_search.add(ch_category);
		p_search.add(t_keyword);
		p_search.add(bt_search);
		p_list.add(p_search,BorderLayout.NORTH);
		p_center.add(p_list, BorderLayout.CENTER);
		add(p_center, BorderLayout.CENTER);
		p_notice.add(la_notice);
		p_east.add(p_notice);
		add(p_east, BorderLayout.EAST);
		//이벤트
		
		
		//보여주기
		//setBackground(Color.BLACK);
	}
	
	
}
