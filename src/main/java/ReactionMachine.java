import java.lang.reflect.Constructor;
import java.util.Timer;
import java.util.TimerTask;

public class ReactionMachine {

    public static void main(String[] args) throws Exception {
//        if (args.length != 1) {
//            System.err.println("Usage: java ReactionMachine CONTROLLER NAME");
//            System.exit(1);
//            return;
//        }
//        String controllerName = args[0];
        String controllerName = "SimpleReactionController";
//        String controllerName = "EnhancedReactionController";
        //Load the class file
        Class cl = Class.forName(controllerName, true,
                Thread.currentThread().getContextClassLoader());
        Constructor constructors[] = cl.getConstructors();
        if (constructors.length == 0) {
            System.out.println("There is NO constructor in your controller class");
            System.exit(1);
            return;
        }

        if (constructors.length > 1) {
            System.out.println("There is more than one constructor in your class");
            System.exit(1);
        }

        //Construct the controller
        Controller controller = (Controller) constructors[0].newInstance(new Object[0]);
        //Construct the display
        Gui display = new Display();

        //Connect the components together
        display.connect(controller);
        controller.connect(display, new Rng());

        //Initialize the components
        display.init();
        controller.init();

        Timer t = new Timer();
        Ticker ticker = new Ticker(controller);
        t.scheduleAtFixedRate(ticker, 0, 10);
    }


    private static class Ticker extends TimerTask {
        Controller controller;

        public Ticker(Controller controller) {
            this.controller = controller;
        }


        public void run() {
            controller.tick();
        }
    }

    ////////////////////////////////////
    //Method for the "Random" interface
    ////////////////////////////////////
    private static class Rng implements Random {
        public int getRandom(int from, int to) {
            return from + (int) ((to - from) * Math.random());
        }
    }
}
