package walmart.core;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Statement;



public abstract class Command implements Runnable {
    protected Connection dbConn;
    protected CallableStatement proc;
    protected Statement query;


    public abstract void execute();

    @Override
    public void run() {
        execute();
    }
}
