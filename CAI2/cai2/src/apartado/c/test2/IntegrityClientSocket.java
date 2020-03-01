package apartado.c.test2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.SocketFactory;

public class IntegrityClientSocket extends Thread {
	private static final Logger LOGGER = Logger.getLogger(IntegrityClientSocket.class.getName());

	private String hostname = "localhost";
	private int port = 8080;
	private Socket clientSocket;
	private PrintWriter out;
	private BufferedReader in;
	private String text;

	public IntegrityClientSocket(Integer port) {
		this.port = port;
	}

	@Override
	public void run() {
		SocketFactory socketFactory = SocketFactory.getDefault();
		try {
			this.clientSocket = socketFactory.createSocket(this.hostname, this.port);
			this.out = new PrintWriter(this.clientSocket.getOutputStream(), true);
			this.in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));

			this.out.println(this.text);
			this.out.flush();

//			Thread.sleep(4000);
			// leemos respuesta del servidor
			String response = this.in.readLine();
			LOGGER.log(Level.INFO, response);

			this.interrupt();

		} catch (IOException  e) {
			LOGGER.severe(e.getMessage());
			this.interrupt();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
			this.interrupt();
			System.exit(1);
		}
	}

	public void sendMessage(String text) {
		this.text = text;
		this.start();

//		try {
//			this.out.println(text);
//			this.out.flush();
//
//			// leemos respuesta del servidor
//			response = this.in.readLine();
//			LOGGER.log(Level.INFO, response);
//
//		} catch (UnknownHostException e) {
//			LOGGER.log(Level.SEVERE, "Don't know about host " + this.hostname);
//			LOGGER.log(Level.SEVERE, e.getMessage());
//			System.exit(1);
//		} catch (IOException e) {
//			LOGGER.log(Level.SEVERE, "Couldn't get I/O for the connection to IO" + this.hostname);
//			LOGGER.log(Level.SEVERE, e.getMessage());
//			System.exit(1);
//		} catch (Exception e) {
//			LOGGER.log(Level.SEVERE, e.getMessage());
//			System.exit(1);
//		}
	}

	public void stopConnection() {
		try {
			this.in.close();
			this.out.close();
			this.clientSocket.close();
			this.interrupt();
		} catch (IOException e) {
			LOGGER.severe(e.getMessage());
		}
	}
}
