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

	private List<RideNotification> rideNotifications=null;
	private List<RequestNotification> requestNotifications=null;
	private boolean running = true;
	public static final int INTERVAL = 900000; //The standard sleep interval for this is 15 minutes
	// (900,000 ms)
	
	public void run() {
		while (running) {
			this.rideNotifications = CentralDataStorage.getRideNotifications();
			this.requestNotifications = CentralDataStorage.getRequestNotifications();
			
			//Send all pending ride notifications one by one to the processor
		   for(RideNotification rideN : this.rideNotifications){
			   if(rideN != null){
				   if(NotificationProcessor.processRideNotification(rideN)){
					   // is there meant to be code here?
				   }
			   }
			
		   }
		   
		   //Send all pending request notifications one by one to the processor
		   for(RequestNotification requestN : this.requestNotifications) {
			   if(requestN != null){
				   if(NotificationProcessor.processRequestNotification(requestN)){
					   // is there meant to be code here?
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
