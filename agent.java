// By Michael Arnold
import java.util.*;
import java.io.*;
import java.util.concurrent.Semaphore;

public class agent implements Runnable{

	Semaphore[] myGivePaperwork;
	Semaphore[] myEyeExam;
	Semaphore[] myPhoto;
	Semaphore[] myEyeReady;
	Semaphore[] myPhotoReady;
	Semaphore[] myAgentDone;
	Semaphore[] myGotLicense;
	Queue<Integer> myAvailableAgents;
	Semaphore myAgentReady;
	
	private int myNum;
	private boolean continueRunning = true;
	
	
	public agent(int num)
	{
		myNum = num;
	}
	

	@Override
	public void run()
	{
		
		while(continueRunning)
		{
			wait(myGivePaperwork[myNum]);
			if(continueRunning)
				System.out.println("Agent " + myNum + " is processing paperwork.");
			
			signal(myEyeExam[myNum]);
			wait(myEyeReady[myNum]);
			if(continueRunning)
				System.out.println("Agent " + myNum + " is giving an eye exam.");
			
			signal(myPhoto[myNum]);
			wait(myPhotoReady[myNum]);
			if(continueRunning)
			{
				System.out.println("Agent " + myNum + " is taking photo.");
				
				System.out.println("Agent " + myNum + " gave temporary license.");
			}
			signal(myAgentDone[myNum]);
			
			wait(myGotLicense[myNum]);
			
			myAvailableAgents.add((Integer)myNum);	//puts agent number in Queue for next customer to know where to go
			signal(myAgentReady);			//signal for first in line customer to .pop() the queue
			
			
		}
		
		return;
		
	}

	public void addGivePaperWork(Semaphore[] givePaperwork)
	{
		myGivePaperwork = givePaperwork;
	}

	public void addEyeExam(Semaphore[] eyeExam)
	{
		myEyeExam = eyeExam;
	}

	public void addPhoto(Semaphore[] photo)
	{
		myPhoto = photo;
	}

	public void addEyeReady(Semaphore[] eyeReady)
	{
		myEyeReady = eyeReady;
	}

	public void addPhotoReady(Semaphore[] photoReady)
	{
		myPhotoReady = photoReady;
	}

	public void addAgentDone(Semaphore[] agentDone)
	{
		myAgentDone = agentDone;
	}

	public void addGotLicense(Semaphore[] gotLicense)
	{
		myGotLicense = gotLicense;
	}

	public void addAgentReady(Semaphore agentReady)
	{
		myAgentReady = agentReady;
	}

	public void addQueue(Queue<Integer> availableAgents)
	{
		myAvailableAgents = availableAgents;
	}
	
	
	
	//Simplifies psuedocode to actual code
	public void wait(Semaphore s)
	{
		try {
			s.acquire();
		} catch (InterruptedException e) {
			System.out.println("Agent " + myNum + " crashed in a wait() method.");
			e.printStackTrace();
		}
	}
	
	//Simplifies psuedocode to actual code
	public void signal(Semaphore s)
	{
		s.release();
	}


	public void signalStop()
	{
		continueRunning = false;
		
		
		//need to signal all semaphores in order for waiting threads to loop again and end.
		signal(myGivePaperwork[myNum]);
		
		signal(myEyeReady[myNum]);
		signal(myPhotoReady[myNum]);
		
		signal(myGotLicense[myNum]);
		
	}
	

}
