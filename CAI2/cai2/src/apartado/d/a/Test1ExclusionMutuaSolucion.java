package apartado.d.a;

public class Test1ExclusionMutuaSolucion {
	
	/* ExclusionMutuaSolucion.java ----
	 * 	Para solucionar problemas de exclusi�n mutua, debemos asegurarnos que la secci�n cr�tica s�lo sea ejecutada por un �nico hilo.
	 * 	En java tenemos la palabra reservada "synchronized" para ello. 
	 */
	
	

	public static void main(String[] args) throws InterruptedException {
		
		class Contador{
			int contador = 0;
			public synchronized void incrementar() { //solo un hilo accede a este m�todo.
				contador++;
			}
			public int getContador() {
				return contador;
			}
		}
		final Contador contador  = new Contador();
		
		class HiloContador extends Thread{
			public void run() {
				for (int i = 0; i < 50000; i++) {
					contador.incrementar();
				}
			}
		}
		
		HiloContador h1 = new HiloContador();
		HiloContador h2 = new HiloContador();
		h1.start();
		h2.start();
		h1.join();
		h2.join();
		System.out.println(contador.getContador()); //deber�a dar 100000
	}
}
