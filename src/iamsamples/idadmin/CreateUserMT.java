package iamsamples.idadmin;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CreateUserMT {


    public static void main(String[] args) throws Exception {
    	
    	BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();
		ThreadPoolExecutor tpe = new ThreadPoolExecutor(5, 5, 0, TimeUnit.SECONDS, workQueue, Executors.defaultThreadFactory());
		
		for (int i=0 ; i<100; i++) {
			final String userLogin = "DHONI" + i;
			Runnable r = new OIMServerThread(userLogin);
			tpe.execute(r);
		}		
		
		tpe.shutdown();
		
		if (!tpe.awaitTermination(60, TimeUnit.SECONDS)) {
			tpe.shutdownNow();
		}
		    	
        System.exit(0);
    }


}
