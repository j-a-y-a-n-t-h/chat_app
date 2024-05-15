package sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	private String clientName; // Add a field to store the client's name

	public ClientHandler(Socket socket) throws IOException {
		this.socket = socket;
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);

		// Receive client's name from the client upon connection
		clientName = in.readLine();
		System.out.println("Client connected: " + clientName);
	}

	@Override
	public void run() {
		try {
			String message;
			while ((message = in.readLine()) != null) {
				System.out.println("Received from client " + clientName + ": " + message);
				// Broadcast message to other clients along with client's name
				Server.broadcastMessage(clientName + ": " + message, this);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void sendMessage(String message) {
		out.println(message);
	}
}
