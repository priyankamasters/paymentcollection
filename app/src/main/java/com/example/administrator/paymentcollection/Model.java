package com.example.administrator.paymentcollection;

public class Model {

	private String lDate;
	private String lDescription;
	private String lDebit;
	private String lCredit;

	public Model(String lDate, String lDescription, String lDebit, String lCredit) {
		this.lDate = lDate;
		this.lDescription = lDescription;
		this.lDebit = lDebit;
		this.lCredit = lCredit;
	}

	public String getsNo() {
		return lDate;
	}

	public String getProduct() {
		return lDescription;
	}

	public String getCategory() {
		return lDebit;
	}

	public String getPrice() {
		return lCredit;
	}
}
