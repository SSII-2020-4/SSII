package apartado.c.test1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IntegrityMultiServerSocket extends Thread {
	private static final Logger LOGGER = Logger.getLogger(IntegrityMultiServerSocket.class.getName());

	private Integer port = 8080;
	private boolean running = false;
	private ServerSocket serverSocket;

	public IntegrityMultiServerSocket(Integer port) {
		this.port = port;
	}

	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(port);
			while (running)
				new ClientHandler(serverSocket.accept()).start();
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		} finally {
			this.stopServer();
		}
	}

	public void startServer() {
		this.running = true;
		this.start();
	}

	public void stopServer() {
		running = false;
		try {
			serverSocket.close();
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		}
		this.interrupt();
	}

	private static class ClientHandler extends Thread {
		private Socket clientSocket;

		public ClientHandler(Socket socket) {
			this.clientSocket = socket;
		}

		@Override
		public void run() {
			try (// abre BufferedReader para leer datos del cliente
					BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
					// abre PrintWriter para enviar datos al cliente
					PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);) {

				String text;
				do {
					text = input.readLine();
					output.println("Server: " + text);
				} while (text != null && text.length() > 0);

				clientSocket.close();
			} catch (IOException e) {
				LOGGER.severe(e.getMessage());
			}
		}
	}
}
