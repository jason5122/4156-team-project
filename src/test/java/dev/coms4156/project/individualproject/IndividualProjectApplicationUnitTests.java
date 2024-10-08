package dev.coms4156.project.teamproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 * This class contains unit tests for IndividualProjectApplication.
 */
@SpringBootTest
@ContextConfiguration
public class IndividualProjectApplicationUnitTests {

    @Mock MyFileDatabase mockFileDatabase;

    /**
     * Set up method to run before each test method in this class.
     */
    @BeforeEach
    public void setupForTesting() {
        mockFileDatabase = mock(MyFileDatabase.class);
        // Using the static method overrideDatabase() since the javadoc mentions it is made for
        // testing
        IndividualProjectApplication.overrideDatabase(mockFileDatabase);
    }

    @Test
    public void mainTest() {
        String[] args = {"0"};
        // Asserting myFileDatabase has no department mapping to begin with (before main())
        assertTrue(IndividualProjectApplication.myFileDatabase.getDepartmentMapping().isEmpty());
        IndividualProjectApplication.main(args);
        // Asserting that myFileDatabase has department mapping after running main()
        assertFalse(IndividualProjectApplication.myFileDatabase.getDepartmentMapping().isEmpty());
        assertNotNull(IndividualProjectApplication.myFileDatabase);
    }

    @Test
    public void runMethodPrintValueTest_setup() throws IOException {
        String[] args = {"setup"};
        IndividualProjectApplication app = new IndividualProjectApplication();

        // Get the output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        app.run(args);
        assertTrue(outputStream.toString().contains("System Setup"));
    }

    @Test
    public void runMethodPrintValueTest_startUp() throws IOException {
        String[] args = {"anything else"};
        IndividualProjectApplication app = new IndividualProjectApplication();

        // Get the output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        app.run(args);
        assertTrue(outputStream.toString().contains("Start up"));
    }

    @Test
    public void overrideDatabaseTest() {
        // Asserting that mockFileDatabase is initially set as the database
        assertEquals(mockFileDatabase, IndividualProjectApplication.myFileDatabase);
        MyFileDatabase testDatabase = new MyFileDatabase(1, "testfile.txt");
        IndividualProjectApplication.overrideDatabase(testDatabase);
        // Asserting that running the overrideDatabase() method has changed database to
        // testDatabase
        assertEquals(testDatabase, IndividualProjectApplication.myFileDatabase);
    }

    @Test
    public void resetDataFileTest() {
        IndividualProjectApplication app = new IndividualProjectApplication();
        app.resetDataFile();
        assertFalse(IndividualProjectApplication.myFileDatabase.toString().isEmpty());
    }

    @Test
    public void onTerminationTest() throws IOException {
        IndividualProjectApplication app = new IndividualProjectApplication();
        // Save original output
        PrintStream out = System.out;
        // Get the output on screen
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        try {
            app.onTermination();
            assertTrue(outputStream.toString().contains("Termination"));
        } finally {
            // Restore original out to avoid issues
            System.setOut(out);
        }
    }
}
