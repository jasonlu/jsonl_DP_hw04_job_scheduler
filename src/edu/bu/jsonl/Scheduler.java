package edu.bu.jsonl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Observable;
import java.util.Observer;

public class Scheduler implements Observer { 
	
	private Boolean keepRunning = true;
	ArrayList<Server> servers;
	ArrayList<Job> jobs = new ArrayList<Job>();
	FakeTime fakeTime;

	public Scheduler() {
		servers = new ArrayList<Server>();
		fakeTime = FakeTime.getInstance();
		fakeTime.addObserver(this);
	}
	
	public Scheduler(ArrayList<Server> servers) {
		this.servers = servers;
		fakeTime = FakeTime.getInstance();
		fakeTime.addObserver(this);
		
	}
	
	private void sortJobs() {
		//Sorting
		Collections.sort(jobs, new Comparator<Job>() {
			@Override
			public int compare(Job  job1, Job  job2) {
				return job1.startTime.compareTo(job2.startTime);
			}
		});
	}
	
	public void addJobs(ArrayList<Job> jobs) {
		this.jobs.addAll(jobs);
		sortJobs();
	}
	
	public void addJob(Job job) {
		jobs.add(job);
		sortJobs();
	}
	
	Boolean idle = true;
	public void executeJob() {
		// jobs is a ArrayList 
		Job job = jobs.get(0);
		if(job == null) {
			if(!idle) {
				System.out.println("No job in queue...");
			}
			idle = true;
			return;
		} else {
			if(!idle) {
				System.out.println(jobs.size() +  " jobs in queue.");
			}
			idle = true;
		}
		
		long intTime = job.startTime.getTime() / 1000;
		if(intTime <= fakeTime.getTime()) {
			idle = false;
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
						jobs.remove(0);
						
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
					} // end if
				} // end if
			} // end for
		} // end if
	} // end method
	
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
	public void update(Observable o, Object arg) {
		executeJob();
		
	}

}
