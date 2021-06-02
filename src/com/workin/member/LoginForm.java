package com.workin.member;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.workin.main.AppMain;

public class LoginForm extends JFrame {
	AppMain appMain;

	JPanel p_back;
	JPanel p_center;
	JLabel la_title;
	JLabel la_login;
	JLabel la_id;
	JLabel la_pass;
	JTextField t_id;
	JPasswordField t_pass;
	JButton bt_login;
	JButton bt_join;

	// db
	Connection con;
	String url = "jdbc:mysql://localhost:3306/workinapp";
	String user = "root";
	String password = "1234";

	public LoginForm() {

		Color c = new Color(44, 62, 80);

		// 생성
		p_back = new JPanel();
		p_center = new JPanel();
		la_title = new JLabel("Work IN");
		la_login = new JLabel("로그인");
		la_id = new JLabel("ID");
		la_pass = new JLabel("Pass");
		t_id = new JTextField(20); // 20자 너비의 크기 갖음(20자만 넣을수있는게 아니다!)
		t_pass = new JPasswordField(20);
		bt_login = new JButton("Login");
		bt_join = new JButton("Join");

		// 스타일 및 레이아웃
		p_back.setBackground(c);
		p_center.setPreferredSize(new Dimension(600, 350));
		la_title.setPreferredSize(new Dimension(600, 20));
		la_title.setHorizontalAlignment(JLabel.CENTER);
		la_login.setPreferredSize(new Dimension(600, 20));
		la_login.setHorizontalAlignment(JLabel.CENTER);
		la_id.setPreferredSize(new Dimension(300, 35));
		t_id.setPreferredSize(new Dimension(300, 35));
		la_pass.setPreferredSize(new Dimension(300, 35));
		t_pass.setPreferredSize(new Dimension(300, 35));

		// 조립
		p_center.add(la_title);
		p_center.add(la_login);
		p_center.add(la_id);
		p_center.add(t_id);
		p_center.add(la_pass);
		p_center.add(t_pass);
		p_center.add(bt_login);
		p_center.add(bt_join);
		p_back.add(p_center);
		add(p_back);

		// 이벤트
		bt_login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loginCheck();
			}
		});

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				disConnect(); // DB 접속해제
				System.exit(0); // kill process
			}
		});

		// 메시지 입력 엔터처리
		t_pass.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					loginCheck();
				}
			}
		});

		// 보여주기
		setBounds(400, 100, 1200, 720);
		setBackground(c);
		setVisible(true);

		connect();
	}

	public void connect() {
		/*
		 * 1)드라이버 로드 2)접속 3)쿼리 실행 4)접속해제
		 */
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(url, user, password);
			if (con != null) {
				this.setTitle("접속 성공");
			} else {
				this.setTitle("접속 실패");
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void disConnect() {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void loginCheck() {
		String sql = "select * from member where user_id=? and user_pass=?";

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, t_id.getText());
			pstmt.setString(2, new String(t_pass.getPassword()));
			rs = pstmt.executeQuery();

			// 회원인지 아닌지?
			if (rs.next()) {
				JOptionPane.showMessageDialog(this, t_id.getText() + "님 환영합니다.");
				this.setVisible(false);
				new AppMain();
			} else {
				JOptionPane.showMessageDialog(this, "로그인 정보가 올바르지 않습니다.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			release(pstmt, rs);
		}

	}

	// 쿼리문이 select인 경우
	public void release(PreparedStatement pstmt, ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		new LoginForm();
	}
}