package edu.bu.jsonl;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class Client {

	public Client() {
	}
    
    public ArrayList<Job> loadJobsXML(String xmlFilepath) {
    	ArrayList<Job> jobs = new ArrayList<Job>();
    	File fXmlFile = new File(xmlFilepath);
    	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    	try{
	    	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	    	Document doc = dBuilder.parse(fXmlFile);
	    	doc.getDocumentElement().normalize();
	    	NodeList nodeList = doc.getElementsByTagName("job");
	    	
	    	for (int i = 0; i < nodeList.getLength(); i++) {
	    		Node node = nodeList.item(i);
	    		if (node.getNodeType() == Node.ELEMENT_NODE) {
	    			Element element = (Element) node;
	    			HashMap<String, Object> params = new HashMap<String, Object>();
	    			params.put("name", element.getElementsByTagName("name").item(0).getTextContent());
	    			params.put("command", element.getElementsByTagName("command").item(0).getTextContent());
	    			params.put("startTime", strToDate(element.getElementsByTagName("startTime").item(0).getTextContent()));	    			
	    			params.put("CPURequirement", Integer.parseInt(element.getElementsByTagName("CPURequirement").item(0).getTextContent()));
	    			params.put("MEMRequirement", Integer.parseInt(element.getElementsByTagName("MEMRequirement").item(0).getTextContent()));
	    			params.put("attributes", element.getElementsByTagName("attributes").item(0).getTextContent());
	    			jobs.add(new Job(params));
	    		}
	    	}
    	} catch(Exception e) {
    		System.out.println(e.getMessage());
    	}
    	return jobs;
    }
    
    public ArrayList<Server> loadServersXML() {
    	ArrayList<Server> servers = new ArrayList<Server>();
    	File xmlFile = new File("./servers.xml");
    	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    	try{
	    	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	    	Document doc = dBuilder.parse(xmlFile);
	    	doc.getDocumentElement().normalize();
	    	NodeList nodeList = doc.getElementsByTagName("server");
	    	
	    	
	    	for (int i = 0; i < nodeList.getLength(); i++) {
	    		Node node = nodeList.item(i);
	    		if (node.getNodeType() == Node.ELEMENT_NODE) {
	    			Element element = (Element) node;
	    			String name = element.getElementsByTagName("name").item(0).getTextContent();
	    			String ip = element.getElementsByTagName("ip").item(0).getTextContent();
	    			int CPUCapacity = Integer.parseInt(element.getElementsByTagName("CPUCapacity").item(0).getTextContent());
	    			int MEMCapacity = Integer.parseInt(element.getElementsByTagName("MEMCapacity").item(0).getTextContent());
	    			String type = element.getElementsByTagName("type").item(0).getTextContent();
	    			type = type.toLowerCase();
	    			Server server = null;
	    			switch(type) {
	    			case "windows":
	    				server = new WindowsServer(name, ip, CPUCapacity, MEMCapacity);
	    				break;
	    			case "linux":
	    				server = new LinuxServer(name, ip, CPUCapacity, MEMCapacity);
	    				break;
	    			case "aix":
	    				server = new AIXServer(name, ip, CPUCapacity, MEMCapacity);
    					break;
	    			case "solaris":
	    				server = new SolarisServer(name, ip, CPUCapacity, MEMCapacity);
	    				break;
	    			
	    			}
	    			servers.add(server);
	    		}
	    	}
    	} catch(Exception e) {
    		System.out.println(e.getMessage());
    	}
    	return servers;
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
		
		if(args.length > 0 ) {
			System.out.println("Loading jobs from xml file: " + args[0]);
			File f = new File(args[0]);
			if(f.exists() && !f.isDirectory()) {
				try {
					jobs = client.loadJobsXML(args[0]);
					System.out.println("Job XML file Loaded.");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			System.out.println("usage: java -jar ./scheduler.jar <job_xml_file_path>");
			return;
		}
		System.out.println("Loading servers from xml.");
		servers = client.loadServersXML();
		System.out.println("Servers XML loaded.");
		
		// The Facade Object.
		Scheduler scheduler = new Scheduler(servers);
		// The Facade Method.
		scheduler.addJobs(jobs);

		// Detach fake clock from main thread.
		Thread tick = new Thread(fakeTime);
		// Clock's ticking...
        tick.start();
		
		// Now, I should make a loop to receive user input..
        String command = "";

        while(command != "e" || command != "E") {
        	command = "";
        	Scanner reader = new Scanner(System.in);
        	System.out.println("Please input operation: (e)xit");
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
        	}
        	
        	if(endMe) {
        		reader.close();
        		break;
        	}
        }
        System.out.println("Program ended... Good bye.");
	}
}
