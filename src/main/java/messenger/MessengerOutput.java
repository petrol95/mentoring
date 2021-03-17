package messenger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * @author Olga Petrova
 */

public class MessengerOutput {

    public MessengerOutput() {
    }

    public void outputToFile(String message, Writer writer) throws IOException {
        try (BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            bufferedWriter.write(message);
        }
    }

    public void printToConsole(String message) {
        System.out.println(message);
    }
}