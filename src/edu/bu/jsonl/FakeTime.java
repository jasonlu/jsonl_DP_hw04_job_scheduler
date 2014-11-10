/**
 * 
 */
package edu.bu.jsonl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;
import java.util.Observable;

/**
 * @author Jason Lu
 * The singleton class to store fake system time.
 *
 */
public class FakeTime extends Observable implements Runnable {
	
	private static FakeTime self;
	
	/**
	 * The fake system time stored in integer
	 */
	private long time;
	
	/**
	 * The fake system time stored in Date
	 */
	private Date date;
	
	/**
	 * The lapse each tick take.
	 */
	private int lapse;
	
	/**
	 * The interval between tick.
	 */
	private int interval;

	private boolean stop, ticking;
	
	/**
	 * 
	 */
	private FakeTime() {
		this.date = new Date();
		this.time = date.getTime() / 1000;
		this.lapse = 30;
		this.interval = 10;
		this.stop = false;
		this.ticking = false;
		System.out.println("Now:" + getTimeStr());
	}
	
	public void run() {
		if(this.ticking) {
			return;
		}
		this.ticking = true;
		
		//self = getInstance();
		boolean notified = false;
		while(!this.stop) {
			//System.out.println("Time:" + getTimeStr());
			time += lapse;
			notified = false;
			if(!notified) {
				setChanged();
				notifyObservers();
				notified = true;
				clearChanged();
			}
			
			try {
				Thread.sleep(interval);
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void stop() {
		this.stop = true;
		this.ticking = false;
	}
	
	public static synchronized FakeTime getInstance() {
		if(self == null) {
			self = new FakeTime();
		}
		return self;
	}
	
	/**
	 * The lapse each tick take.
	 */
	public void setLapse(int newLapse) {
		
		this.lapse = newLapse;
	}
	
	/**
	 * The interval between tick.
	 */
	public void setInterval(int newInterval) {
		this.interval = newInterval;
	}
	
	public long getTime() {
		return time;
	}
	
	public void setTime(long newTime) {
		time = newTime;
	}
	
	public String getTimeStr() {
		SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss"); 
		date.setTime(time * 1000);
		//System.out.println("getTimeStr(int): " + time);
		//System.out.println("getTimeStr: " + date);
		return ft.format(date);
	}
	
	public void addTime(int howMany) {
		time += howMany;
	}
	

}
