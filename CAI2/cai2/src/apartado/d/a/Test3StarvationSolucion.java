package apartado.d.a;

public class Test3StarvationSolucion {
	
	public static void main(String[] args) {
		 Trabajador trabajador = new Trabajador();
		 
	        for (int i = 0; i < 10; i++) {
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
	            try {
	                wait(10000); // hacemos esperar al hilo para que los dem�s tengan posibilidad de acceder al recurso (trabajar)
	            } catch (InterruptedException e) {}
	            
	        }
	    }
	}
	


}
