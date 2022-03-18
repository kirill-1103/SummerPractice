import Controller.Controller;
import GUI.Gui;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Main {
    public static void main(String[] args) {
        Controller controller = new Controller();
        controller.start();
        Logger logger = LogManager.getLogger();
        logger.info("Программа запущена.");
    }
}
