package com.asp.corda.contracts;

import com.asp.corda.contracts.AssetContract.Commands;

import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.Contract;
import net.corda.core.transactions.LedgerTransaction;

public class AssetTransferContract implements Contract {
	
	 public static final String ASSETTRANSFER_CONTRACT_ID = "com.asp.corda.contracts.AssetTransferContract";

	@Override
	public void verify(LedgerTransaction arg0) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}
	
	 public interface Commands extends CommandData {
	        class CreateRequest implements Commands {}
	        class ConfirmRequest implements Commands {}
	        class SettleRequest implements Commands {}
	    }


}
