package org.isf.commons;

import java.util.ArrayList;
import java.util.List;

public class ExceptionQueue extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1177624248474352370L;
	
	private List<Throwable> queue = new ArrayList<Throwable>();

	public ExceptionQueue(String message) {
		super(message);
	}
	
	public ExceptionQueue(Throwable t) {
		super(t);
	}
	
	public ExceptionQueue(String message, Throwable t) {
		super(message, t);
	}
	
	public void append(Throwable t) {
		queue.add(t);
	}
	
	public int queueSize() { return queue.size(); }
	
	public Throwable get(int index) { return queue.get(index); }
	
	public boolean hasErrors() { return queueSize() > 0; }
	
	public String getMessage() {
		String msg = super.getMessage()+": "+(getCause() != null ? getCause().getMessage() : "");
		if (hasErrors()) {
			msg += "\r\n";
			for (Throwable t : queue)
				msg += t.getMessage()+"\r\n";
		}
		return msg;
	}
	
}
