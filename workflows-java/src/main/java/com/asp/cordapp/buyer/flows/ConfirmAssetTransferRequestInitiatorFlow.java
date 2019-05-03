package com.asp.cordapp.buyer.flows;

import java.security.SignatureException;
import java.util.Arrays;
import java.util.List;

import com.asp.corda.contracts.AssetTransferContract;
import com.asp.corda.states.AssetTransferState;
import com.asp.cordapp.common.flows.ReceiveTransactionUnVerifiedFlow;
import com.google.common.collect.ImmutableList;

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
import net.corda.core.flows.SendTransactionFlow;
import net.corda.core.flows.StartableByRPC;
import net.corda.core.identity.Party;
import net.corda.core.node.services.Vault;
import net.corda.core.node.services.vault.QueryCriteria;
import net.corda.core.transactions.LedgerTransaction;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import net.corda.core.utilities.ProgressTracker;
import net.corda.finance.contracts.asset.Cash;

@StartableByRPC
@InitiatingFlow
public class ConfirmAssetTransferRequestInitiatorFlow extends FlowLogic<SignedTransaction> {

	UniqueIdentifier linearID;
	 private final ProgressTracker progressTracker = new ProgressTracker();
	 @Override
	    public ProgressTracker getProgressTracker() {
	        return progressTracker;
	    }
	 
	public ConfirmAssetTransferRequestInitiatorFlow(UniqueIdentifier linearID) {
		// super();
		this.linearID = linearID;
	}

	@Override
	@Suspendable
	public SignedTransaction call() throws FlowException {
		// TODO Auto-generated method stub

		QueryCriteria criteria = new QueryCriteria.LinearStateQueryCriteria(null, ImmutableList.of(linearID),
				Vault.StateStatus.UNCONSUMED, null);
		// getServiceHub().loadState(linearID,AssetTransferState.class)

		List<StateAndRef<AssetTransferState>> inpuData = getServiceHub().getVaultService()
				.queryBy(AssetTransferState.class, criteria).getStates();

		AssetTransferState assetTransfer = inpuData.get(0).getState().getData();

		Command command = new Command<>(new AssetTransferContract.Commands.SettleRequest(),
				getOurIdentity().getOwningKey());

		Party notary = getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0);

		TransactionBuilder txBuilder = new TransactionBuilder(notary).addInputState(inpuData.get(0))
				.addOutputState(assetTransfer, AssetTransferContract.ASSETTRANSFER_CONTRACT_ID).addCommand(command);

		
		//Send Asset Transfer Confirmation to Seller 
		SignedTransaction tempPtx = getServiceHub().signInitialTransaction(txBuilder);
		FlowSession securitySellerSession = initiateFlow(assetTransfer.getAssetSeller());
		subFlow(new SendTransactionFlow(securitySellerSession, tempPtx));
		SignedTransaction assetPTx = subFlow(new ReceiveTransactionUnVerifiedFlow(securitySellerSession));

		//Soft lock Cash 
		
		/* Cash.generateSpend(getServiceHub(), tx, payments, onlyFromParties) */
		
		LedgerTransaction assetLtx = null;
		try {
			assetLtx = assetPTx.toLedgerTransaction(getServiceHub(),false);
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		assetLtx.getInputs().forEach(it -> txBuilder.addInputState(it));

		assetLtx.getOutputs().forEach(it -> txBuilder.addOutputState(it));

		assetLtx.getCommands().forEach(it -> txBuilder.addCommand(new Command(it.getValue(), it.getSigners())));
		
	/*	subFlow(new IdentitySyncFlow.Send(securitySellerSession,
				txBuilder.toWireTransaction(getServiceHub())));
		*/
		SignedTransaction Fstx=getServiceHub().signInitialTransaction(txBuilder, getOurIdentity().getOwningKey());
		
		 SignedTransaction fullySignedTx = subFlow(new CollectSignaturesFlow(
				 Fstx, Arrays.asList(securitySellerSession), CollectSignaturesFlow.tracker()));
		 SignedTransaction signedTx=subFlow(new FinalityFlow(fullySignedTx));
		 return signedTx;
	}

}
