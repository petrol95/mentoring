package messenger;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

/**
 * Represents the class that contains methods for processing in different modes and parsing the template
 *
 * @author Olga Petrova
 */

public class Messenger {

    public static final String TEMPLATE_FILE = "template.txt";
    public static final String TEMPLATE_PATTERN = "\\#\\{.+?\\}";
    public static final int PATTERN_START_OFFSET = 2;
    public static final int PATTERN_END_OFFSET = 1;
    public static final InputStream CONSOLE_INPUT_STREAM = System.in;
    public static final ClassLoader CLASS_LOADER = Messenger.class.getClassLoader();

    public void processFileOperation(String inputFileName, String outputFileName) throws IOException, URISyntaxException {
        MessengerInput messengerInput = new MessengerInput(new BufferedReader
                (new FileReader(CLASS_LOADER.getResource(inputFileName).getFile())));
        String message = readTemplate(messengerInput.inputFromFile());
        new MessengerOutput().outputToFile(message, new FileWriter(CLASS_LOADER.getResource(outputFileName).getFile()));
    }

    public void processConsoleOperation() throws IOException, URISyntaxException {
        MessengerInput messengerInput = new MessengerInput(new BufferedReader(new InputStreamReader(CONSOLE_INPUT_STREAM)));
        String message = readTemplate(messengerInput.inputFromConsole());
        new MessengerOutput().printToConsole(message);
    }

    public String readTemplate(Map<String, String> inputData) throws IOException, URISyntaxException {
        Path path = Paths.get(Paths.get(CLASS_LOADER.getResource(TEMPLATE_FILE).toURI()).toString());
        String template = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
        return operateTemplate(inputData, template);
    }

    public String operateTemplate(Map<String, String> inputData, String template) {
        return Pattern.compile(TEMPLATE_PATTERN)
                .matcher(template)
                .replaceAll(matchResult -> replaceMatch(inputData, template, matchResult));
    }

    private String replaceMatch(Map<String, String> inputData, String line, MatchResult matchResult) {
        String replacement = line.substring(matchResult.start() + PATTERN_START_OFFSET, matchResult.end() - PATTERN_END_OFFSET);
        if (!inputData.containsKey(replacement)) {
            throw new IllegalArgumentException("Parameter " + replacement + " should be initialized");
        }
        return inputData.get(replacement);
    }
}
