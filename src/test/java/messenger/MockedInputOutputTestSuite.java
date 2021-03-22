package messenger;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Represents the class that contains methods for unit testing input/output methods
 *
 * @author Olga Petrova
 */

@ExtendWith(MockitoExtension.class)
public class MockedInputOutputTestSuite {

    private static final String NEW_LINE = System.lineSeparator();

    @Mock
    private BufferedReader mockReader;

    @Test
    public void shouldWriteMessageToFile(@TempDir Path tempDir) throws IOException {
        // given
        Path tempFile = tempDir.resolve("temp.txt");

        String message = "Dear Anna," + NEW_LINE +
                " we would like to inform you about our meeting on 12.09.2020." + NEW_LINE +
                " Please see #{meet} for more information.";

        MessengerOutput messengerOutput = new MessengerOutput();

        // when
        messengerOutput.outputToFile(message, new FileWriter(tempFile.toString()));

        // then
        List<String> result = new ArrayList<>();
        result.add("Dear Anna,");
        result.add(" we would like to inform you about our meeting on 12.09.2020.");
        result.add(" Please see #{meet} for more information.");

        assertAll(
                () -> assertTrue(Files.exists(tempFile)),
                () -> assertLinesMatch(result, Files.readAllLines(tempFile))
        );
    }

    @Test
    public void shouldCollectDataInMapWhenMockedInput() throws IOException {
        // given
        MessengerInput messengerInput = new MessengerInput(mockReader);

        // when
        when(mockReader.readLine())
                .thenReturn("username Anna", "event meeting", "eventDate 12.09.2020", "tag #{meet}")
                .thenReturn(null);

        // then
        Map<String, String> inputData = new HashMap<>();
        inputData.put("username", "Anna");
        inputData.put("event", "meeting");
        inputData.put("eventDate", "12.09.2020");
        inputData.put("tag", "#{meet}");

        assertEquals(inputData, messengerInput.inputFromFile());
    }

    @Test
    public void shouldAddDataToInputMap() {
        // given
        MessengerInput messengerInput = new MessengerInput(mockReader);
        MessengerInput spyInput = spy(messengerInput);
        Map<String, String> inputData = new HashMap<>();

        // when
        spyInput.collectInputData("username Anna", inputData);

        // then
        assertEquals("Anna", inputData.get("username"));
    }

    @Test
    public void shouldRunFileOperation() throws Exception {
        // given
        Messenger mockMessenger = mock(Messenger.class);
        String[] params = new String[] {"input.txt", "output.txt"};

        // when
        Launcher.chooseOperationType(params, mockMessenger);

        // then
        verify(mockMessenger, times(1)).processFileOperation(params[0], params[1]);
        verify(mockMessenger, never()).processConsoleOperation();
    }

    @Test
    public void shouldRunConsoleOperation() throws IOException, URISyntaxException {
        // given
        Messenger mockMessenger = mock(Messenger.class);
        String emptyFileName = "";
        String[] params = new String[0];

        // when
        Launcher.chooseOperationType(params, mockMessenger);

        // then
        verify(mockMessenger, times(1)).processConsoleOperation();
        verify(mockMessenger, never()).processFileOperation(emptyFileName, emptyFileName);
    }
}
