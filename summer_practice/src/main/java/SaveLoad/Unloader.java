package SaveLoad;

import Graph.Graph;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.naming.Context;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Unloader {
    public boolean save(Graph graph, String name) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(graph);
        FileOutputStream output = null;

        try {
            output = new FileOutputStream(name);
            output.write(jsonString.getBytes());
        } catch (Exception e) {
            Logger logger = LogManager.getLogger();
            logger.info("in Unloader: " + e.getMessage());
            return false;
        }
        if (output != null) {
            try {
                output.close();
            } catch (IOException e) {
                Logger logger = LogManager.getLogger();
                logger.info("in Unloader: " + e.getMessage());
                return false;
            }
        }
        return true;
    }
}
