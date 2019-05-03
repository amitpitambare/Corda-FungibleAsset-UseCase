package com.asp.cordapp.buyer.flows;

import com.asp.cordaapp.seller.flows.AssetTransferRequestInitiateFlow;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.flows.FlowException;
import net.corda.core.flows.FlowLogic;
import net.corda.core.flows.FlowSession;
import net.corda.core.flows.InitiatedBy;
import net.corda.core.flows.SignTransactionFlow;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.utilities.ProgressTracker;

@InitiatedBy(AssetTransferRequestInitiateFlow.class)
public class CreateAssetTransferRequestReponderFlow extends FlowLogic<SignedTransaction> {

	private final FlowSession otherPartySession;

	public CreateAssetTransferRequestReponderFlow(FlowSession otherPartySession) {
		this.otherPartySession = otherPartySession;
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
		SignedTransaction signedTx = subFlow(
				new SignTxFlow(otherPartySession, SignTransactionFlow.Companion.tracker()));
		return waitForLedgerCommit(signedTx.getId());
}
}
