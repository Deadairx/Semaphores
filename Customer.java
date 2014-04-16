// By Michael Arnold
import java.util.*;
import java.io.*;
import java.util.concurrent.Semaphore;

public class Customer implements Runnable {
	
	private int myThreadNum;
	private int myNum;
	private int myAgentNum;
	Semaphore[] myArray1;
	Semaphore[] myArray2;
	Semaphore myFront;
	Semaphore myNumberGiven;
	Semaphore[] myAnnouncement;
	Semaphore myAgentReady;
	Queue<Integer> myAvailableAgents;
	Semaphore[] myGivePaperwork;
	Semaphore[] myEyeExam;
	Semaphore[] myEyeReady;
	Semaphore[] myPhoto;
	Semaphore[] myPhotoReady;
	Semaphore[] myAgentDone;
	Semaphore[] myGotLicense;
	Queue<Integer> myTicketQ;
	Semaphore myCustomerLeft;
	
	
	public Customer(int i)
	{
		myThreadNum = i;
	}
	
	
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void run()
	{
		wait(myArray1[9]);	//waiting to enter building (aka 10th spot in first line)
		
		System.out.println("Customer " + myThreadNum + " enters the DMV.");
		
		for(int i=9; i > 0; i--)
		{
			wait(myArray1[i-1]);
			//moving forward in line			
			signal(myArray1[i]);	//Signals person behind to move forward
		}
		//Now at front of first line
		System.out.println("Customer " + myThreadNum + " is now at the front of the first line.");
		signal(myFront);
		
		wait(myNumberGiven);
		//Save number via setNum() method below
		myNum = myTicketQ.poll(); //Gets number from Queue. Guaranteed because next person can't get number until signal below
		System.out.println("Customer " + myThreadNum + ": I got number " + myNum + ".");
		signal(myArray1[0]);
		System.out.println("Customer " + myThreadNum + " is now in the waiting room.");
		
		wait(myAnnouncement[myNum]);
		//Number was called. Get in second line
		System.out.println("Customer " + myThreadNum + "'s number " + myNum + " was called. Moving to second line.");
		
		for(int i=9; i > 0; i--)
		{
			wait(myArray2[i-1]);
			//moving foward in line
			signal(myArray2[i]);
		}
		
		wait(myAgentReady);
		
		while( myAvailableAgents.peek() == null)
		{
			
		}
		
		myAgentNum = (int)(myAvailableAgents.poll());	//Gives customer the number of the first available agent
		
		System.out.println("Customer " + myThreadNum + " is being served by an Agent.");
		signal(myArray2[0]);	//Moved out of line. Lets line move forward.
		signal(myGivePaperwork[myAgentNum]);
		wait(myEyeExam[myAgentNum]);
		System.out.println("Customer " + myThreadNum + " is now ready for the eye exam.");
		signal(myEyeReady[myAgentNum]);
		wait(myPhoto[myAgentNum]);
		System.out.println("Customer " + myThreadNum + " is now ready for a picture.");
		signal(myPhotoReady[myAgentNum]);
		wait(myAgentDone[myAgentNum]);
		System.out.println("Customer " + myThreadNum + ": I just got my license. Time to get out of here.");
		signal(myGotLicense[myAgentNum]);
		
		System.out.println("Customer " + myThreadNum + " just left the DMV");
		signal(myCustomerLeft);
		
		
		return;
	}
	
	public void setNum(int num)
	{
		myNum = num;
	}
	
	public void setAgentNum(int num)
	{
		myAgentNum = num;
	}
	
	
	public void addArray1(Semaphore[] array1)
	{
		myArray1 = array1;
	}

	public void addArray2(Semaphore[] array2)
	{
		myArray2 = array2;
	}

	public void addFront(Semaphore front)
	{
		myFront = front;
	}

	public void addNumberGiven(Semaphore numberGiven)
	{
		myNumberGiven = numberGiven;
	}

	public void addAnnouncer(Semaphore[] announcement)
	{
		myAnnouncement = announcement;
	}

	public void addAgentReady(Semaphore agentReady)
	{
		myAgentReady = agentReady;
	}
	
	public void addAgentQueue(Queue<Integer> q)
	{
		myAvailableAgents = q;
	}

	public void addGivePaperwork(Semaphore[] givePaperwork)
	{
		myGivePaperwork = givePaperwork;
	}

	public void addEyeExam(Semaphore[] eyeExam)
	{
		myEyeExam = eyeExam;
	}

	public void addEyeReady(Semaphore[] eyeReady)
	{
		myEyeReady = eyeReady;
	}

	public void addPhotoReady(Semaphore[] photoReady)
	{
		myPhotoReady = photoReady;
	}

	public void addGotLicense(Semaphore[] gotLicense)
	{
		myGotLicense = gotLicense;
	}

	public void addPhoto(Semaphore[] photo)
	{
		myPhoto = photo;
	}

	public void addAgentDone(Semaphore[] agentDone)
	{
		myAgentDone = agentDone;
	}
	
	public void addTicketQ(Queue<Integer> ticketQ) 
	{
		myTicketQ = ticketQ;
	}
	
	public void addCustomerLeft(Semaphore customerLeft)
	{
		myCustomerLeft = customerLeft;
	}
	
	
	public int getNum()
	{
		return myNum;
	}
	
	
	
	
	
	
	//Simplifies psuedocode to actual code
	public void wait(Semaphore s)
	{
		try {
			s.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.out.println("Customer " + myNum + " crashed in a wait() method.");
			e.printStackTrace();
		}
	}
	
	//Simplifies psuedocode to actual code
	public void signal(Semaphore s)
	{
		s.release();
	}

	
	
}
