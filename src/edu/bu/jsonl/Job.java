package edu.bu.jsonl;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class Job {
	String uuid;
	String name;
	String command;
	Date startTime;
	int CPURequirement;
	int MEMRequirement;
	String attributes;
	
	
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
	 * @return the command
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * @param command the command to set
	 */
	public void setCommand(String command) {
		this.command = command;
	}

	/**
	 * @return the startTime
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the cPURequirement
	 */
	public int getCPURequirement() {
		return CPURequirement;
	}

	/**
	 * @param cPURequirement the cPURequirement to set
	 */
	public void setCPURequirement(int cPURequirement) {
		CPURequirement = cPURequirement;
	}

	/**
	 * @return the mEMRequirement
	 */
	public int getMEMRequirement() {
		return MEMRequirement;
	}

	/**
	 * @param mEMRequirement the mEMRequirement to set
	 */
	public void setMEMRequirement(int mEMRequirement) {
		MEMRequirement = mEMRequirement;
	}

	/**
	 * @return the attributes
	 */
	public String getAttributes() {
		return attributes;
	}

	/**
	 * @param attributes the attributes to set
	 */
	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}

	@Override
	public String toString() {
		return name + ": " + command + ", CPU: " + CPURequirement + " MEM: " + MEMRequirement; 
	}

	public Job() {
		uuid = UUID.randomUUID().toString();
		this.startTime = new Date();
	}
	
	public Job(String command) {
		this();
		this.command = command;
	}
	
	public Job(HashMap<String, Object> params) {
		this();
		name = (String)params.get("name");
		command = (String)params.get("command");
		startTime = (Date)params.get("startTime");
		CPURequirement = (int)params.get("CPURequirement");
		MEMRequirement = (int)params.get("MEMRequirement");
		attributes = (String)params.get("attributes");
	}

}
