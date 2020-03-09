package sensores.reactor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

public class ServerSensores {

	protected static Queue<String> datosSensores = new LinkedList<>();
	private ServerSocket server;

	public ServerSensores(String ipAddress) {
		try {
			if (ipAddress != null && !ipAddress.isEmpty())
				this.server = new ServerSocket(0, 1, InetAddress.getByName(ipAddress));
			else
				this.server = new ServerSocket(0, 1, InetAddress.getLocalHost());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void listen() throws IOException {
		String data = null;
		Socket client = this.server.accept();
		String clientAddress = client.getInetAddress().getHostAddress();
		System.out.println("\r\nNuevo Sensor desde " + clientAddress);

		BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		while ((data = in.readLine()) != null) {
			System.out.println("\r\nLectura desde " + clientAddress + ": " + data);
			try {
				datosSensores.add(data);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws Exception {
		ServerSensores app = new ServerSensores(args[0]);
		System.out.println("\r\nEjecutando el Servidor de Sensores: ");

		app.listen();
	}

}
