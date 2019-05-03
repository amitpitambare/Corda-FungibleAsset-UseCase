package com.asp.cordapp.seller.flows;

import java.security.PublicKey;
import java.security.SignatureException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.asp.corda.contracts.AssetContract;
import com.asp.corda.states.AssetState;
import com.asp.corda.states.AssetTransferState;
import com.asp.cordapp.buyer.flows.ConfirmAssetTransferRequestInitiatorFlow;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.contracts.CommandAndState;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.flows.FlowException;
import net.corda.core.flows.FlowLogic;
import net.corda.core.flows.FlowSession;
import net.corda.core.flows.InitiatedBy;
import net.corda.core.flows.ReceiveTransactionFlow;
import net.corda.core.flows.SendTransactionFlow;
import net.corda.core.flows.SignTransactionFlow;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import net.corda.core.node.StatesToRecord;
import net.corda.core.transactions.LedgerTransaction;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import net.corda.core.utilities.ProgressTracker;

@InitiatedBy(ConfirmAssetTransferRequestInitiatorFlow.class)
public class ConfirmAssetTransferResponderFlow extends FlowLogic<SignedTransaction>{
	
	private final FlowSession otherSideSession;
	
	

	public ConfirmAssetTransferResponderFlow(FlowSession otherSideSession) {
		
		this.otherSideSession = otherSideSession;
	}



	@Override
	@Suspendable
	public SignedTransaction call() throws FlowException {
		
		class SignTxFlow extends SignTransactionFlow {
			private SignTxFlow(FlowSession otherPartySession, ProgressTracker progressTracker) {
				super(otherPartySession, progressTracker);
			}

			@Override
			protected void checkTransaction(SignedTransaction stx) throws FlowException {
              /*  requireThat(require -> {
                    ContractState output = stx.getTx().getOutputs().get(0).getData();
                    require.using("This must be an IOU transaction.", output instanceof AssetTransferState);
                    
                    return null;
                });*/
            }
		
	}
		SignedTransaction recvdTx=subFlow(new ReceiveTransactionFlow(otherSideSession, false, StatesToRecord.NONE));
	
		LedgerTransaction ledTx=null;
		try {
			ledTx=recvdTx.toLedgerTransaction(getServiceHub(), false);
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	Optional<AssetTransferState> assetTransferState=ledTx.getInputStates().stream().filter(e -> e instanceof AssetTransferState).map(AssetTransferState.class::cast).findFirst();
	String assetCode=assetTransferState.get().getAsset().getAssetCode();
	AbstractParty buyerParty=assetTransferState.get().getAssetBuyer();
		List<StateAndRef<AssetState>> assetStateAndRefs=getServiceHub().getVaultService().queryBy(AssetState.class).getStates();
		 List<StateAndRef<AssetState>> assetStateandRef=assetStateAndRefs.stream().filter(e-> e.getState().getData().getAssetCode().equals(assetCode)).collect(Collectors.toList());	
		
		CommandAndState cmdState= assetStateandRef.get(0).getState().getData().withNewOwner(buyerParty);
		List<PublicKey> requiredSigners = Arrays.asList(getOurIdentity().getOwningKey());

		Party notary = getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0);
		TransactionBuilder txBuilder = new TransactionBuilder(notary).addInputState(assetStateandRef.get(0))
				.addOutputState(cmdState.getOwnableState(), AssetContract.ASSET_CONTRACT_ID).addCommand(cmdState.getCommand(),requiredSigners);
		
		SignedTransaction tempPtx = getServiceHub().signInitialTransaction(txBuilder);
		
		   subFlow(new SendTransactionFlow(otherSideSession, tempPtx));
		   
		   SignedTransaction stx = subFlow(new SignTxFlow(otherSideSession, SignTransactionFlow.Companion.tracker()));
		 return waitForLedgerCommit(stx.getId());
		
		
	
		//asset.withNewOwner(newOwner)
	}

}
