import edu.rutgers.cs.scarletTaxi.cancellation.CancelHandler;
import edu.rutgers.cs.scarletTaxi.notification_exporter.importer.Importer;

/**
 * This class will spin off threads for our Daemon server processes then join with one to wait for
 * an InterruptedException when the time comes to shut down gracefully.
 * @author Isaac Yochelson
 *
 */
public class ScarletTaxi {
	public static void main (String[] argv) {
		Importer importer = new Importer();
		CancelHandler cancelH = new CancelHandler("imap.gmail.com","scarlettaxicancellation@gmail.com","cs431group5","[Gmail]/All Mail",900000);
		Thread importerThread = new Thread(importer);
		Thread cancelThread = new Thread(cancelH);
		cancelThread.start();
		importerThread.start();
		//add more thread starts here
		try {
			importerThread.join();
		} catch (InterruptedException e) {
			importerThread.interrupt();
			cancelThread.interrupt();
			// add an interrupt for each added thread here.
		}
	}
}
