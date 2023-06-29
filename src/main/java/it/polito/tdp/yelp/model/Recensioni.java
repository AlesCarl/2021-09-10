package it.polito.tdp.yelp.model;

public class Recensioni {
	
	String businessID; 
	double avgRecensioni;
	
	
	public Recensioni(String businessID, double avgRecensioni) {
		this.businessID = businessID;
		this.avgRecensioni = avgRecensioni;
	}

	public String getBusinessID() {
		return businessID;
	}

	public void setBusinessID(String businessID) {
		this.businessID = businessID;
	}

	public double getAvgRecensioni() {
		return avgRecensioni;
	}

	public void setAvgRecensioni(double avgRecensioni) {
		this.avgRecensioni = avgRecensioni;
	} 
	
	

}
