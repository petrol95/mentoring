package messenger;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Olga Petrova
 */

public class MessengerInput {

    public static final String SPACE_EXPRESSION = "\\s";
    public static final String INPUT_FINISH_CONDITION = "*";

    private final BufferedReader bufferedReader;
    private final Map<String, String> inputData;

    public MessengerInput(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
        inputData = new HashMap<>();
    }

    public Map<String, String> inputFromFile() throws IOException {
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                collectInputData(line);
            }
        } finally {
            bufferedReader.close();
        }
        return inputData;
    }

    public Map<String, String> inputFromConsole() throws IOException {
        System.out.println("Please enter template parameters. Print asterisk on the new line to finish");
        try {
            while (true) {
                String line = bufferedReader.readLine();
                if (line.contains(INPUT_FINISH_CONDITION)) {
                    break;
                }
                collectInputData(line);
            }
        } finally {
            bufferedReader.close();
        }
        return inputData;
    }

    public void collectInputData(String line) {
        String[] data = line.split(SPACE_EXPRESSION);
        inputData.put(data[0], data[1]);
    }

    public Map<String, String> getInputData() {
        return inputData;
    }
}
