
import java.util.*;
import java.util.concurrent.*;

//import org.boon.json.*;

import io.netty.channel.ChannelHandlerContext;

public class Controller implements ParseListener {

	public Dispatcher _dispatcher;
	protected ExecutorService _threadPoolParsers;

	public Controller() {
	}

	public void init() throws Exception {
		_dispatcher = new Dispatcher();
		_dispatcher.init();
		_threadPoolParsers = Executors.newFixedThreadPool(10);
	}

	public void execRequest(ClientHandle clientHandle) {

		System.out.println("execRequest");

		_threadPoolParsers.execute(new RequestParser(this, clientHandle));

		System.out.println("execRequest after");
	}

	public synchronized void parsingFinished(ClientHandle clientHandle, ClientRequest clientRequest) {
		try {
			String strAction;
			strAction = clientRequest.getAction();

			System.out.println(strAction);

			if (strAction.equalsIgnoreCase("sendMessage") || strAction.equalsIgnoreCase("fetchMessages")
					|| strAction.equalsIgnoreCase("retrieveUnclaimedMessages")) {
				System.out.println("dispatchrequest");
				_dispatcher.dispatchRequest(clientHandle, clientRequest);
			} else {
				String strSessionID;
				strSessionID = clientRequest.getSessionID();
				if (strSessionID == null || strSessionID.length() == 0 || !Cache.sessionExists(strSessionID)) {
					clientHandle.terminateClientRequest();

				} else {
					_dispatcher.dispatchRequest(clientHandle, clientRequest);
				}
			}
		} catch (Exception exp) {
			clientHandle.terminateClientRequest();
			System.err.println(exp.toString());
		}
	}

	public synchronized void parsingFailed(ClientHandle clientHandle, String strError) {
		clientHandle.terminateClientRequest();
		System.err.println("An error in parsing " + strError);
	}

}