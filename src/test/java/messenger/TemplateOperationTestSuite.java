package messenger;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.DisabledOnJre;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Olga Petrova
 */

public class TemplateOperationTestSuite {

    private static final String NEW_LINE = Messenger.LINE_SEPARATOR;
    private static final String TEMPLATE_LINE = "Dear #{username}," +
            " we would like to inform you about our #{event} on #{eventDate}." +
            " Please see #{tag} for more information.";

    private Messenger messenger;
    private BufferedReader mockReader;

    private Map<String, String> firstInputData;
    private Map<String, String> secondInputData;
    private Map<String, String> thirdInputData;

    @BeforeEach
    public void prepareInputData() {
        messenger = new Messenger();
        mockReader = mock(BufferedReader.class);

        firstInputData = new HashMap<>();
        firstInputData.put("username", "Anna");
        firstInputData.put("event", "meeting");
        firstInputData.put("eventDate", "12.09.2020");
        firstInputData.put("tag", "#{meet}");

        secondInputData = new HashMap<>();
        secondInputData.put("username", "John");
        secondInputData.put("event", "party");
        secondInputData.put("eventDate", "14.10.2021");
        secondInputData.put("tag", "#123");

        thirdInputData = new HashMap<>();
        thirdInputData.put("username", "Peter");
        thirdInputData.put("event", "lunch");
        thirdInputData.put("eventDate", "2020/09/01");
        thirdInputData.put("tag", "link");
    }

    @Test
    @Staging
    public void shouldPrintWhenAllParametersGiven() throws IOException {
        when(mockReader.readLine()).thenReturn(TEMPLATE_LINE).thenReturn(null);
        assertEquals("Dear Anna," +
                " we would like to inform you about our meeting on 12.09.2020." +
                " Please see #{meet} for more information." + NEW_LINE,
                messenger.operateTemplate(firstInputData, mockReader));
    }

    @Test
    @Staging
    public void shouldThrowExceptionWhenSomeParametersMissed() throws IOException {
        when(mockReader.readLine()).thenReturn(TEMPLATE_LINE).thenReturn(null);
        Map<String, String> inputData = new HashMap<>();
        inputData.put("username", "Anna");
        inputData.put("event", "meeting");

        Assertions.assertThrows(IllegalArgumentException.class, () -> messenger.operateTemplate(inputData, mockReader));
    }

    @Test
    @ExtendWith(OutputExtension.class)
    public void shouldWriteTestResultToFile() throws IOException {
        when(mockReader.readLine()).thenReturn(TEMPLATE_LINE).thenReturn(null);
        assertEquals("Dear Anna," +
                " we would like to inform you about our meeting on 12.09.2020." +
                " Please see #{meet} for more information." + NEW_LINE,
                messenger.operateTemplate(firstInputData, mockReader));
    }

    @TestFactory
    Collection<DynamicTest> runDynamicTestsWithValidParameters() {
        return Arrays.asList(
                dynamicTest("Dynamic test #1",
                        () -> {
                            when(mockReader.readLine()).thenReturn(TEMPLATE_LINE).thenReturn(null);
                            assertEquals("Dear John," +
                                            " we would like to inform you about our party on 14.10.2021." +
                                            " Please see #123 for more information." + NEW_LINE,
                                    messenger.operateTemplate(secondInputData, mockReader));
                        }),

                dynamicTest("Dynamic test #2",
                        () -> {
                            when(mockReader.readLine()).thenReturn(TEMPLATE_LINE).thenReturn(null);
                            assertEquals("Dear Peter," +
                                            " we would like to inform you about our lunch on 2020/09/01." +
                                            " Please see link for more information." + NEW_LINE,
                                    messenger.operateTemplate(thirdInputData, mockReader));
                        })
        );
    }

    @ParameterizedTest
    @MethodSource("generateValidParams")
    public void shouldPrintForDifferentValidParameters(Map<String, String> data, String result) throws IOException {
        when(mockReader.readLine()).thenReturn(TEMPLATE_LINE).thenReturn(null);
        assertEquals(result, messenger.operateTemplate(data, mockReader));
    }

    private static Stream generateValidParams() {
        Map<String, String> firstDataKit = new HashMap<>();
        firstDataKit.put("username", "John");
        firstDataKit.put("event", "party");
        firstDataKit.put("eventDate", "14.10.2021");
        firstDataKit.put("tag", "#123");

        Map<String, String> secondDataKit = new HashMap<>();
        secondDataKit.put("username", "Peter");
        secondDataKit.put("event", "lunch");
        secondDataKit.put("eventDate", "2020/09/01");
        secondDataKit.put("tag", "link");

        return Stream.of(
                Arguments.of(
                        firstDataKit,
                        "Dear John," +
                                " we would like to inform you about our party on 14.10.2021." +
                                " Please see #123 for more information." + NEW_LINE),
                Arguments.of(
                        secondDataKit,
                        "Dear Peter," +
                                " we would like to inform you about our lunch on 2020/09/01." +
                                " Please see link for more information." + NEW_LINE)
        );
    }

    @Test
    @Staging
    @DisabledOnJre(JRE.JAVA_11)
    public void shouldNotBeRun() throws IOException {
        when(mockReader.readLine()).thenReturn(TEMPLATE_LINE).thenReturn(null);
        assertEquals("Dear John," +
                " we would like to inform you about our party on 14.10.2021."  +
                " Please see #123 for more information." + NEW_LINE,
                messenger.operateTemplate(secondInputData, mockReader));
    }
}
