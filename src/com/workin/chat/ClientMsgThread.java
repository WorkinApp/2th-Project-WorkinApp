package com.workin.chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ClientMsgThread extends Thread{
	ChatClient chatClient;
	Socket socket;
	BufferedReader buffr;
	BufferedWriter buffw;
	boolean flag=true;
	
	public ClientMsgThread(Socket socket, ChatClient chatClient) {
		this.socket=socket;
		this.chatClient=chatClient;
		
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
			chatClient.area.append(msg+"\n");
		} catch (IOException e) {
			//e.printStackTrace();
			flag=false; //쓰레드 dead 상태로 두기 
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
		if(buffw!=null) {
			try {
				buffw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(buffr!=null) {
			try {
				buffr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
