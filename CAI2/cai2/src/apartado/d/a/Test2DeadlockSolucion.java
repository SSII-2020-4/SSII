package apartado.d.a;

public class Test2DeadlockSolucion {
	
	/* Para evitar un deadlock o interbloqueo 
	 * 	es necesario observar en qu� orden se est�n accediendo a los recursos y ordenarlos correctamente. 
	 */
	
	

	public static Object recurso1 = new Object();
	public static Object recurso2 = new Object();

	public static void main(String[] args) {
	      ThreadDeadlock1 td1 = new ThreadDeadlock1();
	      ThreadDeadlock2 td2 = new ThreadDeadlock2();
	      td1.start();
	      td2.start();

	}
	
	 private static class ThreadDeadlock1 extends Thread {
	      public void run() {
	         synchronized (recurso1) {
	            System.out.println("Hilo 1: Utilizando recurso 1...");
	            
	            try { Thread.sleep(1000); }
	            catch (InterruptedException e) {}
	            System.out.println("Hilo 1: Esperando por el recurso 2...");
	            
	            synchronized (recurso2) {
	               System.out.println("Hilo 1: Utilizando recursos 1 y 2...");
	            }
	         }
	      }
	   }
	 
	 // cambiando el orden de uso de los recursos, evitamos el deadlock.
	   private static class ThreadDeadlock2 extends Thread {
	      public void run() {
	         synchronized (recurso1) {
	            System.out.println("Hilo 2: Utilizando recurso 1...");
	            
	            try { Thread.sleep(1000); }
	            catch (InterruptedException e) {}
	            System.out.println("Hilo 2: Esperando por el recurso 2...");
	            
	            synchronized (recurso2) {
	               System.out.println("Hilo 2: Utilizando recursos 1 y 2...");
	            }
	         }
	      }
	   } 

}
