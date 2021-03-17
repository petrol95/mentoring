package messenger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Olga Petrova
 */

@ExtendWith(MockitoExtension.class)
public class InputOutputTestSuite {

    public static final String TEMP_FILE = "temp.txt";
    public static final String INPUT_FILE = "input.txt";
    public static final String OUTPUT_FILE = "output.txt";

    private static final String NEW_LINE = Messenger.LINE_SEPARATOR;
    private static final String TEMPLATE_LINE = "Dear #{username}," + NEW_LINE +
            " we would like to inform you about our #{event} on #{eventDate}." + NEW_LINE +
            " Please see #{tag} for more information.";

    private Messenger messenger;
    private Map<String, String> inputData;
    private BufferedReader mockReader;

    @BeforeEach
    public void prepareInputData() {
        messenger = new Messenger();
        mockReader = mock(BufferedReader.class);

        inputData = new HashMap<>();
        inputData.put("username", "Anna");
        inputData.put("event", "meeting");
        inputData.put("eventDate", "12.09.2020");
        inputData.put("tag", "#{meet}");
    }

    @Test
    public void shouldWriteMessageToFile(@TempDir Path tempDir) throws IOException {
        Path tempFile = tempDir.resolve(TEMP_FILE);
        when(mockReader.readLine()).thenReturn(TEMPLATE_LINE).thenReturn(null);
        MessengerOutput messengerOutput = new MessengerOutput();

        List<String> result = new ArrayList<>();
        result.add("Dear Anna,");
        result.add(" we would like to inform you about our meeting on 12.09.2020.");
        result.add(" Please see #{meet} for more information.");

        String message = messenger.operateTemplate(inputData, mockReader);
        messengerOutput.outputToFile(message, new FileWriter(tempFile.toString()));

        assertAll(
                () -> assertTrue(Files.exists(tempFile)),
                () -> assertLinesMatch(result, Files.readAllLines(tempFile))
        );
    }

    @Test
    public void shouldThrowExceptionWhenWrongInputFileName() {
        assertThrows(FileNotFoundException.class, () ->
                new MessengerInput(new BufferedReader(new FileReader("failed.txt"))));
    }

    @Test
    public void shouldCollectDataInMapWhenMockedInput() throws IOException {
        MessengerInput messengerInput = new MessengerInput(mockReader);

        when(mockReader.readLine())
                .thenReturn("username Anna", "event meeting", "eventDate 12.09.2020", "tag #{meet}")
                .thenReturn(null);

        assertEquals(inputData, messengerInput.inputFromFile());
    }

    @Test
    public void shouldAddDataToInputMap() {
        MessengerInput messengerInput = new MessengerInput(mockReader);
        MessengerInput spyInput = spy(messengerInput);

        spyInput.collectInputData("username Anna");
        assertEquals("Anna", spyInput.getInputData().get("username"));
    }

    @Test
    public void shouldRunFileOperation() throws IOException {
        Messenger mockMessenger = mock(Messenger.class);
        String[] params = new String[2];

        Launcher.chooseOperationType(params, mockMessenger);

        verify(mockMessenger, times(1)).processFileOperation(params);
        verify(mockMessenger, never()).processConsoleOperation();
    }

    @Test
    public void shouldRunConsoleOperation() throws IOException {
        Messenger mockMessenger = mock(Messenger.class);
        String[] params = new String[0];

        Launcher.chooseOperationType(params, mockMessenger);

        verify(mockMessenger, times(1)).processConsoleOperation();
        verify(mockMessenger, never()).processFileOperation(params);
    }

    @Test
    public void shouldReadTemplateFromFile() throws IOException {
        assertEquals("Dear Anna," + NEW_LINE +
                        "we would like to inform you about our meeting on 12.09.2020." + NEW_LINE +
                        "Please see #{meet} for more information." + NEW_LINE,
                messenger.readTemplate(inputData));
    }

    @Test
    public void shouldProcessFileOperating() throws IOException {
        Path outputFile = Paths.get(OUTPUT_FILE);

        List<String> result = new ArrayList<>();
        result.add("Dear Anna,");
        result.add("we would like to inform you about our meeting on 12.09.2020.");
        result.add("Please see #{meet} for more information.");

        messenger.processFileOperation(new String[]{INPUT_FILE, OUTPUT_FILE});

        assertAll(
                () -> assertTrue(Files.exists(outputFile)),
                () -> assertLinesMatch(result, Files.readAllLines(outputFile))
        );
    }
}
