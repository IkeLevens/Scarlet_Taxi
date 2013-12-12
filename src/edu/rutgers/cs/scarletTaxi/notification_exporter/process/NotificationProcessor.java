package edu.rutgers.cs.scarletTaxi.notification_exporter.process;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import edu.rutgers.cs.scarletTaxi.centralDataStoratge.CentralDataStorage;
import edu.rutgers.cs.scarletTaxi.centralDataStoratge.Request;
import edu.rutgers.cs.scarletTaxi.centralDataStoratge.RequestNotification;
import edu.rutgers.cs.scarletTaxi.centralDataStoratge.Ride;
import edu.rutgers.cs.scarletTaxi.centralDataStoratge.RideNotification;
import edu.rutgers.cs.scarletTaxi.centralDataStoratge.User;
import edu.rutgers.cs.scarletTaxi.notification_exporter.export.Exporter;

/**
 * This class is responsible for processing and compiling the information for each individual notification.
 * @author Michael Wasserman
 * 
 */
public class NotificationProcessor {
	
	public static boolean processRideNotification(RideNotification RideN){
		NType type=null;
		User[] recipients;
		User driver=null;
		String text;
		String time;
		Ride ride = CentralDataStorage.getRide(RideN.rideID);
		driver = ride.car.driver;
		Notification n = null;
		switch (RideN.notificationType){
			case 'C': //process ride cancellation notification
				type=NType.RideCancel;
				recipients = CentralDataStorage.getUsers(RideN.rideID);
				time = ""+ride.departure.getHours()+":"+ride.departure.getMinutes();
				text = "Scarlet Taxi Notification(Ride Cancellation):\n" +
						driver.username + " has canceled the " + time + " ride to " + ride.destination.locationName + "!\n" +
								"Please check the rides list for available rides that fit your neccessary criteria, or plan" +
								"alternate travel accomodations. We apologize for any inconvenience, and thank you for using" +
								"Scarlet Taxi.";
				//replace the user's phone number with an sms email for each of the notification recipients
				for(int i=0;i<recipients.length;i++){
					recipients[i].mobileNumber = compileSMSEmail(recipients[i]);
					CentralDataStorage.removeRideRequest(recipients[i].userID,RideN.rideID);
				}
				n = new Notification(type,recipients,text);
				Exporter.exportMultipleNotifications(n);
				break;
			case 'T': //process pickup time reminder notification
				type=NType.TimeReminder;
				recipients = CentralDataStorage.getUsers(RideN.rideID);
				TimeZone tz = TimeZone.getTimeZone("America/New_York");
				long mins = (Math.abs(ride.departure.getHours() - (Calendar.getInstance(tz).HOUR_OF_DAY+3))*60) + Math.abs(ride.departure.getMinutes() - Calendar.getInstance(tz).MINUTE);
				time = ""+ride.departure.getHours()+":"+ride.departure.getMinutes();
				text = "Scarlet Taxi Notification(Pickup Time Reminder):\n" +
						"Your pickup for the " + time + " ride to " + ride.destination.locationName + " will be arriving in " +
						mins + " minutes!"
						+"Thank you for using Scarlet Taxi.";
				//replace the user's phone number with an sms email for each of the notification recipients
				for(int i=0;i<recipients.length;i++){
					recipients[i].mobileNumber = compileSMSEmail(recipients[i]);
				}
				n = new Notification(type,recipients,text);
				Exporter.exportMultipleNotifications(n);
				break;
		}
		//if notification type indicator was not represented with a valid character.
		if(type==null){
			return false;
		}
		return true;
	}
	
