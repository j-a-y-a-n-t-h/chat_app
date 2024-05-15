package sockets;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
	private static List<ClientHandler> clients = new ArrayList<>();

	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = new ServerSocket(1234);
		System.out.println("Server started. Waiting for clients...");

		while (true) {
			Socket socket = serverSocket.accept();
			System.out.println("Client connected: " + socket);

			// Create a new thread to handle this client
			ClientHandler clientHandler = new ClientHandler(socket);
			clients.add(clientHandler);
			new Thread(clientHandler).start();
		}
	}

	public static void broadcastMessage(String message, ClientHandler sender) {
		for (ClientHandler client : clients) {
			if (client != sender) {
				client.sendMessage(message);
			}
		}
	}
}
