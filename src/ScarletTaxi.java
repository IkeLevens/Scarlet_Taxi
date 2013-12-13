import edu.rutgers.cs.scarletTaxi.cancellation.CancelHandler;
import edu.rutgers.cs.scarletTaxi.centralDataStoratge.PasswordProtector;
import edu.rutgers.cs.scarletTaxi.notification_exporter.importer.Importer;
import edu.rutgers.cs.scarletTaxi.removal.Remover;

/**
 * This class will spin off threads for our Daemon server processes then join with one to wait for
 * an InterruptedException when the time comes to shut down gracefully.
 * @author Isaac Yochelson
 *
 */
public class ScarletTaxi {
	/**
	 * The entry point method to the project.
	 * @param argv An array of Strings of command line arguments.
	 */
	public static void main (String[] argv) {
		Importer importer = new Importer();
		CancelHandler cancelH = new CancelHandler(PasswordProtector.MAIL_HOST,PasswordProtector.MAIL_USER,PasswordProtector.MAIL_PASSWORD,PasswordProtector.MAILBOX,PasswordProtector.INTERVAL);
		Thread importerThread = new Thread(importer);
		Thread cancelThread = new Thread(cancelH);
		Thread removalThread = new Thread(new Remover());
		cancelThread.start(); // This thread retrieves incoming emails (including those delivered by
		// SMS) and, if they include the keyword cancel, and are from a recognized source, then the
		// next ride of the sender will be canceled, and notifications sent to requesting passengers.
		importerThread.start(); // This thread checks for queued notificatioins and sends them out
		// by email or SMS as specified in the users' account settings.
		removalThread.start(); // This thread removes will, every INTERVAL (set in PasswordProtector)
		// millisceconds delete all rides (and requests for those rides) which have occurred in the
		// last INTERVAL milliseconds.
		//add more thread starts here if necessary.
		try {
			importerThread.join();
		} catch (InterruptedException e) {
			importerThread.interrupt();
			cancelThread.interrupt();
			removalThread.interrupt();
			// add an interrupt for each added thread here if necessary.
		}
	}
}
