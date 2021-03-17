package messenger;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * @author Olga Petrova
 */

public class OutputExtension implements AfterTestExecutionCallback {

    public static final String TEST_TEMPLATE_FILE = "test_output.txt";
    public static final String LINE_SEPARATOR = System.lineSeparator();

    @Override
    public void afterTestExecution(ExtensionContext extensionContext) throws Exception {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_TEMPLATE_FILE))) {
            writer.write("Test method: " + extensionContext.getDisplayName() + LINE_SEPARATOR);
            writer.write("Result: " +  (extensionContext.getExecutionException().isPresent() ? "failed" : "passed"));
        }
    }
}
