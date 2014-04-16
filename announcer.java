// By Michael Arnold
import java.util.*;
import java.io.*;
import java.util.concurrent.Semaphore;

public class announcer implements Runnable {

	Semaphore myEndOfLine;
	Semaphore[] myAnnouncement;
	private int nextTicketNum = 0;
	private boolean continueRunning = true;

	@Override
	public void run()
	{
		while(continueRunning)
		{
			wait(myEndOfLine);
			//10th spot is empty. Call another number to fill the spot!
			if(continueRunning)
				System.out.println("Announcer: ticket number " + nextTicketNum + " may now enter line 2." );
			if(nextTicketNum < myAnnouncement.length)
			{
				signal(myAnnouncement[nextTicketNum]);
			}
			
			nextTicketNum++;
			
		}
		
		return;
		
		
	}

	public void addEndOfLine(Semaphore array2EndOfLine)
	{
		myEndOfLine = array2EndOfLine;
	}

	public void addAnnouncer(Semaphore[] announcement)
	{
		myAnnouncement = announcement;
	}
	
	
	public void signalStop()
	{
		continueRunning = false;
		
		//need to signal semaphores to allow waiting threads to end loops
		signal(myEndOfLine);
	}
	
	
	
	//Simplifies pseudocode to actual code
	public void wait(Semaphore s)
	{
		try {
			s.acquire();
		} catch (InterruptedException e) {
			System.out.println("Announcer crashed in a wait() method.");
			e.printStackTrace();
		}
	}
	
	//Simplifies pseudocode to actual code
	public void signal(Semaphore s)
	{
		s.release();
	}
	

}
