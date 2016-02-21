package waffle.cli;

import java.io.File;
import java.net.URL;

public class Main {

    public static void main(String[] args) throws Exception {
        
        if (args.length == 0) {
            throw new IllegalArgumentException("Please provide a command");
        }
        
        if (args.length == 1) {
            throw new IllegalArgumentException("Please provide an argument");
        }
        
        String command = args[0];
        String argument = args[1];
        Task task = null;
        
        if (command.equals(TrainTask.COMMAND)) {
            task = new TrainTask(new File(argument));
        }
        
        if (command.equals(ClassifyTask.COMMAND)) {
            task = new ClassifyTask(new URL(argument));
        }
        
        if (task == null) {
            throw new IllegalArgumentException("Unknown command: " + command);
        }
        
        task.execute();
    }
}
