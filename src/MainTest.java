import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.*;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    private final InputStream originalSystemIn = System.in;
    private final PrintStream originalSystemOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    void tearDown() {
        System.setIn(originalSystemIn);
        System.setOut(originalSystemOut);
    }

    @Test
    void registerOrAuthorize_AuthorizeAdmin_ReturnsAdmin() throws IOException {
        String input = "1\nadmin\n123\n4\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        UserManager userManager = new UserManager();
        ReadingManager readingManager = new ReadingManager();
        Main.initAdmin(userManager);

        User result = Main.registerOrAuthorize(new BufferedReader(new InputStreamReader(System.in)), userManager);
        assertNotNull(result);
        assertEquals("admin", result.getUsername());
        assertEquals(UserRoles.ADMIN, result.getRole());
    }

    @Test
    void registerOrAuthorize_RegisterUser_ReturnsRegisteredUser() throws IOException {
        String input = "2\nnewUser\npassword\n1\n10.0\n20.0\n1\n3\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        UserManager userManager = new UserManager();
        ReadingManager readingManager = new ReadingManager();
        Main.initAdmin(userManager);

        User result = Main.registerOrAuthorize(new BufferedReader(new InputStreamReader(System.in)), userManager);
        assertNotNull(result);
        assertEquals("newUser", result.getUsername());
        assertEquals(UserRoles.USER, result.getRole());

        List<Reading> readings = result.getSubmittedReadings();
        assertEquals(1, readings.size());
        assertEquals(10.0, readings.get(0).getHotWaterCounter());
        assertEquals(20.0, readings.get(0).getColdWaterCounter());
    }

    @Test
    void processActions_AdminShowsReadings_ReturnsReadings() throws IOException {
        String input = "4\n3\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        UserManager userManager = new UserManager();
        ReadingManager readingManager = new ReadingManager();
        Main.initAdmin(userManager);

        User admin = new User("admin", "123", UserRoles.ADMIN);
        userManager.registerUser(admin);

        Main.processActions(admin, new BufferedReader(new InputStreamReader(System.in)), userManager, readingManager);

        String output = outputStreamCaptor.toString().trim();
        assertTrue(output.contains("Пользователь admin, показания: []"));
    }

    @Test
    void processActions_InvalidInput_Repeats() throws IOException {
        String input = "invalid\n3\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        UserManager userManager = new UserManager();
        ReadingManager readingManager = new ReadingManager();
        Main.initAdmin(userManager);

        User admin = new User("admin", "123", UserRoles.ADMIN);
        userManager.registerUser(admin);

        Main.processActions(admin, new BufferedReader(new InputStreamReader(System.in)), userManager, readingManager);

        String output = outputStreamCaptor.toString().trim();
        assertTrue(output.contains("Некорректный ввод. Попробуйте еще раз."));
    }

    @Test
    void showReadingsOfAllUsers_AdminShowsReadings_ReturnsReadings() {
        UserManager userManager = new UserManager();
        ReadingManager readingManager = new ReadingManager();
        Main.initAdmin(userManager);

        User user1 = new User("user1", "password", UserRoles.USER);
        userManager.registerUser(user1);
        User user2 = new User("user2", "password", UserRoles.USER);
        userManager.registerUser(user2);

        Reading reading1 = new Reading(user1.getId(), "January", 10.0, 20.0);
        readingManager.submitReading(reading1, user1);

        Reading reading2 = new Reading(user2.getId(), "February", 15.0, 25.0);
        readingManager.submitReading(reading2, user2);

        User admin = new User("admin", "123", UserRoles.ADMIN);
        userManager.registerUser(admin);

        Main.showReadingsOfAllUsers(userManager);

        String output = outputStreamCaptor.toString().trim();
        assertTrue(output.contains("Пользователь user1, показания: [Reading"));
        assertTrue(output.contains("Пользователь user2, показания: [Reading"));
        assertFalse(output.contains("Пользователь admin, показания: []"));
    }

    @Test
    void submitCounterData_ValidInput_Success() throws IOException {
        String input = "10.5\n20.0\n1\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        UserManager userManager = new UserManager();
        ReadingManager readingManager = new ReadingManager();
        Main.initAdmin(userManager);

        User user = new User("user", "password", UserRoles.USER);
        userManager.registerUser(user);

        Main.submitCounterData(user, new BufferedReader(new InputStreamReader(System.in)), readingManager);

        List<Reading> actualReadings = readingManager.getLatestReadings(user.getId(), user.getRole());
        assertEquals(1, actualReadings.size());
    }

    @Test
    void submitCounterData_InvalidInput_PrintsErrorMessage() throws IOException {
        String input = "not_a_number\n20.0\n1\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        UserManager userManager = new UserManager();
        ReadingManager readingManager = new ReadingManager();
        Main.initAdmin(userManager);

        User user = new User("user", "password", UserRoles.USER);
        userManager.registerUser(user);

        Main.submitCounterData(user, new BufferedReader(new InputStreamReader(System.in)), readingManager);

        // Проверяем, что в системе нет новых показаний
        List<Reading> actualReadings = readingManager.getLatestReadings(user.getId(), user.getRole());
        assertEquals(0, actualReadings.size());


    }
}