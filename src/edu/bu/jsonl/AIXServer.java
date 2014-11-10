package edu.bu.jsonl;

public class AIXServer extends Server {

	

	public AIXServer() {
		// TODO Auto-generated constructor stub
	}

	public AIXServer(String name, String ip, int CPUCapacity, int MEMCapacity) {
		super(name, ip, CPUCapacity, MEMCapacity);
	}
	
	@Override
	public String getStatus() {
		System.out.println("Getting status of AIX server...");
		return status;
	}

	@Override
	public Boolean connect() {
		System.out.println("AIX server connected by ssh.");
		return true;
	}

	@Override
	public Boolean execute(String command) {
		System.out.println("AIX server executing command: " + command + "...");
		Thread cmdThread = new Thread(this);
		cmdThread.start();
		status = "running";
		setChanged();
		notifyObservers();
		clearChanged();
		return true;
	}

	

}
