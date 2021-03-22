package messenger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Represents the class that contains methods for integration testing input/output methods
 *
 * @author Olga Petrova
 */

public class IntegrationInputOutputTestSuite {

    private static final ClassLoader CLASS_LOADER = IntegrationInputOutputTestSuite.class.getClassLoader();
    private static final String NEW_LINE = System.lineSeparator();

    private Messenger messenger;

    @BeforeEach
    public void prepareInputData() {
        messenger = new Messenger();
    }

    @AfterEach
    public void cleanInputData() {
        messenger = null;
    }

    @Test
    public void shouldThrowExceptionWhenWrongInputFileName() {
        assertThrows(NullPointerException.class, () ->
                new MessengerInput(new BufferedReader(new FileReader(CLASS_LOADER.getResource
                        ("nonexistent.txt").getFile()))));
    }

    @Test
    public void shouldThrowExceptionWhenWrongOutputFileName() {
        assertThrows(NullPointerException.class, () ->
                new MessengerOutput().outputToFile("", new FileWriter(CLASS_LOADER.getResource
                        ("nonexistent.txt").getFile())));
    }

    @Test
    public void shouldReadTemplateFromFile() throws IOException, URISyntaxException {
        // given
        Map<String, String> inputData = new HashMap<>();
        inputData.put("username", "Anna");
        inputData.put("event", "meeting");
        inputData.put("eventDate", "12.09.2020");
        inputData.put("tag", "#{meet}");

        // when
        String result = messenger.readTemplate(inputData);

        // then
        assertEquals("Dear Anna," + NEW_LINE +
                "we would like to inform you about our meeting on 12.09.2020." + NEW_LINE +
                "Please see #{meet} for more information.", result);
    }

    @Test
    public void shouldProcessFileOperating() throws IOException, URISyntaxException {
        // given
        // when
        messenger.processFileOperation("input.txt", "output.txt");

        // then
        Path outputFile = Paths.get(CLASS_LOADER.getResource("output.txt").toURI());

        List<String> result = new ArrayList<>();
        result.add("Dear Max,");
        result.add("we would like to inform you about our meeting on 12.09.2020.");
        result.add("Please see #{meet} for more information.");

        assertAll(
                () -> assertTrue(Files.exists(outputFile)),
                () -> assertLinesMatch(result, Files.readAllLines(outputFile))
        );
    }
}
