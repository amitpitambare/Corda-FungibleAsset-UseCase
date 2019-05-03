
package com.asp.cordapp.common.flows;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.flows.FlowException;
import net.corda.core.flows.FlowLogic;
import net.corda.core.flows.FlowSession;
import net.corda.core.internal.ResolveTransactionsFlow;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.utilities.UntrustworthyData;



	public class ReceiveTransactionUnVerifiedFlow extends FlowLogic<SignedTransaction> {

		private final FlowSession otherSideSession;
		public ReceiveTransactionUnVerifiedFlow(FlowSession otherSideSession) {
		
			this.otherSideSession = otherSideSession;
		}
		/*
		 * @Suspendable
		 * 
		 * @Throws(AttachmentResolutionException::class,
		 * TransactionResolutionException::class) override fun call(): SignedTransaction
		 * { val stx = otherSideSession.receive<SignedTransaction>().unwrap {
		 * subFlow(ResolveTransactionsFlow(it, otherSideSession)) it } return stx } }
		 */
		@Override
		@Suspendable
		public SignedTransaction call() throws FlowException {
			UntrustworthyData<SignedTransaction> stx = otherSideSession.receive(SignedTransaction.class);
			SignedTransaction signTrax=stx.unwrap(data -> {
				subFlow(new ResolveTransactionsFlow(data,otherSideSession));
			   return data;
			});
			return signTrax;
		}

		
	}

