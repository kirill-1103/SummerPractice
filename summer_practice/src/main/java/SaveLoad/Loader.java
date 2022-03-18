package SaveLoad;

import Graph.*;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

public class Loader {
    public Optional<Graph> load(String name) {
        Graph graph;
        InputStreamReader reader = null;
        FileInputStream input = null;
        try {
            input = new FileInputStream(name);
            reader = new InputStreamReader(input);
            Gson gson = new Gson();
            graph = gson.fromJson(reader, Graph.class);
        } catch (IOException e) {
            Logger logger = LogManager.getLogger();
            logger.info("in Loader: " + e.getMessage());
            return Optional.empty();
        }
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                Logger logger = LogManager.getLogger();
                logger.info("in Loader: " + e.getMessage());
                return Optional.empty();
            }
        }
        if (input != null) {
            try {
                input.close();
            } catch (IOException e) {
                Logger logger = LogManager.getLogger();
                logger.info("in Loader: " + e.getMessage());
                return Optional.empty();
            }
        }
        return graph == null ? Optional.empty() : Optional.of(graph);
    }
}
