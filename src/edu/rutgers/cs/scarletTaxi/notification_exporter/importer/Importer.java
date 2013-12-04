package edu.rutgers.cs.scarletTaxi.notification_exporter.importer;
import java.util.List;

import edu.rutgers.cs.scarletTaxi.centralDataStoratge.CentralDataStorage;
import edu.rutgers.cs.scarletTaxi.centralDataStoratge.RequestNotification;
import edu.rutgers.cs.scarletTaxi.centralDataStoratge.RideNotification;
import edu.rutgers.cs.scarletTaxi.notification_exporter.process.NotificationProcessor;

/*
 * 
 * @author Michael Wasserman
 * 
 */

public class Importer implements Runnable{

	private boolean running = true;
	public static final int INTERVAL = 900000; //The standard sleep interval for this is 15 minutes
	// (900,000 ms)
	
	public void run() {
		while (running) {
			List<RideNotification> RideNotifications=null;
			List<RequestNotification> RequestNotifications=null;
			RideNotification RideN = null;
			RequestNotification RequestN = null;
			RideNotifications = CentralDataStorage.getRideNotifications();
			RequestNotifications = CentralDataStorage.getRequestNotifications();
			
			//Send all pending ride notifications one by one to the processor
		   for(int i=0;i<RideNotifications.size();i++){
			   RideN = RideNotifications.get(i);
			   if(RideN != null){
				   if(NotificationProcessor.processRideNotification(RideN)){
					   
				   }
			   }
			
		   }
		   
		   //Send all pending request notifications one by one to the processor
		   for(int i=0;i<RequestNotifications.size();i++){
			   RequestN = RequestNotifications.get(i);
			   if(RequestN != null){
				   if(NotificationProcessor.processRequestNotification(RequestN)){
					   
				   }
			   }
		   }
		   try {
			   Thread.sleep(Importer.INTERVAL);
		   } catch (InterruptedException e) {
			   running = false;
		   }
		}
	}
	

}
