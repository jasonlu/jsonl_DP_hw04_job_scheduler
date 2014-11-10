package edu.bu.jsonl;

import java.util.Observable;
import java.util.Observer;

public class Connection implements Observer {

	private Server server = null;
	
	public Connection(Server s) throws Exception {
		System.out.println("Connecting to server:" + s );
		if( s.connect() )
			this.server = s;
		else {
			throw new Exception();
		}
	}
	
	public Boolean sendCommand(Job job) {
		System.out.println("Sending command: " + job.command + " to server: "
				+ server );
		server.execute(job.command);
		server.takeCPUCapacity(job.CPURequirement);
		server.takeMEMCapacity(job.MEMRequirement);
		return true;
	}
	
	public String getStatus() {
		return server.getStatus();
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

	public int getCPUCapacity() {
		return server.getCPUCapacity();
	}
	
	public int getMEMCapacity() {
		return server.getMEMCapacity();
	}
}