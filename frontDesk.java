// By Michael Arnold
import java.util.*;
import java.io.*;
import java.util.concurrent.Semaphore;

public class frontDesk implements Runnable{
	
	Semaphore myFront;
	Semaphore myNumberGiven;
	Queue<Integer> myTicketQ;
	private int nextTicketNum = 0;
	private boolean continueRunning = true;
	

	@Override
	public void run()
	{
		while(continueRunning)
		{
			wait(myFront);	//Waits for someone to enter the front of the line
			//put next ticket number in Queue
			myTicketQ.add(nextTicketNum);
			signal(myNumberGiven);
			nextTicketNum++;
		}
		
		return;
		
	}

	public void addFront(Semaphore front)
	{
		myFront = front;
	}

	public void addNumberGiven(Semaphore numberGiven)
	{
		myNumberGiven = numberGiven;
	}
	
	public void addTicketQ(Queue<Integer> ticketQ)
	{
		myTicketQ = ticketQ;
	}
	
	
	public void signalStop()
	{
		continueRunning = false;
		
		//Need to signal the semaphores in order to release the threads waiting on them.
		signal(myFront);
	}
	
	
	
	
	//Simplifies psuedocode to actual code
	public void wait(Semaphore s)
	{
		try {
			s.acquire();
		} catch (InterruptedException e) {
			System.out.println("Front Desk Clerk crashed in a wait() method.");
			e.printStackTrace();
		}
	}
	
	//Simplifies psuedocode to actual code
	public void signal(Semaphore s)
	{
		s.release();
	}
	

}
