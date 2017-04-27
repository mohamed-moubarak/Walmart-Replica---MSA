package controller;

import java.util.*;

import com.codesnippets4all.json.parsers.JSONParser;
import com.codesnippets4all.json.parsers.JsonParserFactory;

import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;

public class RequestParser implements Runnable {

	protected ParseListener _parseListener;
	protected ClientHandle _clientHandle;

	public RequestParser(ParseListener parseListener, ClientHandle clientHandle) {
		_parseListener = parseListener;
		_clientHandle = clientHandle;
	}

	public void run() {
		try {
			HttpRequest request = _clientHandle.getRequest();

			HttpPostRequestDecoder postDecoder;

			if (request.method().compareTo(HttpMethod.POST) == 0) {
				postDecoder = new HttpPostRequestDecoder(request);

				System.out.println("REQ : " + request);

				List<InterfaceHttpData> lst = postDecoder.getBodyHttpDatas();

				System.out.println("body DATAS: " + postDecoder.getBodyHttpDatas());

				String str = lst.get(0).toString();
				System.out.println("THIS : " + str);
				String jsonStr = str.substring(str.indexOf('{'));

				JsonParserFactory factory = JsonParserFactory.getInstance();

				JSONParser parser = factory.newJsonParser();

				Map<String, Object> jsonData = parser.parseJson(jsonStr);

				String strAction = jsonData.get("action").toString();

				jsonStr = jsonStr.substring(jsonStr.indexOf('{', 1), jsonStr.length() - 1);

				jsonData = parser.parseJson(jsonStr);

				ClientRequest cr = new ClientRequest(strAction, null, jsonData);

				_parseListener.parsingFinished(_clientHandle, cr);

			} else {
				System.out.println("The request is not a POST request");
			}

		} catch (Exception exp) {
			_parseListener.parsingFailed(_clientHandle, "Exception while parsing JSON object " + exp.toString());
		}
	}

}