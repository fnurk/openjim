package Server;

import java.net.*;
import java.io.*;


public class ServerClient implements Runnable{
	public Socket socket;
	private Server server;
	public DataOutputStream out = null;
	private DataInputStream in = null;
	public boolean running;
	private String nickname;
	
	public ServerClient(Socket socket, Server server){
		this.socket = socket;
		this.nickname = "Unknown";
		this.server = server;
	}

	@Override
	public void run() {
		running = true;
		try{
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
		}catch(IOException e){
			System.out.println("IOexception: I or O failed");
		}
		while(running){
			String intext;
			try{
				intext = in.readUTF();
				if(intext.equals("$DISCONNECT")){
					disconnect();
					break;
				}
				if(intext.equals("$CHANGENICK")){
					changeNickname();
				}
				else{
					server.sendText(nickname + ": " + intext);
				}
				}catch(IOException e){
				server.addText("Could not read. " + nickname + " probably dropped.");
				server.removeClient(this);
				running = false;

			}
		}
	}
	
	
	public void sendText(String text){
		try {
			out.writeUTF(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setNickname(String nick){
		nickname = nick;
	}
	
	public void changeNickname(){
		try {
			String oldnick = nickname;
			out.flush();
			out.writeUTF("write your new nick");
			nickname = in.readUTF();
			server.sendText("User " + oldnick + " changed nickname to " + nickname);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void disconnect(){
		server.removeClient(this);
		server.sendText("User " + this.nickname + " at " + socket.getInetAddress().toString().replace("/","") + " disconnected."); 
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		running = false;
		
	}
}
