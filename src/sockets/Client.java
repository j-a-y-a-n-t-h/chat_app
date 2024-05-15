package sockets;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				new ClientGUI().initialize();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
}

class ClientGUI {
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	private Frame frame;
	private TextArea chatArea;
	private TextField inputField;
	private Button sendButton;
	private TextField nameField;

	public void initialize() throws IOException {
		String clientName = promptForName();
		socket = new Socket("localhost", 1234);
		out = new PrintWriter(socket.getOutputStream(), true);

		// Send client name to the server
		out.println(clientName);

		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		
		frame = new Frame("Chat Client");
		chatArea = new TextArea();
		inputField = new TextField();
		sendButton = new Button("Send");

		
		frame.setLayout(new BorderLayout());

		
		frame.add(chatArea, BorderLayout.CENTER);
		frame.add(inputField, BorderLayout.SOUTH);
		frame.add(sendButton, BorderLayout.EAST);

		
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String message = inputField.getText();
				out.println(message);
				chatArea.append("You: " + message + "\n");
				inputField.setText("");
			}
		});

		
		frame.setSize(400, 300);
		frame.setVisible(true);

		// Listen for messages in a separate thread
		new Thread(() -> {
			try {
				String message;
				while ((message = in.readLine()) != null) {
					chatArea.append(message + "\n");
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}).start();
	}

	private String promptForName() {
		nameField = new TextField();
		Dialog dialog = new Dialog(frame, "Enter Your Name", true);
		dialog.setLayout(new BorderLayout());
		dialog.add(new Label("Enter your name:"), BorderLayout.WEST);
		dialog.add(nameField, BorderLayout.CENTER);
		Button okButton = new Button("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
			}
		});
		dialog.add(okButton, BorderLayout.SOUTH);
		dialog.setSize(300, 100);
		dialog.setLocationRelativeTo(frame);
		dialog.setVisible(true);
		return nameField.getText();
	}
}
