package apartado.c.test2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ServerSocketFactory;

public class IntegrityServerSocket extends Thread {
	private static final Logger LOGGER = Logger.getLogger(IntegrityServerSocket.class.getName());

	private Integer port = 8080;
	private boolean running = false;

	public IntegrityServerSocket(Integer port) {
		this.port = port;
	}

	@Override
	public void run() {
		// espera conexiones del cliente y comprueba login
		LOGGER.log(Level.INFO, "Starting Socket");
		while (this.running) {
			// crea Socket de la factoría
			ServerSocketFactory socketFactory = ServerSocketFactory.getDefault();
			LOGGER.log(Level.INFO, "Waiting connections..");
			try (ServerSocket serverSocket = socketFactory.createServerSocket(this.port, 2);
					Socket clientSocket = serverSocket.accept();
					// abre BufferedReader para leer datos del cliente
					BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
					// abre PrintWriter para enviar datos al cliente
					PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);) {
				LOGGER.log(Level.INFO, "ServerSocket accepted");

				String text;
				do {
					text = input.readLine();
					output.println("Server: " + text);
				} while (text != null && text.length() > 0);

			} catch (IOException e) {
				this.running = false;
				LOGGER.log(Level.SEVERE, String.format(
						"Exception caught when trying to listen on port %d or listening for a connection", this.port));
				LOGGER.log(Level.SEVERE, e.getMessage());
			}
		}
	}

	public void startServer() {
		this.running = true;
		this.start();
	}

	public void stopServer() {
		running = false;
		this.interrupt();
	}

}
