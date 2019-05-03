package com.asp;

import static net.corda.core.utilities.NetworkHostAndPort.parse;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.corda.client.rpc.CordaRPCClient;
import net.corda.core.messaging.CordaRPCOps;
import net.corda.core.node.NodeInfo;
import net.corda.core.utilities.NetworkHostAndPort;

/**
 * Connects to a Corda node via RPC and performs RPC operations on the node.
 *
 * The RPC connection is configured using command line arguments.
 */
public class Client {
    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // Create an RPC connection to the node.
      //  if (args.length != 3) throw new IllegalArgumentException("Usage: Client <node address> <rpc username> <rpc password>");
        final NetworkHostAndPort nodeAddress = parse("localhost:10013");
        final String rpcUsername = "user1";
        final String rpcPassword = "test";
        final CordaRPCClient client = new CordaRPCClient(nodeAddress);
        final CordaRPCOps proxy = client.start(rpcUsername, rpcPassword).getProxy();

        // Interact with the node.
        // For example, here we print the nodes on the network.
        final List<NodeInfo> nodes = proxy.networkMapSnapshot();
		/*
		 * 
		 * CordaX500Name name= new CordaX500Name("GSK", "GSK","US", "US"); Set<Party>
		 * liParty=proxy.partiesFromName("GSK", false); final Party otherParty =
		 * liParty.iterator().next(); final SignedTransaction signedTx = proxy
		 * .startTrackedFlowDynamic(AddValidatedPatientRecordsFlow.Initiator.class,
		 * patientDetails, otherParty) .getReturnValue() .get(); logger.info("{}",
		 * nodes);
		 */
    }
}