
import java.io.ObjectOutputStream.PutField;
import java.util.*;
import java.util.Map.Entry;

//import org.boon.json.*;
//
//import static org.boon.Boon.fromJson;
//import static org.boon.Boon.puts;
//import static org.boon.Boon.toJson;

import com.json.*;
import com.json.parsers.JSONParser;
import com.json.parsers.JsonParserFactory;

import io.netty.handler.codec.http.multipart.MixedAttribute;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;

public class RequestParser implements Runnable {

	protected ParseListener _parseListener;
	protected ClientHandle _clientHandle;

	public RequestParser(ParseListener parseListener, ClientHandle clientHandle) {
		_parseListener = parseListener;
		_clientHandle = clientHandle;
	}

	// public ClientRequest parseJSON(String jsonStr) {
	//
	// ClientRequest cr;
	// String strAction, strSessionID;
	// Map<String, Object> mapRequestData;
	//
	// strAction = "addUser";
	// strSessionID = "sasdas";
	// // mapRequestData= new M
	//
	// cr = new ClientRequest(strAction, strSessionID, mapRequestData);
	//
	// }

	public void run() {
		try {
			System.out.println("Hello");
			// clientHandle
			String str = _clientHandle._serviceHandler.lst.get(0).toString();
			System.out.println(str);
			String jsonStr = str.substring(str.indexOf('{'));
			System.out.println(jsonStr);

			JsonParserFactory factory = JsonParserFactory.getInstance();

			JSONParser parser = factory.newJsonParser();

			Map<String, Object> jsonData = parser.parseJson(jsonStr);

			String strAction = jsonData.get("action").toString();

			jsonStr = jsonStr.substring(jsonStr.indexOf('{', 1), jsonStr.length() - 1);
			// jsonStr = jsonStr.substring(jsonStr.indexOf('{'));
			System.out.println("User request : " + jsonStr);

			jsonData = parser.parseJson(jsonStr);

			System.out.println(jsonData);

			ClientRequest cr = new ClientRequest(strAction, "sad", jsonData);

			System.out.println(" 1 " + cr._strAction);
			System.out.println(" 2 " + cr._strSessionID);
			System.out.println(" 3 " + cr._mapRequestData);

			// System.out.println(toJson(jsonStr));

			// System.out.println(cr._strAction);

			// System.out.println(fromJson(_clientHandle._httpRequest.toString()));
			// Map<String, Object> _mapRequestData = new Map<String, Object>();
			// ClientRequest cr = new ClientRequest("addUser", "23123", null);
			_parseListener.parsingFinished(_clientHandle, cr);
		} catch (Exception exp) {
			_parseListener.parsingFailed(_clientHandle, "Exception while parsing JSON object " + exp.toString());
		}
	}

}