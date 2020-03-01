package apartado.a;

public class Test2Deadlock {
	
	/* Test2Deadlock.java ----
	 *  Deadlock ocurre cuando un proceso espera un evento el cual nunca va a suceder.
	 *  En este script hemos utilizado 2 hilos para simular este suceso. El hilo 1 accede al recurso 1, mientras que el hilo 2 al recurso 2,
	 *  para posteriormete el hilo 1 intentar acceder al recurso 2, y el hilo 2 al recurso 1, form�ndose as� el bloqueo.
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
	            
	            try { 
	            	Thread.sleep(1000); 
	            } catch (InterruptedException e) {}
	            System.out.println("Hilo 1: Esperando por el recurso 2...");
	            
	            synchronized (recurso2) {
	               System.out.println("Hilo 1: Utilizando recursos 1 y 2...");
	            }
	         }
	      }
	   }
	 
	   private static class ThreadDeadlock2 extends Thread {
	      public void run() {
	         synchronized (recurso2) {
	            System.out.println("Hilo 2: Utilizando recurso 2...");
	            
	            try { Thread.sleep(1000); }
	            catch (InterruptedException e) {}
	            System.out.println("Hilo 2: Esperando por el recurso 1...");
	            
	            synchronized (recurso1) {
	               System.out.println("Hilo 2: Utilizando recursos 1 y 2...");
	            }
	         }
	      }
	   } 

}
