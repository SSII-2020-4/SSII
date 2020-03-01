package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ServerSocketFactory;

public class IntegrityServerSocket {
	private static final Logger LOGGER = Logger.getLogger(IntegrityServerSocket.class.getName());

	public static void main(String[] args) throws IOException {
		// espera conexiones del cliente y comprueba login
		LOGGER.log(Level.INFO, "Starting Socket");
		int portNumber = 8080;
		// crea Socket de la factoría
		ServerSocketFactory socketFactory = ServerSocketFactory.getDefault();
		boolean isGood = true;
		while (isGood) {
			LOGGER.log(Level.INFO, "Waiting connections..");
			try (ServerSocket serverSocket = socketFactory.createServerSocket(portNumber, 1);
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
				} while (!text.equals("."));

			} catch (IOException e) {
				isGood = false;
				LOGGER.log(Level.SEVERE, "Exception caught when trying to listen on port " + portNumber
						+ " or listening for a connection");
				LOGGER.log(Level.SEVERE, e.getMessage());
			}
		}
	}
}
