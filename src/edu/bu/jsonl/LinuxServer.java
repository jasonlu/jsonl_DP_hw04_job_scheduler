package edu.bu.jsonl;

public class LinuxServer extends Server {

	public LinuxServer() {
		// TODO Auto-generated constructor stub
	}
	
	public LinuxServer(String name, String ip, int CPUCapacity, int MEMCapacity) {
		super(name, ip, CPUCapacity, MEMCapacity);
	}
	
	@Override
	public String getStatus() {
		System.out.println("Getting status of Linux server...");
		return status;
	}

	@Override
	public Boolean connect() {
		System.out.println("Linux server connected by ssh.");
		return true;
	}

	@Override
	public Boolean execute(String command) {
		System.out.println("Linux server executing command: " + command + "...");
		return true;
	}
}
