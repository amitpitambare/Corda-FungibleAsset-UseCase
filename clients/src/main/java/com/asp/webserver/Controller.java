
package com.asp.webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.corda.core.messaging.CordaRPCOps;
import net.corda.core.serialization.CordaSerializable;

/**
 * Define your API endpoints here.
 */
@RestController
@CordaSerializable
@RequestMapping("/") // The paths for HTTP requests are relative to this base path.
public class Controller {

	
	private final static Logger logger = LoggerFactory.getLogger(Controller.class);
	private final CordaRPCOps proxy;
	

	public Controller(NodeRPCConnection rpc) {
		this.proxy = rpc.proxy;
	}
}
