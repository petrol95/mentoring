package messenger;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the class that contains data input methods
 *
 * @author Olga Petrova
 */

public class MessengerInput {

    public static final String SPACE_EXPRESSION = "\\s";
    public static final String INPUT_FINISH_CONDITION = "*";

    private final BufferedReader bufferedReader;

    public MessengerInput(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
    }

    public Map<String, String> inputFromFile() throws IOException {
        Map<String, String> inputData = new HashMap<>();
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                collectInputData(line, inputData);
            }
        } finally {
            bufferedReader.close();
        }
        return inputData;
    }

    public Map<String, String> inputFromConsole() throws IOException {
        Map<String, String> inputData = new HashMap<>();
        System.out.println("Please enter template parameters. Print asterisk on the new line to finish");
        try {
            while (true) {
                String line = bufferedReader.readLine();
                if (line.contains(INPUT_FINISH_CONDITION)) {
                    break;
                }
                collectInputData(line, inputData);
            }
        } finally {
            bufferedReader.close();
        }
        return inputData;
    }

    public void collectInputData(String line, Map<String, String> inputData) {
        String[] data = line.split(SPACE_EXPRESSION);
        inputData.put(data[0], data[1]);
    }
}
