package com.asp.cordapp.seller.flows;



import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.asp.corda.contracts.AssetTransferContract;
import com.asp.corda.states.AssetState;
import com.asp.corda.states.AssetTransferState;
import com.asp.corda.states.RequestStatus;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.contracts.Command;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.flows.CollectSignaturesFlow;
import net.corda.core.flows.FinalityFlow;
import net.corda.core.flows.FlowException;
import net.corda.core.flows.FlowLogic;
import net.corda.core.flows.FlowSession;
import net.corda.core.flows.InitiatingFlow;
import net.corda.core.flows.StartableByRPC;
import net.corda.core.identity.Party;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import net.corda.core.utilities.ProgressTracker;
@StartableByRPC
@InitiatingFlow
public class AssetTransferRequestInitiateFlow extends FlowLogic<SignedTransaction> {
	

	private final String assetCode;
	
	private final Party assetBuyer;
	 private final ProgressTracker progressTracker = new ProgressTracker();
	 @Override
	    public ProgressTracker getProgressTracker() {
	        return progressTracker;
	    }
	 
	public AssetTransferRequestInitiateFlow(String assetCode, Party assetBuyer) {
		//super();
		this.assetCode = assetCode;
		this.assetBuyer = assetBuyer;
	}

	@Override
	@Suspendable
	public SignedTransaction call() throws FlowException {
		// TODO Auto-generated method stub
		//subFlow(new SwapIdentitiesFlow(assetBuyer));
		List<StateAndRef<AssetState>> assetStateAndRefs=getServiceHub().getVaultService().queryBy(AssetState.class).getStates();
		 List<StateAndRef<AssetState>> assetStateandRef=assetStateAndRefs.stream().filter(e-> e.getState().getData().getAssetCode().equals(assetCode)).collect(Collectors.toList());	
		AssetState asset= assetStateandRef.get(0).getState().getData();
		
		
		//CommandAndState cmdSate= asset.withNewOwner(assetBuyer);
		 AssetTransferState assetTransfer = new AssetTransferState(asset, getOurIdentity(), assetBuyer,RequestStatus.PENDING_CONFIRMATION,new UniqueIdentifier());
		
		 Command command = new Command<>(new AssetTransferContract.Commands.CreateRequest(), getOurIdentity().getOwningKey());
		 
		 Party notary =getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0);
		 
		 TransactionBuilder txBuilder = new TransactionBuilder(notary)
	    	        .addOutputState(assetTransfer, AssetTransferContract.ASSETTRANSFER_CONTRACT_ID)
	    	        .addCommand(command);
		
		 
		 SignedTransaction stx=getServiceHub().signInitialTransaction(txBuilder);
		 
		 FlowSession otherPartySession = initiateFlow(assetBuyer);
		 
		 SignedTransaction fullySignedTx = subFlow(new CollectSignaturesFlow(
				 stx, Arrays.asList(otherPartySession), CollectSignaturesFlow.tracker()));
		 
		 SignedTransaction signedTx=subFlow(new FinalityFlow(fullySignedTx));
		 return signedTx;
		 
		 
	}
	
	
}
