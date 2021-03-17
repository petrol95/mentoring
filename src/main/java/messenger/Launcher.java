package messenger;

import java.io.IOException;

/**
 * @author Olga Petrova
 */

public class Launcher {

    public static void main(String[] args) throws IOException {
        Messenger messenger = new Messenger();
        chooseOperationType(args, messenger);
    }

    public static void chooseOperationType(String[] args, Messenger messenger) throws IOException {
        if (args.length == 0) {
            messenger.processConsoleOperation();
        } else {
            messenger.processFileOperation(args);
        }
    }
}