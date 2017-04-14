
public class MQManager {

	public static String[] message;
	public static String[] apps = {"user","home","transaction","search","admin","messages"};
	public static void main(String[] args) throws Exception {
		for (int i = 0; i< apps.length; i++){
			new EmitLog(apps[i]+"Requests");
			new ReceiveLogs(apps[i]+"Replies");
		}
		
		

	}
}