	public static boolean processRequestNotification(RequestNotification RequestN){
		Notification n = null;
		NType type = null;
		User recipient;
		User rider;
		User driver;
		String text;
		String time;
		Request request = CentralDataStorage.getRequest(RequestN.requestID);
		Ride ride = request.ride;
		switch (RequestN.notificationType){
			case 'R':
				type=NType.NewRequest;
				recipient = ride.car.driver;
				rider = request.passenger;
				time = ""+ride.departure.getHours()+":"+ride.departure.getMinutes();
				text = "Scarlet Taxi Notification(New Request):\n" +
						rider.username + " has requested a spot in your " + time + " ride to " + ride.destination.locationName + ".\n" +
						"Thank you for using Scarlet Taxi.";
				//replace user's phone number with a sms email address
				if(recipient.receiveSMSNOtification==true){
					recipient.mobileNumber = compileSMSEmail(recipient);
				}
				n = new Notification(type,recipient,text);
				Exporter.exportNotification(n);
				break;
			case 'C': //process request cancellation notification
				type=NType.RequestCancel;
				recipient = ride.car.driver;
				rider = request.passenger;
				time = ""+ride.departure.getHours()+":"+ride.departure.getMinutes();
				text = "Scarlet Taxi Notification(Request Cancellation):\n" +
						rider.username + " has cancelled his/her request for a spot in your " + time + " ride to " + ride.destination.locationName + ".\n" +
						"Thank you for using Scarlet Taxi.";
				//replace user's phone number with a sms email address
				if(recipient.receiveSMSNOtification==true){
					recipient.mobileNumber = compileSMSEmail(recipient);
				}
				n = new Notification(type,recipient,text);
				Exporter.exportNotification(n);
				break;
			case 'A': //process request approval notification
				type=NType.RequestApproval;
				recipient=request.passenger;
				driver = ride.car.driver;
				time = ""+ride.departure.getHours()+":"+ride.departure.getMinutes();
				text = "Scarlet Taxi Notification(Request Approval):\n" +
						driver.username + " has approved your request for a spot in his/her " + time + " ride to " + ride.destination.locationName + "!\n" +
						"Thank you for using Scarlet Taxi.";
				//replace user's phone number with a sms email address
				if(recipient.receiveSMSNOtification==true){
					recipient.mobileNumber = compileSMSEmail(recipient);
				}
				n = new Notification(type,recipient,text);
				Exporter.exportNotification(n);
				break;
			case 'D': //process request denial notification
				type=NType.RequestDenial;
				recipient=request.passenger;
				driver = ride.car.driver;
				time = ""+ride.departure.getHours()+":"+ride.departure.getMinutes();
				text = "Scarlet Taxi Notification(Request Approval):\n" +
						"Sorry, but " + driver.username + " has denied your request for a spot in his/her " + time + " ride to " + ride.destination.locationName + ".\n" +
						"Thank you for using Scarlet Taxi.";
				//replace user's phone number with a sms email address
				if(recipient.receiveSMSNOtification==true){
					recipient.mobileNumber = compileSMSEmail(recipient);
				}
				n = new Notification(type,recipient,text);
				Exporter.exportNotification(n);
				break;
		}
		//if notification type indicator was not represented with a valid character.
		if(type==null){
			return false;
		}
		return true;
	}
	/*private Notification cancelRide(RideNotification rn){
		Notification n = null;
		User[] recipients = null;
		return n;
	}*/
	/**
	 * Generates an email address String to send an SMS message to user.
	 * @param u An abstraction of a user.
	 * @return A string which is the email address which will sent an SMS message to user.
	 */
	private static String compileSMSEmail(User u){
		 final String verizon = "@vtext.com";
		 final String att = "@txt.att.net";
		 final String tMobile = "@tmomail.net";
		 final String sprint = "@sprintpcs.com";
		 final String vMobile = "@vmobl.com";
		 String smsEmail = null;
		 switch (u.carrier) {
		 	case 'V':
		 		smsEmail = u.mobileNumber + verizon;
		 		break;
		 	case 'T':
		 		smsEmail = "1" + u.mobileNumber + tMobile;
		 		break;
		 	case 'S':
		 		smsEmail = u.mobileNumber + sprint;
		 		break;
		 	case 'A':
		 		smsEmail = u.mobileNumber + att;
		 		break;
		 	case 'M':
		 		smsEmail = u.mobileNumber + vMobile;
		 		break;
		 	case 'N':
		 		return null;
		 			 }
		return smsEmail;
	}
}