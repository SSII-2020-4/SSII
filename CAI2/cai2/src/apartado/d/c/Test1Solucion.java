package apartado.d.c;

import java.util.logging.Logger;

import apartado.c.test1.IntegrityClientSocket;
import apartado.c.test1.IntegrityMultiServerSocket;

public class Test1Solucion {
	private static final Logger LOGGER = Logger.getLogger(Test1Solucion.class.getName());

	public static void main(String[] args) {

		Integer port = 8080;
		IntegrityMultiServerSocket server = new IntegrityMultiServerSocket(port);
		IntegrityClientSocket client1 = new IntegrityClientSocket(port);
		IntegrityClientSocket client2 = new IntegrityClientSocket(port);

		try {
			/*
			 * Inicializa el servidor permitiendo sólo 1 cliente. Cuando tenemos 2 clientes
			 * realizando peticiones no va a responderle, es decir que se va a perder la
			 * integridad en la comunicación. Para poder capturar más de un cliente haría
			 * falta meter entre el socket y el cliente un handler.
			 */
			server.startServer();
			Thread.sleep(1000);
			// Uno de los clientes se conecta y recibe respuesta, pero
			client1.sendMessage("Cliente1: Hola servidor");
			client2.sendMessage("Cliente2: Hola servidor");

			Thread.sleep(5000);

			server.stopServer();

			LOGGER.info("Server stopped");
			System.exit(0);

		} catch (Exception e) {
			LOGGER.severe("PrincipalException: " + e.getMessage());
			server.stopServer();
			System.exit(1);
		}

	}

}
