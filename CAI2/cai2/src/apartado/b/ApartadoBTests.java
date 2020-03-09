package apartado.b;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ApartadoBTests {

	public static void main(String[] args) throws IOException, InterruptedException {

		
		/* En este script, se muestran los riesgos que existen relacionados con la integridad 
		*  al no aplicar las tuberías de forma adecuada en la comunicación interprocesos.
		*  Se presentan 3 escenarios distintos. Para cada escenario, se usan estrategias distintas 
		*  que se pueden usar en los otros si se adaptan correctamente:
		* 	- Cuando se quieren transferir objetos entre procesos (Se usa la clase ArrayBlockingQueue<E>)
		* 	- Cuando se quieren transferir bytes (Se usa la clase ByteBuffer)
		* 	- Cuando se quieren transferir Strings (Se usa un array de String)
		* 
		*  Para cada escenario, se usan estrategias distintas que se pueden usar en los otros si se
		*  adaptan correctamente. De esta forma, se pueden observar los fallos de cada uno y como ponerles
		*  solución. 
		*/
		
		
//		Descomentar el método que se quiera probar.		
//		transferenciaObjetosEntreProcesos();
//		transferenciaBytesEntreProcesos();
		transferenciaStringsEntreProcesos();

	}

	
	/* En este método, se generan números del 1 al 5000 y se imprimen ordenadamente. Se aprecia que no se
	 * imprimen todos los números.
	 * 
	 */
	public static void transferenciaObjetosEntreProcesos() {
		
		// En lugar de Integer, se podrian usar cualquier tipo de objetos, 
		// incluso String, aunque luego se ha dedicado un método a la tranferencia de String
		// Se ha decidido que el tamaño del buffer será de 10 Integer
		
		Queue<Integer> buffer = new ArrayBlockingQueue<Integer>(10);

		Thread threadProductor = new Thread("Productor") {
			public void run() {
				for (int i = 0; i < 5000; i++) {
					try {
						buffer.add(i);
					} catch (IllegalStateException e) {
						// Se ha capturado la excepcion
					}
				}
			};

		};

		Thread threadConsumidor = new Thread("Consumidor") {
			public void run() {
				// Se para la ejecucion cuando el hilo muera y la pila este vacia
				while (threadProductor.isAlive() || !buffer.isEmpty()) {
					if (!buffer.isEmpty()) {
						System.out.println(buffer.poll());
					}

				}
			};

		};

		threadProductor.start();
		threadConsumidor.start();
	}
	
	
	/* En este método, se generan bytes desde 0x00 hasta 0x0F y el proceso consumidor 
	 * los imprime por pantalla. El proceso prodcutor los genera tan rápido que al consumidor
	 * no le da tiempo a consumirlos todos. 
	 * 
	 */
	public static void transferenciaBytesEntreProcesos() {
		
		// Se ha decidido que el buffer tendrá un tamaño limitado de 10 bytes.
		// Se puede usar para otros tipos como String (StringBuffer) o Long (LongBuffer).
		// No se recomienda usar estas clases a no ser que sea imprescindible, por dos motivos:
		//	- Es complejo el concepto, lo que dificulta el desarrollo y el mantenimiento del código.
		//	- No están orientados a usar para la comunicación entre procesos. Si se usa para este fin, 
		//	  hay que añadir la sincronización apropiada.
		
		ByteBuffer buffer = ByteBuffer.allocate(10);

		Thread threadProductor = new Thread() {
			public void run() {
				byte transmision = 0x00;
				for (int i = 0; i < 16; i++) {
					try {
						buffer.put(transmision);
					} catch (BufferOverflowException e) {
						// Se ha capturado la excepcion
					} finally {
						transmision++;
					}
				}
			};

		};

		Thread threadConsumidor = new Thread() {
			public void run() {
				buffer.rewind();
				while (threadProductor.isAlive() || buffer.hasRemaining()) {
					if (buffer.hasRemaining()) {
						byte[] aux = new byte[buffer.remaining()];
						buffer.get(aux);
						System.out.println(Arrays.toString(aux));
					}
				}
				;
			};
		};

		threadProductor.start();
		threadConsumidor.start();
	}
	
	/*
	 * En este método, un proceso lee Strings de un archivo y las almacena en un buffer y otro
	 * proceso las lee de este buffer y las escribe en otro archivo. Se espera que el contenido 
	 * de los ficheros sea el mismo, pero no es así al ejecutar este método.
	 * 
	 */
	public static void transferenciaStringsEntreProcesos() throws IOException, InterruptedException {
		FileReader fr = new FileReader(System.getProperty("user.dir") + "\\CAI2\\cai2\\files\\archivo-tests-apartado-b.txt");
		BufferedReader br = new BufferedReader(fr);

		FileWriter fw = new FileWriter(System.getProperty("user.dir") + "\\CAI2\\cai2\\files\\archivo-salida-tests.txt");
		BufferedWriter bw = new BufferedWriter(fw);
		
		List<String> lines = br.lines().collect(Collectors.toList());
		
		//EL tamaño del buffer es un array de 10 Strings. 
		
		Integer size = 10;
		String buffer[] = new String[size];
		AtomicInteger pos = new AtomicInteger(0);
		
		Thread threadProductor = new Thread("Productor") {
			public void run() {
				for (String line : lines) {
					if (pos.get() < size) {
						buffer[pos.getAndIncrement()] = line;
					}	
				}
			};

		};

		Thread threadConsumidor = new Thread("Consumidor") {
			public void run() {
				while (threadProductor.isAlive() || pos.get() != 0) {
					try {
						System.out.println(buffer[0]);
						bw.write(buffer[0]);
						bw.newLine();
					} catch (IOException e) {
						//Se ha controlado la excepcion
					} catch (NullPointerException mp) {
						// Se ha controlado la excepcion
					}
					for (int i = 1; i < pos.get(); i++) {
						buffer[i-1] = buffer[i];
					}
					pos.decrementAndGet();

				}
			};

		};
		
		threadProductor.start();
		threadConsumidor.start();
		
		threadConsumidor.join();
		
		bw.flush();
		fw.flush();
		
		fr.close();
		br.close();
		fw.close();
		bw.close();

	}


}
