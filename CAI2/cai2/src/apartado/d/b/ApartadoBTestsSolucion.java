package apartado.d.b;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class ApartadoBTestsSolucion {
	public static void main(String[] args) throws IOException, InterruptedException {
		
		/*
		 * En este script, se muestran las soluciones a los problemas encontrados al usar 
		 * diferentes clases para la comunicación entre procesos. Las soluciones para los 
		 * escenarios mostrados anteriormente son:
		 * 	- Si se usa la clase ArrayBlockingQueue<E>, solo hay que adpatar el código para que 
		 * 		solo añada elementos a la queue cuando haya espacio.
		 * 	- En lugar de usar ByteBuffer, usar PipedInputStream y PipedOutputStream que 
		 * 		facilitan la sincronización entre procesos.
		 * 	- Si se quieren usar arrays como buffer, hay que tener en cuenta los problemas 
		 * 		de integridad y programar en base a la sincronización entre los procesos. 
		 * 		En la medida de los posible, usar ArrayBlockingQueue<E> que facilita esta 
		 * 		tarea, aunque si hay que usar arrays, el método 
		 * 		transferenciaStringsEntreProcesos() muestra un código de ejemplo.
		 */
		
// 		Descomentar el método que se quiera usar
//		transferenciaObjetosEntreProcesos();
//		transferenciaBytesEntreProcesos();
//		transferenciaStringsEntreProcesos();
	}

	public static void transferenciaObjetosEntreProcesos() {
		Queue<Integer> pruebas = new ArrayBlockingQueue<Integer>(10);

		Thread threadProductor = new Thread() {
			public void run() {
				int i = 0;
				while (i < 5000) {
					try {
						pruebas.add(i);
						i++;
					} catch (IllegalStateException e) {

					}
				}
			}
		};

		Thread threadConsumidor = new Thread() {
			public void run() {

				while (threadProductor.isAlive() || !pruebas.isEmpty()) {
					if (!pruebas.isEmpty()) {
						System.out.println(pruebas.poll());
					}
				}
			};
		};

		threadProductor.start();
		threadConsumidor.start();
	}

	public static void transferenciaBytesEntreProcesos() throws InterruptedException, IOException {
		PipedInputStream inputStream = new PipedInputStream(10);
		PipedOutputStream outputStream = new PipedOutputStream(inputStream);

		Thread threadProductor = new Thread() {
			public void run() {
				byte transmision = 0x00;
				int i = 0;
				while (i < 16) {
					try {
						outputStream.write(transmision);
						i++;
					} catch (IOException e) {

					} finally {
						transmision++;
					}
				};
			};
		};

		Thread threadConsumidor = new Thread() {
			public void run() {
				try {
					while (threadProductor.isAlive() || inputStream.available() != 0) {
						if (inputStream.available() != 0) {
							byte[] aux = new byte[inputStream.available()];
							inputStream.read(aux);
							System.out.println(Arrays.toString(aux));
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				};
			};
		};

		threadProductor.start();
		threadConsumidor.start();

		threadConsumidor.join();

		inputStream.close();
		outputStream.close();
	}

	public static void transferenciaStringsEntreProcesos() throws InterruptedException, IOException {
		FileReader fr = new FileReader(System.getProperty("user.dir") + "\\CAI2\\cai2\\files\\archivo-tests-apartado-b.txt");
		BufferedReader br = new BufferedReader(fr);
		
		// Se especifica la ruta en la que se va a guardar el archivo

		FileWriter fw = new FileWriter(System.getProperty("user.dir") + "\\CAI2\\cai2\\files\\archivo-salida-tests.txt");
		BufferedWriter bw = new BufferedWriter(fw);

		Integer size = 10;
		String buffer[] = new String[size];
		AtomicInteger pos = new AtomicInteger(0);

		Thread threadProductor = new Thread() {
			public void run() {
				boolean keepReading = true;
				String lineToWrite;
				try {
					lineToWrite = br.readLine();
					System.out.println(lineToWrite);
					while (keepReading) {
						if (lineToWrite == null) {
							keepReading = false;
						} else if (pos.get() < size) {
							if (buffer[size - 1] == null) {
								buffer[pos.getAndIncrement()] = lineToWrite;
								lineToWrite = br.readLine();
							}
						}
					}
				} catch (IOException e) {

				}
			}

		};

		Thread threadConsumidor = new Thread("Consumidor") {
			public void run() {
				while (threadProductor.isAlive() || buffer[0] != null) {
					if (buffer[0] != null) {
						try {
							System.out.println(buffer[0]);
							bw.write(buffer[0]);
							bw.newLine();
						} catch (IOException e) {

						} catch (NullPointerException mp) {

						}
						reorderArray(buffer, pos);
					}

				}
			}

			private synchronized void reorderArray(String[] buffer, AtomicInteger pos) {
				for (int i = 1; i < pos.get(); i++) {
					buffer[i - 1] = buffer[i];
				}
				if (pos.get() > 0) {
					pos.decrementAndGet();
				}
				buffer[pos.get()] = null;

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
