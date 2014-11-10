package edu.bu.jsonl;

import java.util.LinkedList;
import java.util.Observable;
import java.util.UUID;

public abstract class Server extends Observable implements Runnable{
	
	protected int CPUCapacity;
	protected int MEMCapacity;
	protected int CPUMaxCapacity;
	protected int MEMMaxCapacity;
	//protected Job currentJob;
	protected String type;
	protected String name;
	protected String id;
	protected String ip;
	protected String status;
	
	/**
	 * @return the cPUCapacity
	 */
	public int getCPUCapacity() {
		return CPUCapacity;
	}


	/**
	 * @param cPUCapacity the cPUCapacity to set
	 */
	public void setCPUCapacity(int cPUCapacity) {
		CPUCapacity = cPUCapacity;
	}


	/**
	 * @return the MEMCapacity
	 */
	public int getMEMCapacity() {
		return MEMCapacity;
	}


	/**
	 * @param MEMCapacity the MEMCapacity to set
	 */
	public void setMEMCapacity(int mEMCapacoty) {
		MEMCapacity = mEMCapacoty;
	}


	/**
	 * @return the job
	 
	public Job getJob() {
		return currentJob;
	}
	*/


	/**
	 * @param job the job
	
	public void setJobs(Job job) {
		this.currentJob = job;
	}
	 */

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}


	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}


	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}


	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	
	public void takeCPUCapacity(int howMany) {
		this.CPUCapacity -= howMany;
		if(this.CPUCapacity < 0) {
			this.CPUCapacity = 0;
		}
	}
	
	public void takeMEMCapacity(int howMany) {
		this.MEMCapacity -= howMany;
		if(this.MEMCapacity < 0) {
			this.MEMCapacity = 0;
		}
	}
	
	public void putCPUCapacity(int howMany) {
		this.CPUCapacity += howMany;
		if(this.CPUCapacity > this.CPUMaxCapacity) {
			this.CPUCapacity = this.CPUMaxCapacity;
		}
	}
	
	public void putMEMCapacity(int howMany) {
		this.MEMCapacity += howMany;
		if(this.MEMCapacity > this.MEMMaxCapacity) {
			this.MEMCapacity = this.MEMMaxCapacity;
		}
	}
	
	@Override
	public String toString() {
		return this.name + " (ip: " + this.ip + "), CPUCapacity: " + CPUCapacity + ", MEMCapacity: " + MEMCapacity;
	}


	public Server() {
		this.id = UUID.randomUUID().toString();
		this.status = "completed";
	}
	
	public Server(String name, String ip, int CPUCapacity, int MEMCapacity) {
		this();
		this.name = name;
		this.ip = ip;
		this.CPUCapacity = CPUCapacity;
		this.CPUMaxCapacity = CPUCapacity;
		this.MEMCapacity = MEMCapacity;
		this.MEMMaxCapacity = MEMCapacity;
	}
	
	abstract public String getStatus();
	abstract public Boolean connect();
	abstract public Boolean execute(String command);
	
	@Override
	public void run() {
		while(CPUCapacity < CPUMaxCapacity && MEMCapacity < MEMMaxCapacity) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			putCPUCapacity(1);
			putMEMCapacity(1);
		}
		setChanged();
		notifyObservers();
		clearChanged();
		status = "completed";
	}
	

}
