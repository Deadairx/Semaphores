// By Michael Arnold
import java.util.*;
import java.io.*;
import java.util.concurrent.Semaphore;

public class main {

	
	public static void main(String[] args) {
		
		Scanner keyboard = new Scanner(System.in);
		
		System.out.println("How many customers would you like to simulate? (please enter int value): ");
		int numCustomers = keyboard.nextInt();
		
		
		//Setup all threads
		Customer[] customers = new Customer[numCustomers];
		agent[] agents = new agent[4];
		frontDesk infoDeskPerson = new frontDesk();
		announcer announcerPerson = new announcer();
		Thread[] customerThreads = new Thread[customers.length];
		Thread[] agentThreads = new Thread[agents.length];
		
		for(int i=0; i < customers.length; i++)
		{
			customers[i] = new Customer(i);
		}
		for(int i=0; i < agents.length; i++)
		{
			agents[i] = new agent(i);
		}
		
		//setup all semaphores
		Semaphore[] array1 = new Semaphore[10];
		Semaphore[] array2 = new Semaphore[10];
		Semaphore front = new Semaphore(0, true);
		Semaphore numberGiven = new Semaphore(0, true);
		Semaphore[] announcement = new Semaphore[customers.length];	//One for each customer/ticket number
		Semaphore agentReady = new Semaphore(4, true);
		Queue<Integer> availableAgents = new LinkedList<Integer>();	//Will be used to assign first available agent's number to customer
		Semaphore[] givePaperwork = new Semaphore[4];	//All the below need to be arrays because
		Semaphore[] eyeExam = new Semaphore[4];			//there are 4 agents and the customer needs
		Semaphore[] eyeReady = new Semaphore[4];		//to send the signal to the correct agent
		Semaphore[] photo = new Semaphore[4];
		Semaphore[] photoReady = new Semaphore[4];
		Semaphore[] agentDone = new Semaphore[4];
		Semaphore[] gotLicense = new Semaphore[4];
		Queue<Integer> ticketQ = new LinkedList<Integer>();	//Will be used to assign ticket numbers
		Semaphore customerLeft = new Semaphore(0, true);
		
		
		//initialize semaphore arrays
		for(int i=0; i < array1.length; i++)
		{
			array1[i] = new Semaphore(1, true);
		}
		for(int i=0; i < array2.length; i++)
		{
			array2[i] = new Semaphore(1, true);
		}
		for(int i=0; i < announcement.length; i++)
		{
			announcement[i] = new Semaphore(0, true);
		}
		for(int i=0; i<4; i++)
		{
			givePaperwork[i] = new Semaphore(0, true);
			eyeExam[i] = new Semaphore(0, true);
			eyeReady[i] = new Semaphore(0, true);
			photo[i] = new Semaphore(0, true);
			photoReady[i] = new Semaphore(0, true);
			agentDone[i] = new Semaphore(0, true);
			gotLicense[i] = new Semaphore(0, true);
			
		}
		//Initialize Queue with all agents
		for(int i=0; i < agents.length; i++)
		{
			availableAgents.add(i);
			agentReady.release();	//will make agentReady = 4 so first 4 customers can get agents immediately
		}
		
		//Give appropriate semaphores to all threads that need them
		for(int i = 0; i < customers.length; i++) //Customers need every semaphore
		{
			customers[i].addArray1(array1);
			customers[i].addArray2(array2);
			customers[i].addFront(front);
			customers[i].addNumberGiven(numberGiven);
			customers[i].addAnnouncer(announcement);
			customers[i].addAgentReady(agentReady);
			customers[i].addGivePaperwork(givePaperwork);
			customers[i].addEyeExam(eyeExam);
			customers[i].addEyeReady(eyeReady);
			customers[i].addPhoto(photo);
			customers[i].addPhotoReady(photoReady);
			customers[i].addAgentDone(agentDone);
			customers[i].addGotLicense(gotLicense);
			customers[i].addAgentQueue(availableAgents);
			customers[i].addTicketQ(ticketQ);
			customers[i].addCustomerLeft(customerLeft);
		}
		for(int i = 0; i < agents.length; i++)  //All agents
		{
			agents[i].addGivePaperWork(givePaperwork);
			agents[i].addEyeExam(eyeExam);
			agents[i].addEyeReady(eyeReady);
			agents[i].addPhoto(photo);
			agents[i].addPhotoReady(photoReady);
			agents[i].addAgentDone(agentDone);
			agents[i].addGotLicense(gotLicense);
			agents[i].addAgentReady(agentReady);
			agents[i].addQueue(availableAgents);
		}
		//Front Desk person's semaphores
		infoDeskPerson.addFront(front);
		infoDeskPerson.addNumberGiven(numberGiven);
		infoDeskPerson.addTicketQ(ticketQ);
		//Number Announcer's semaphores
		announcerPerson.addEndOfLine(array2[9]);
		announcerPerson.addAnnouncer(announcement);
		
		
		//Run all threads
		for(int i = 0; i < agents.length; i++)
		{
			agentThreads[i] = new Thread(agents[i]);
			agentThreads[i].start();
		}
		Thread infoDeskThread = new Thread(infoDeskPerson);
		infoDeskThread.start();
		
		Thread announcerThread = new Thread(announcerPerson);
		announcerThread.start();
		
		for(int i = 0; i < customers.length; i++)
		{
			customerThreads[i] = new Thread(customers[i]);
			customerThreads[i].start();
		}
		
		
		
		for(int i=0; i<customers.length; i++) //waits for each customer to signal they've left
		{
			try {
				customerLeft.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//Once all customers left, time to exit program.
		System.out.println("All customers left. Closing store.");
		
		
		infoDeskPerson.signalStop();
		
		try {
			infoDeskThread.join();		//join infoDesk person
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		

		announcerPerson.signalStop();
		
		try {
			announcerThread.join();		//join announcer thread
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		infoDeskPerson.signalStop();
		announcerPerson.signalStop();
		
		for(int i = 0; i < agents.length; i++)
		{

			agents[i].signalStop();
			
			try {
				agentThreads[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			agentThreads[i].interrupt();		//just in case
		}


		
		
		for(int i = 0; i < customers.length; i++)
		{

			try {
				customerThreads[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			customerThreads[i].interrupt();	//just in case
		}
		infoDeskThread.interrupt();
		announcerThread.interrupt();
		
		
	}

}
