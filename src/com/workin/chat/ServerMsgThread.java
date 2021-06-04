package com.workin.chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.workin.main.AppMain;


public class ServerMsgThread extends Thread{
	AppMain appMain;

	Socket socket;
	BufferedReader buffr;
	BufferedWriter buffw;
	boolean flag=true;
	
	Member member;
	
	
	public ServerMsgThread(Socket socket, AppMain appMain) {
		this.socket=socket;
		this.appMain=appMain;
		
		try {
			buffr= new BufferedReader(new InputStreamReader(socket.getInputStream()));
			buffw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//듣고
	public void listen() {
		String msg=null;
		try {
			msg= buffr.readLine();

			JSONParser jsonParser=new JSONParser();
			try {
				JSONObject packet=(JSONObject)jsonParser.parse(msg);
				String cmd = (String)packet.get("cmd");
				
				if(cmd.equals("login")) { 
					member = new Member(); 
					
					JSONObject obj=(JSONObject)packet.get("member");
					member.setUser_name((String)obj.get("user_name")); 
					member.setUser_id((String)obj.get("user_id")); 
					member.setUser_pass((String)obj.get("user_pass")); 
					member.setRegdate((String)obj.get("regdate")); 
					member.setImg((String)obj.get("img")); 
				}else if(cmd.equals("chat")) { 
					String message = (String)packet.get("message");
					
					
					
					//broadcasting !!!
					for(int i=0;i<appMain.getClientList().size();i++) {
						ServerMsgThread msgThread=appMain.getClientList().get(i);
						System.out.println("여긴 오니?");
						System.out.println(msgThread);
						msgThread.send(member.getUser_name()+"의 말:"+message);
					}
				}else if(cmd.equals("emo")) { 
					
				}else if(cmd.equals("add_friend")) {
					
				}else if(cmd.equals("present")) { 
				}
				
			} catch (ParseException e) {
				e.printStackTrace();
			}

		} catch (IOException e) {
			//e.printStackTrace();
			flag=false; 
			appMain.getClientList().remove(this);
			System.out.println("현재까지 참여자 수 : "+appMain.getClientList().size()+"\n");
		}
	}
	
	//말하기
	public void send(String msg) {
		try {
			buffw.write(msg+"\n");
			buffw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		while(flag) {
			listen();
		}
	}
	
}