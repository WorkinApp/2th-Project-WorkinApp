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

import com.workin.chat.ChatServer;
import com.workin.chat.Member;
import com.workin.chat.ServerMsgThread;


public class ServerMsgThread extends Thread{
	ChatServer chatServer;
	Socket socket;
	BufferedReader buffr;
	BufferedWriter buffw;
	boolean flag=true;
	
	Member member;
	
	
	public ServerMsgThread(Socket socket, ChatServer chatServer) {
		this.socket=socket;
		this.chatServer=chatServer;
		
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
					member.setUser_id((String)obj.get("user_id")); 
					member.setName((String)obj.get("name")); 
					member.setRegdate((String)obj.get("regdate"));
				}else if(cmd.equals("chat")) { 
					String message = (String)packet.get("message");
					
					
					
					//broadcasting !!!
					for(int i=0;i<chatServer.clientList.size();i++) {
						ServerMsgThread msgThread=chatServer.clientList.get(i);
						msgThread.send(member.getUser_id()+"의 말:"+message);
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
			chatServer.clientList.remove(this);
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