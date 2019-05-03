package com.asp.cordapp.seller.flows;

import java.util.Currency;

import com.asp.corda.contracts.AssetContract;
import com.asp.corda.states.AssetState;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.contracts.Amount;
import net.corda.core.contracts.Command;
import net.corda.core.flows.CollectSignaturesFlow;
import net.corda.core.flows.FinalityFlow;
import net.corda.core.flows.FlowException;
import net.corda.core.flows.FlowLogic;
import net.corda.core.flows.InitiatingFlow;
import net.corda.core.flows.StartableByRPC;
import net.corda.core.identity.Party;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import net.corda.core.utilities.ProgressTracker;
import net.corda.core.utilities.ProgressTracker.Step;


@InitiatingFlow
@StartableByRPC
public class CreateAssetFlow extends FlowLogic<Void> {



	private final String assetName;
	 private final Amount<Currency> purchaseCost;
	 private final String assetCode;
	 
	 
	 private final ProgressTracker progressTracker1 = new ProgressTracker();
	 @Override
	    public ProgressTracker getProgressTracker() {
	        return progressTracker;
	    }
	 
	 private final Step GENERATING_TRANSACTION = new Step("Generating transaction based on new IOU.");
     private final Step VERIFYING_TRANSACTION = new Step("Verifying contract constraints.");
     private final Step SIGNING_TRANSACTION = new Step("Signing transaction with our private key.");
     private final Step GATHERING_SIGS = new Step("Gathering the counterparty's signature.") {
         @Override
         public ProgressTracker childProgressTracker() {
             return CollectSignaturesFlow.Companion.tracker();
         }
     };
     private final Step FINALISING_TRANSACTION = new Step("Obtaining notary signature and recording transaction.") {
         @Override
         public ProgressTracker childProgressTracker() {
             return FinalityFlow.Companion.tracker();
         }
     };

     // The progress tracker checkpoints each stage of the flow and outputs the specified messages when each
     // checkpoint is reached in the code. See the 'progressTracker.currentStep' expressions within the call()
     // function.
     private final ProgressTracker progressTracker = new ProgressTracker(
             GENERATING_TRANSACTION,
             VERIFYING_TRANSACTION,
             SIGNING_TRANSACTION,
             GATHERING_SIGS,
             FINALISING_TRANSACTION
     );

	 
		public CreateAssetFlow(String assetName, Amount<Currency> purchaseCost, String assetCode) {
			
			this.assetName = assetName;
			this.purchaseCost = purchaseCost;
			this.assetCode = assetCode;
		}
	 
		 

	@Override
	@Suspendable
	public Void call() throws FlowException {
		// TODO Auto-generated method stub
		
		
		Party notary= getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0);
		
		AssetState asset= new AssetState(getOurIdentity(), assetName, purchaseCost, assetCode);
		
		Command command = new Command<>(new AssetContract.Commands.Create(), getOurIdentity().getOwningKey());
		  progressTracker.setCurrentStep(GENERATING_TRANSACTION);
    	// We create a transaction builder and add the components.
    	TransactionBuilder txBuilder = new TransactionBuilder(notary)
    	        .addOutputState(asset, AssetContract.ASSET_CONTRACT_ID)
    	        .addCommand(command);
    	
    	 progressTracker.setCurrentStep(SIGNING_TRANSACTION);
    	 SignedTransaction signTRx=getServiceHub().signInitialTransaction(txBuilder);
    	 progressTracker.setCurrentStep(FINALISING_TRANSACTION);
    	 subFlow(new FinalityFlow(signTRx));
		return null;
	}

	
}
