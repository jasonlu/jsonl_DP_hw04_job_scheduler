package edu.bu.jsonl;

public class WindowsServer extends Server {

	
	public WindowsServer(String name, String ip, int CPUCapacoty, int MEMCapacoty) {
		super(name, ip, CPUCapacoty, MEMCapacoty);
	}

	@Override
	public String getStatus() {
		System.out.println("Getting status of Windows server...");
		return status;
	}

	@Override
	public Boolean connect() {
		System.out.println("Window server connected by RDP.");
		return true;
	}
	
	@Override
	public Boolean execute(String command) {
		System.out.println("Windows server executing command: " + command + "...");
		Thread cmdThread = new Thread(this);
		cmdThread.start();
		
		return true;
	}
	

}
