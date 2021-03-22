package messenger;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.DisabledOnJre;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

/**
 * Represents the class that contains methods for unit testing template operation methods
 *
 * @author Olga Petrova
 */

public class TemplateOperationTestSuite {

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
    @Staging
    public void shouldPrintWhenAllParametersGiven() {
        // given
        Map<String, String> inputData = new HashMap<>();
        inputData.put("username", "Anna");
        inputData.put("event", "meeting");
        inputData.put("eventDate", "12.09.2020");
        inputData.put("tag", "#{meet}");

        String template = "Dear #{username}," +
                " we would like to inform you about our #{event} on #{eventDate}." +
                " Please see #{tag} for more information.";

        // when
        String result = messenger.operateTemplate(inputData, template);

        // then
        assertEquals("Dear Anna," +
                " we would like to inform you about our meeting on 12.09.2020." +
                " Please see #{meet} for more information.", result);
    }

    @Test
    @Staging
    public void shouldThrowExceptionWhenSomeParametersMissed() {
        // given
        Map<String, String> inputData = new HashMap<>();
        inputData.put("username", "Anna");
        inputData.put("event", "meeting");

        String template = "Dear #{username}," +
                " we would like to inform you about our #{event} on #{eventDate}." +
                " Please see #{tag} for more information.";

        // when
        // then
        Assertions.assertThrows(IllegalArgumentException.class, () -> messenger.operateTemplate(inputData, template));
    }

    @Test
    @ExtendWith(OutputExtension.class)
    public void shouldWriteTestResultToFile() {
        // given
        Map<String, String> inputData = new HashMap<>();
        inputData.put("username", "Anna");
        inputData.put("event", "meeting");
        inputData.put("eventDate", "12.09.2020");
        inputData.put("tag", "#{meet}");

        String template = "Dear #{username}," +
                " we would like to inform you about our #{event} on #{eventDate}." +
                " Please see #{tag} for more information.";

        // when
        String result = messenger.operateTemplate(inputData, template);

        // then
        assertEquals("Dear Anna," +
                " we would like to inform you about our meeting on 12.09.2020." +
                " Please see #{meet} for more information.", result);
    }

    @TestFactory
    Collection<DynamicTest> runDynamicTestsWithValidParameters() {
        // given
        Map<String, String> firstInputData = new HashMap<>();
        firstInputData.put("username", "John");
        firstInputData.put("event", "party");
        firstInputData.put("eventDate", "14.10.2021");
        firstInputData.put("tag", "#123");

        Map<String, String> secondInputData = new HashMap<>();
        secondInputData.put("username", "Peter");
        secondInputData.put("event", "lunch");
        secondInputData.put("eventDate", "2020/09/01");
        secondInputData.put("tag", "link");

        String template = "Dear #{username}," +
                " we would like to inform you about our #{event} on #{eventDate}." +
                " Please see #{tag} for more information.";

        // when
        String firstResult = messenger.operateTemplate(firstInputData, template);
        String secondResult = messenger.operateTemplate(secondInputData, template);

        // then
        return Arrays.asList(
                dynamicTest("Dynamic test #1",
                        () -> assertEquals("Dear John," +
                                " we would like to inform you about our party on 14.10.2021." +
                                " Please see #123 for more information.", firstResult)),

                dynamicTest("Dynamic test #2",
                        () -> assertEquals("Dear Peter," +
                                " we would like to inform you about our lunch on 2020/09/01." +
                                " Please see link for more information.", secondResult))
        );
    }

    @ParameterizedTest
    @MethodSource("generateValidParams")
    public void shouldPrintForDifferentValidParameters(Map<String, String> data, String result) {
        // given
        String template = "Dear #{username}," +
                " we would like to inform you about our #{event} on #{eventDate}." +
                " Please see #{tag} for more information.";

        // when
        // then
        assertEquals(result, messenger.operateTemplate(data, template));
    }

    private static Stream<Arguments> generateValidParams() {
        // given
        Map<String, String> firstInputData = new HashMap<>();
        firstInputData.put("username", "John");
        firstInputData.put("event", "party");
        firstInputData.put("eventDate", "14.10.2021");
        firstInputData.put("tag", "#123");

        Map<String, String> secondInputData = new HashMap<>();
        secondInputData.put("username", "Peter");
        secondInputData.put("event", "lunch");
        secondInputData.put("eventDate", "2020/09/01");
        secondInputData.put("tag", "link");

        return Stream.of(
                Arguments.of(
                        firstInputData,
                        "Dear John," +
                                " we would like to inform you about our party on 14.10.2021." +
                                " Please see #123 for more information."),
                Arguments.of(
                        secondInputData,
                        "Dear Peter," +
                                " we would like to inform you about our lunch on 2020/09/01." +
                                " Please see link for more information.")
        );
    }

    @Test
    @Staging
    @DisabledOnJre(JRE.JAVA_8)
    public void shouldNotBeRunOnJava8() {
        // given
        Map<String, String> inputData = new HashMap<>();
        inputData.put("username", "John");
        inputData.put("event", "party");
        inputData.put("eventDate", "14.10.2021");
        inputData.put("tag", "#123");

        String template = "Dear #{username}," +
                " we would like to inform you about our #{event} on #{eventDate}." +
                " Please see #{tag} for more information.";

        // when
        String result = messenger.operateTemplate(inputData, template);

        // then
        assertEquals("Dear John," +
                " we would like to inform you about our party on 14.10.2021."  +
                " Please see #123 for more information.", result);
    }
}
