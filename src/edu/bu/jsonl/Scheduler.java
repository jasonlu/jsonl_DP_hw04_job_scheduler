package edu.bu.jsonl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

public class Scheduler implements Runnable, Observer { 
	
	private Boolean keepRunning = true;
	ArrayList<Server> servers;
	LinkedList<Job> jobs = new LinkedList<Job>();
	FakeTime fakeTime;

	public Scheduler() {
		servers = new ArrayList<Server>();
		fakeTime = FakeTime.getInstance();
	}
	
	public Scheduler(ArrayList<Server> servers) {
		this.servers = servers;
		fakeTime = FakeTime.getInstance();
		
	}
	
	public void addJob(Job job) {
		jobs.add(job);		
	}
	
	Boolean idle = true;
	public void executeJob() {
		// jobs is a queue, implemented by linked list. 
		Job job = jobs.peek();
		if(job == null) {
			if(!idle) {
				System.out.println("No job in queue...");
			}
			idle = true;
			return;
		}
		System.out.println(jobs.size() +  " jobs in queue.");
		idle = false;
		long intTime = job.startTime.getTime() / 1000;
		if(intTime <= fakeTime.getTime()) {
			System.out.println("Scheduled time reached.");
			System.out.println("Finding available server.");
			for(Server s : servers) {
				// Connect to each server to find server status.
				Connection conn = connectToServer(s);
				System.out.println("Job: " + job);
				// Find server status via conn object.
				if(conn.getStatus() == "completed") {
					// Check if server has enough capacity to do the job.
					if(job.getCPURequirement() <= conn.getCPUCapacity() && 
					   job.getMEMRequirement() <= conn.getMEMCapacity()) {
						// Send command via conn object.
						conn.sendCommand(job);
						// Remove job from queue.
						jobs.remove();
						
						/*
						 * 
						 *  Now this flow will cause halt if the job requires too many resources that no any server can provide.
						 *  If so... what do I do?
						 *  
						 *  1) Remove that job.
						 *  2) Keep that job in queue but skip to next job.
						 *  
						 *  
						 */						
						break;
					}
				}
			}
		}
	}
	
	private Connection connectToServer(Server s) {
		Connection conn;
		try {
			conn = new Connection(s);
		} catch (Exception e) {
			e.printStackTrace();
			conn = null;
		}
		return conn;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(keepRunning) {
			executeJob();
			try {
				Thread.sleep(100);
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		executeJob();
		
	}

}
