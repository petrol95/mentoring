package messenger;

import java.io.*;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Olga Petrova
 */

public class Messenger {

    public static final String TEMPLATE_FILE = "template.txt";
    public static final String TEMPLATE_PATTERN = "\\#\\{.+?\\}";
    public static final int PATTERN_START_SHIFT = 2;
    public static final int PATTERN_END_SHIFT = 1;
    public static final String LINE_SEPARATOR = System.lineSeparator();

    public void processFileOperation(String[] args) throws IOException {
        MessengerInput messengerInput = new MessengerInput(new BufferedReader(new FileReader(args[0])));
        String message = readTemplate(messengerInput.inputFromFile());
        new MessengerOutput().outputToFile(message, new FileWriter(args[1]));
    }

    public void processConsoleOperation() throws IOException {
        MessengerInput messengerInput = new MessengerInput(new BufferedReader(new InputStreamReader(System.in)));
        String message = readTemplate(messengerInput.inputFromConsole());
        new MessengerOutput().printToConsole(message);
    }

    public String readTemplate(Map<String, String> inputData) throws IOException {
        File template = new File(TEMPLATE_FILE);
        try (BufferedReader reader = new BufferedReader(new FileReader(template))) {
            return operateTemplate(inputData, reader);
        }
    }

    public String operateTemplate(Map<String, String> inputData, BufferedReader reader) throws IOException {
        String line;
        StringBuffer decodedMessage = new StringBuffer();
        while ((line = reader.readLine()) != null) {
            Pattern pattern = Pattern.compile(TEMPLATE_PATTERN);
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                String parameter = line.substring(matcher.start() + PATTERN_START_SHIFT,
                        matcher.end() - PATTERN_END_SHIFT);
                String param = inputData.get(parameter);
                if (param == null) {
                    throw new IllegalArgumentException("Parameter " + parameter + " should be initialized");
                }
                matcher.appendReplacement(decodedMessage, inputData.get(parameter));
            }
            matcher.appendTail(decodedMessage).append(LINE_SEPARATOR);
        }
        return decodedMessage.toString();
    }
}
