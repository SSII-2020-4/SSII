package apartado.a;

public class Test1ExclusionMutua {
	
	/* Test1ExclusionMutua.java ------
	 * 	La exclusi�n mutua ocurre cuando varios hilos ejecutan una secci�n cr�tica de un programa.
	 * 	En este script representamos c�mo 2 hilos incrementan un contador concurrentemente, obteniendo resultados indeseados.
	 */
	
	

	public static void main(String[] args) throws InterruptedException {
		
		class Contador{
			int contador = 0;
			public void incrementar() {
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
