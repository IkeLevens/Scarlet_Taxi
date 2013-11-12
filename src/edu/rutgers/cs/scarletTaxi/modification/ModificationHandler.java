package edu.rutgers.cs.scarletTaxi.modification;
import edu.rutgers.cs.scarletTaxi.centralDataStorage.*;

import java.util.ArrayList;

/** This class handles the modification of the account settings for a User. 
 * 	@author Andy Krohg 
 * 
 */

public class ModificationHandler 
{
	private User user;
	
	/** Constructor for a ModificationHandler object to be used for changing account
	 * settings. 
	 * 
	 * @param user
	 */
	public ModificationHandler(int userID)
	{
		this.user = CentralDataStorage.getUser(userID);
	}
	
	/** Change the username of the specified user.
	 * 
	 * @param newName the new username
	 * @return true if the update saved successfully
	 */
	public boolean changeUsername(String newName)
	{
		user.name = newName;
		return CentralDataStorage.modifyUser(user.userID, user);
	}
	
	/** Change the password of the specified user.
	 * 
	 * @param oldPassword the old password
	 * @param newPassword the new password
	 * @return true if the update saved successfully
	 */
	public boolean changePassword(byte[] oldPassword, byte[] newPassword)
	{
		if (!user.password.equals(oldPassword))
			return false;
		user.password = newPassword;
		return CentralDataStorage.modifyUser(user.userID, user);
	}
	
	/** Add a car to the user's profile.
	 * 
	 * @param make the make of the car
	 * @param bodyStyle the body style of the car
	 * @param color the color of the car
	 * @param seats the number of seats in the car
	 * @return true if the update saved successfully
	 */
	public boolean addCar(final String make, final String bodyStyle, final String color, final int seats)
	{
		return CentralDataStorage.addCar(new Car(this.user, make, bodyStyle, color, seats));
	}
	
	/** Change the alert settings of the specified user.
	 * 
	 * @param email the specification for email notifications
	 * @param text the specification for text notifications
	 * @return true if the update saved successfully
	 */
	public boolean changeAlerts(boolean email, boolean text)
	{
		user.receiveEmailNotification = email;
		user.receiveSMSSnotification = text;
		return CentralDataStorage.modifyUser(user.userID, user);
	}
	
	/** Change the email address of the specified user.
	 * 
	 * @param newEmail the new email address
	 * @return true if the update saved successfully
	 */
	public boolean changeEmail(String newEmail)
	{
		user.email = newEmail;
		return CentralDataStorage.modifyUser(user.userID, user);
	}
	
	/** Change the mobile number of the specified user.
	 * 
	 * @param newNumber the new mobile number
	 * @return true if the update saved successfully
	 */
	public boolean changeNumber(String newNumber)
	{
		user.mobileNumber = newNumber;
		return CentralDataStorage.modifyUser(user.userID, user);
	}
}
