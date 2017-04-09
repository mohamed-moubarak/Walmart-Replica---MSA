package walmart.core.commands.admin;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import walmart.core.Command;
import walmart.core.CommandsHelp;
import walmart.core.PostgresConnection;
import walmart.core.User;
import org.json.JSONException;
import org.json.JSONObject;
import org.postgresql.util.PSQLException;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.logging.Logger;

public class GetUsersCommand extends Command implements Runnable {
    private final Logger LOGGER = Logger.getLogger(GetUsersCommand.class.getName());

    @Override
    public void execute() {

        try {
            dbConn = PostgresConnection.getDataSource().getConnection();
            dbConn.setAutoCommit(false);
            proc = dbConn.prepareCall("{? = call get_users(?)}");
            proc.setPoolable(true);
            proc.registerOutParameter(1, Types.OTHER);
            proc.setString(2, map.get("user_substring"));
            proc.execute();

            set = (ResultSet) proc.getObject(1);

            ArrayNode usersArray = nf.arrayNode();
            root.put("app", map.get("app"));
            root.put("method", map.get("method"));
            root.put("status", "ok");
            root.put("code", "200");

            while (set.next()) {
                Integer id = set.getInt(1);
                String email = set.getString(2);
                String firstname = set.getString(3);
                String avatar_url = set.getString(4);

                User user = new User();
                user.setId(id);
                user.setEmail(email);
                user.setFirstname(firstname);
                user.setPicturepath(avatar_url);
               

                usersArray.addPOJO(user);
            }
            set.close();
            proc.close();
            root.set("users", usersArray);
            try {
                CommandsHelp.submit(map.get("app"),mapper.writeValueAsString(root),map.get("correlation_id"), LOGGER);
             
            } catch (JsonGenerationException e) {
                //Logger.log(Level.SEVERE, e.getMessage(), e);
            } catch (JsonMappingException e) {
                //Logger.log(Level.SEVERE, e.getMessage(), e);
            } catch (IOException e) {
                //Logger.log(Level.SEVERE, e.getMessage(), e);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            dbConn.commit();
        } catch (PSQLException e) {
            CommandsHelp.handleError(map.get("app"), map.get("method"), e.getMessage(), map.get("correlation_id"), LOGGER);
            //Logger.log(Level.SEVERE, e.getMessage(), e);
        } catch (SQLException e) {
            CommandsHelp.handleError(map.get("app"), map.get("method"), e.getMessage(), map.get("correlation_id"), LOGGER);
            //Logger.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            PostgresConnection.disconnect(set, proc, dbConn,null);
        }
    }
}