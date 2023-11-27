package org.data_chain.database;

public class AdsbRequestData {

	public int requestCount;
	public int resetDate;
	public boolean resetOccurred;

	public AdsbRequestData(int requestCount, int resetDate, boolean resetOccurred) {
		this.requestCount = requestCount;
		this.resetDate = resetDate;
		this.resetOccurred = resetOccurred;
	}
}