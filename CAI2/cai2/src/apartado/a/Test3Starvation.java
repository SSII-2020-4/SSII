package apartado.a;

public class Test3Starvation {
	
	/* Starvation ocurre cuando un hilo consume un recurso por un largo periodo de tiempo, 
	 * de tal forma que los dem�s hilos quedan bloqueados. 
	 */
	
	public static void main(String[] args) {
		 Trabajador trabajador = new Trabajador();
		 
	        for (int i = 0; i < 5; i++) {
	            new Thread(new Runnable() {
	                public void run() {
	                    trabajador.trabajar();
	                }
	            }).start();
	        }
		
	}
	
	
	private static class Trabajador{
	    public synchronized void trabajar() {
	        String hilo = Thread.currentThread().getName();

	        try { Thread.sleep(1000); }
	        catch (InterruptedException e) {}
	        System.out.println("El hilo " + hilo + " ha escrito este mensaje.");
	 
	        while (true) {
	            System.out.println(hilo + " est� trabajando.");
	            
	        }
	    }
	}
	


}
