package servidor;

import java.util.Random;

public class Carrera implements Runnable{

    public void run() {
    	try {
			aleatorio();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void aleatorio() throws InterruptedException {
    	Random rdm = new Random();
    	int aleatorio = rdm.nextInt(500)+1;
    	synchronized(rdm) {
    		for(int i = aleatorio; i < 500; i++) {
    			Thread.sleep(200);
    		}
    	}
    }
}