package edu.bu.jsonl;

public class SolarisServer extends Server {

	public SolarisServer() {
		// TODO Auto-generated constructor stub
	}
	
	public SolarisServer(String name, String ip, int CPUCapacoty, int MEMCapacoty) {
		super(name, ip, CPUCapacoty, MEMCapacoty);
	}
	
	@Override
	public String getStatus() {
		System.out.println("Getting status of Solaris server...");
		return status;
	}

	@Override
	public Boolean connect() {
		System.out.println("Solaris server connected by ssh.");
		return true;
	}

	@Override
	public Boolean execute(String command) {
		System.out.println("Solaris server executing command: " + command + "...");
		return true;
	}
}
