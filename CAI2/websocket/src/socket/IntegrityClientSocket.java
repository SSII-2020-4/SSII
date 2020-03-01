package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.SocketFactory;
import javax.swing.JOptionPane;

public class IntegrityClientSocket {
	private static final Logger LOGGER = Logger.getLogger(IntegrityClientSocket.class.getName());

	public static void main(String[] args) throws IOException {

		SocketFactory socketFactory = SocketFactory.getDefault();

		String hostName = "localhost";
		int portNumber = 8080;

		// crea Socket de la factoria
		try (Socket echoSocket = socketFactory.createSocket(hostName, portNumber);
				// crea PrintWriter para enviar login a servidor
				PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
				// crea BufferedReader para leer respuesta del servidor
				BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
				BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

			String text;
			do {
				// solicita el username
				text = JOptionPane.showInputDialog(null, "Enter text:");
				// coloca al usuario en el flujo
				out.println(text);
				// envia al servidor
				out.flush();

				// leemos respuesta del servidor
				String response = in.readLine();
				JOptionPane.showMessageDialog(null, response);

			} while (!text.equals("."));

		} catch (UnknownHostException e) {
			LOGGER.log(Level.SEVERE, "Don't know about host " + hostName);
			System.exit(1);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Couldn't get I/O for the connection to " + hostName);
			System.exit(1);
		}
	}
}
