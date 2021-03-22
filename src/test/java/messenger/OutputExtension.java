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
    public static final ClassLoader CLASS_LOADER = OutputExtension.class.getClassLoader();

    @Override
    public void afterTestExecution(ExtensionContext extensionContext) throws Exception {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CLASS_LOADER.getResource(TEST_TEMPLATE_FILE).getFile()))) {
            writer.write("Test method: " + extensionContext.getDisplayName() + LINE_SEPARATOR);
            writer.write("Result: " +  (extensionContext.getExecutionException().isPresent() ? "failed" : "passed"));
        }
    }
}
