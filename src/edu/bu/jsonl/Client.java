package edu.bu.jsonl;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

public class Client {

	public Client() {
		// TODO Auto-generated constructor stub
	}
	
	
    public void serializeObjectToXML(String xmlFileLocation, Object objectToSerialize) throws Exception {
        FileOutputStream os = new FileOutputStream(xmlFileLocation);
        XMLEncoder encoder = new XMLEncoder(os);
        encoder.writeObject(objectToSerialize);
        encoder.close();
    }
 
    
    public Object deserializeXMLToObject(String xmlFileLocation) throws Exception {
        FileInputStream os = new FileInputStream(xmlFileLocation);
        XMLDecoder decoder = new XMLDecoder(os);
        Object obj = decoder.readObject();
        decoder.close();
        return obj;
    }
    

    private Date strToDate(String str) {
    	SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss"); 
	    Date date = null; 
	    try { 
	    	date = ft.parse(str); 
	         
	    } catch (ParseException e) { 
	        System.out.println("Unparseable using " + ft);
	        date = new Date();
	    }
	    
	    return date;
    }
	public static void main(String[] args) {
		
		Client client = new Client();
		
		// The fake system time object.
		FakeTime fakeTime = FakeTime.getInstance();
		// Each tick lapse 120 seconds (2 minutes)
		fakeTime.setLapse(120);
		// Tick interval: 0.5 second (real time)
		fakeTime.setInterval(500);
		// Therefore, 1 real second == 4 simulation minutes
		
		ArrayList<Server> servers = new ArrayList<Server>();
		ArrayList<Job> jobs = new ArrayList<Job>();
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		params.put("name", "sync time");
		params.put("command", "cmd_sync_time");
		params.put("startTime", client.strToDate("2014-11-10 01:23:45"));
		
		params.put("CPURequirement", 10);
		params.put("MEMRequirement", 50);
		params.put("attributes", "-v -s -f -R");
		
		jobs.add(new Job(params));
		jobs.add(new Job(params));
		jobs.add(new Job(params));
		
		/*
		try {
			client.serializeObjectToXML("./jobs.xml", jobs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		
		Server windows001 = new WindowsServer("windows001", "10.10.0.100", 100, 200);
		Server linux001 = new LinuxServer("linux001", "10.10.0.101", 120, 250);
		Server solaris001 = new SolarisServer("solaris001", "10.10.0.102", 90, 150);
		Server aix001 = new AIXServer("aix001", "10.10.0.103", 150, 300);
		Server windows002 = new WindowsServer("windows002", "10.10.0.104", 80, 120);
		
		servers.add(windows001);
		servers.add(linux001);
		servers.add(solaris001);
		servers.add(aix001);
		servers.add(windows002);
		
		
		Scheduler scheduler = new Scheduler(servers);
		for(Job j : jobs) {
			scheduler.addJob(j);
		}
		
		
		// Because agent is the observer of fakeTime.
		fakeTime.addObserver(scheduler);
		// Detach fake clock from main thread.
		Thread tick = new Thread(fakeTime);
		// Clock's ticking...
        tick.start();
		
		// Now, I should make a loop to receive user input and place order...
        String command = "";

        while(command != "e" || command != "E") {
        	command = "";
        	String remoteCommand = "";
        	Scanner reader = new Scanner(System.in);
        	System.out.println("Please input operation: (a)dd job or (e)xit");
        	command  = reader.nextLine();
        	System.out.println("Your command:" + command);
        	boolean endMe = false;
        	switch(command) {
        	case "e":
        	case "E":
        		fakeTime.stop();
        		try {
					tick.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		endMe = true;
        		break;
        		
        	case "a":        		
        	case "A":
        		System.out.println("Enter job command:");
            	remoteCommand= reader.nextLine();
            	System.out.println("Enter start time: (YYYY-MM-DD HH:MM:SS");
            	
        		Job job = new Job();
        		scheduler.addJob(job);
        		break;
        	}
        	
        	if(endMe) {
        		reader.close();
        		break;
        	}
        	//System.out.println(fakeTime.getTimeStr() + " order placed...");
        	
        }
        
        System.out.println("Program ended... Good bye.");
		

	}

}
