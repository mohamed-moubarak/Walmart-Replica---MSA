
import java.util.Map;

public class ClientRequest{

	protected String				_strAction,
									_strSessionID;
	protected Map<String, Object> 	_mapRequestData;
	
	public ClientRequest( String strAction , 
							String strSessionID, 
							Map<String, Object> mapRequestData ){
		_strAction    	=	strAction;
		_strSessionID 	=	strSessionID;   
		_mapRequestData =	mapRequestData;					
	}
	
	public String getAction( ){
		return _strAction;
	}
	
	public String getSessionID( ){
		return _strSessionID;
	}
	
	public Map<String, Object>  getData( ) {
		return _mapRequestData;
	}
	
}