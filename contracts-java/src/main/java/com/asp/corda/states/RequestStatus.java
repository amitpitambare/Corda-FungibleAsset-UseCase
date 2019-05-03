package com.asp.corda.states;

import net.corda.core.serialization.CordaSerializable;

@CordaSerializable
public enum RequestStatus {
	 PENDING_CONFIRMATION("Pending Confirmation"), //Initial status
	    PENDING("Pending"), // updated by buyer
	    TRANSFERRED("Transferred"), // on valid asset data clearing house update this status
	    REJECTED("Rejected"), // on invalid asset data clearing house reject transaction with this status.
	    FAILED("Failed") ;// on fail of settlement e.g. with insufficient cash from Buyer party.
	    
	    
	   String status ;
	
	private RequestStatus(String status) 
    { 
        this.status = status; 
    } 

	public String getStatus() {
		return status;
	}

	
	
	
	    
}
