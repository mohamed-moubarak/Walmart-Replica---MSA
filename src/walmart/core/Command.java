package walmart.core;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import walmart.core.MyObjectMapper;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public abstract class Command implements Runnable {
    protected HashMap<String, String> map;
    protected Connection dbConn;
    protected CallableStatement proc;
    protected Statement query;
    protected ResultSet set;
    protected MyObjectMapper mapper = new MyObjectMapper();
    protected JsonNodeFactory nf = JsonNodeFactory.instance;
    protected ObjectNode root = nf.objectNode();
    protected Map<String, String> details = new HashMap<String, String>();




    public abstract void execute();

    public void setMap(HashMap<String, String> map) {
        this.map=map;
    }

    @Override
    public void run() {
        execute();
    }
}