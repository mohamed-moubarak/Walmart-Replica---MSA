package walmart.core;


import walmart.core.commands.admin.CreateNewProductCommand;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class CommandsMap {
    private static ConcurrentMap<String, Class<?>> cmdMap;
//Each new function you create, you must add it below
    public static void instantiate() {
        cmdMap = new ConcurrentHashMap<>();
         cmdMap.put("CreateNewProduct", CreateNewProductCommand.class);
    }

    public static Class<?> queryClass(String cmd) {
        return cmdMap.get(cmd);
    }
}