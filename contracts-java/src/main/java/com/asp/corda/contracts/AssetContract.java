package com.asp.corda.contracts;


import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.CommandWithParties;
import net.corda.core.contracts.Contract;
import net.corda.core.contracts.ContractState;

import static net.corda.core.contracts.ContractsDSL.requireSingleCommand;

import java.util.List;

import com.asp.corda.states.AssetState;

import static net.corda.core.contracts.ContractsDSL.requireThat;
import net.corda.core.contracts.Command;
import net.corda.core.transactions.LedgerTransaction;



public class AssetContract implements Contract {

	
	 public static final String ASSET_CONTRACT_ID = "com.asp.corda.contracts.AssetContract";
	 
	 
	 
	@Override
	public void verify(LedgerTransaction tx) throws IllegalArgumentException {
		
	 final CommandWithParties<Commands> commandWithParties =requireSingleCommand(tx.getCommands(), Commands.class);
	 List listOFSigner=commandWithParties.getSigners();
	 
	 
	 Commands command=commandWithParties.getValue();
	 if(command instanceof Commands.Create){
		 verifyCreate(tx,listOFSigner);
	 } 
	 if(command instanceof Commands.Transfer) {
		 verifyTransfer(tx,listOFSigner);
	 }
		
		
    
		
	}
	
	 private void verifyTransfer(LedgerTransaction tx, List listOFSigner) {
		// TODO Auto-generated method stub
		 List<AssetState> inpStaes=tx.inputsOfType(AssetState.class);
		 
		 
		 requireThat(require ->{
			 require.using("There is must be only one input Asset" , inpStaes.size()==1);
			
			 return require;
		 });
		
	
	}

	private void verifyCreate(LedgerTransaction tx, List listOFSigner) {
		// TODO Auto-generated method stub
		
		requireThat(require -> {
           
            require.using("No inputs should be consumed when issuing an Asset.", !tx.getInputs().isEmpty());
            require.using("There should be one output state of type AssetState", tx.getOutputStates().size() == 1);
            ContractState outputState = tx.getOutput(0);

            AssetState assetState=(AssetState)outputState;
            assetState.getOwner().getOwningKey();
            require.using(" Ouput must be a AssetState", outputState instanceof AssetState);
            require.using("Only Seller can issue contract",listOFSigner.contains(assetState.getOwner().getOwningKey()) );
            
            return require;
        });
		}

	public interface Commands extends CommandData {
	        class Transfer implements Commands {}
	        class Create implements Commands {}
	    }

}
