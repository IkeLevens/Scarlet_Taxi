package edu.rutgers.cs.scarletTaxi.notification_exporter.importer;
import java.util.List;

import edu.rutgers.cs.scarletTaxi.centralDataStoratge.CentralDataStorage;
import edu.rutgers.cs.scarletTaxi.centralDataStoratge.PasswordProtector;
import edu.rutgers.cs.scarletTaxi.centralDataStoratge.RequestNotification;
import edu.rutgers.cs.scarletTaxi.centralDataStoratge.RideNotification;
import edu.rutgers.cs.scarletTaxi.notification_exporter.process.NotificationProcessor;

/**
 * This class is the public interface for starting the notification module.
 * @author Michael Wasserman
 * 
 */

public class Importer implements Runnable{

	private List<RideNotification> rideNotifications=null;
	private List<RequestNotification> requestNotifications=null;
	private boolean running = true;
	public static final int INTERVAL = PasswordProtector.INTERVAL; //The standard sleep interval for this is 15 minutes
	// (900,000 ms)
	
	/**
	 * This is the implementation of the Runnable.run() method, which is called where <thread>.start()
	 * is called on an instance of this class.  This method checks for all ride and request
	 * notifications once every INTERVAL milliseconds, then calls for NotificationProccessor to
	 * process each notification it found queued.
	 */
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