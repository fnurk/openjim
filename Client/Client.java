package Client;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;

import javax.swing.JFrame;

class Client {
	public void sendMessage(String x) {
		try {
			out.writeUTF(x);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Socket client;
	public InputStream inServer;
	public OutputStream outServer;
	public DataInputStream in;
	public DataOutputStream out;
	private boolean running = true;

	public void start() {
		// Initialize GUI
		GUI guiObject = new GUI(this);
		guiObject.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		guiObject.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				shutdownServer();
			}
		});

		guiObject.setSize(500, 300);
		guiObject.setVisible(true);
		guiObject.setResizable(true);

		// Connect to server
		try {
			client = new Socket("94.254.22.75", 25568);
			inServer = client.getInputStream();
			outServer = client.getOutputStream();

			in = new DataInputStream(inServer);
			out = new DataOutputStream(outServer);

		} catch (IOException e) {
			e.printStackTrace();
		}

		// Program loop
		while (running) {
			try {

				guiObject.tr(in.readUTF());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//close disconnection at shutdown, when loop is exited
		try {
			out.close();
			in.close();
			out.writeUTF("$DISCONNECT");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}

	public void shutdownServer() {
		running = false;
		System.exit(0);
	}

	public static void main(String[] args) {
		Client client = new Client();
		client.start();
	}

}