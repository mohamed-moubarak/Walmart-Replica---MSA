

package walmart.core;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import walmart.activemq.ActiveMQConfig;
import walmart.activemq.Producer;
import walmart.core.commands.admin.MyObjectMapper;

import java.io.IOException;
import java.util.logging.Logger;

public class CommandsHelp {

    public static void handleError(String app, String method, String errorMsg,
                                   String correlationID, Logger logger) {
        JsonNodeFactory nf = JsonNodeFactory.instance;
        MyObjectMapper mapper = new MyObjectMapper();
        ObjectNode node = nf.objectNode();
        node.put("app", app);
        node.put("method", method);
        node.put("status", "Bad Request");
        node.put("code", "400");
        node.put("message", errorMsg);
        try {
            submit(app, mapper.writeValueAsString(node), correlationID, logger);
        } catch (JsonGenerationException e) {
            //logger.log(Level.SEVERE, e.getMessage(), e);
        } catch (JsonMappingException e) {
            //logger.log(Level.SEVERE, e.getMessage(), e);
        } catch (IOException e) {
            //logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void submit(String app, String json, String correlationID,
                              Logger logger) {
        Producer p = new Producer(new ActiveMQConfig(app.toUpperCase()
                + ".OUTQUEUE"));
        p.send(json, correlationID, logger);
    }
}
