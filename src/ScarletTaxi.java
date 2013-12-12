import edu.rutgers.cs.scarletTaxi.cancellation.CancelHandler;
import edu.rutgers.cs.scarletTaxi.notification_exporter.importer.Importer;

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
		CancelHandler cancelH = new CancelHandler("imap.gmail.com","scarlettaxicancellation@gmail.com","cs431group5","[Gmail]/All Mail",900000);
		Thread importerThread = new Thread(importer);
		Thread cancelThread = new Thread(cancelH);
		cancelThread.start(); // This thread retrieves incoming emails (including those delivered by
		// SMS) and, if they include the keyword cancel, and are from a recognized source, then the
		// next ride of the sender will be canceled, and notifications sent to requesting passengers.
		importerThread.start(); // This thread checks for queued notificatioins and sends them out
		// by email or SMS as specified in the users' account settings.
		//add more thread starts here if necessary.
		try {
			importerThread.join();
		} catch (InterruptedException e) {
			importerThread.interrupt();
			cancelThread.interrupt();
			// add an interrupt for each added thread here if necessary.
		}
	}
}
