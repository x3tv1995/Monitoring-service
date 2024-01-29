import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
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
    void processActions_AdminShowsReadings_ReturnsReadings() throws IOException {
        String input = "4\n3\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        UserManager userManager = new UserManager();
        ReadingManager readingManager = new ReadingManager();
        Main.initAdmin();

        User admin = new User("admin", "123", UserRoles.ADMIN);
        userManager.registerUser(admin);

        Main.processActions(admin, new BufferedReader(new InputStreamReader(System.in)));

        String output = outputStreamCaptor.toString().trim();
        assertTrue(output.contains("Выберите действие:"));
        assertTrue(output.contains("Введите номер действия:"));
        assertFalse(output.contains("Ошибка: пользователь не найден."));
    }

    @Test
    void processActions_InvalidInput_Repeats() throws IOException {
        String input = "invalid\n3\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        UserManager userManager = new UserManager();
        ReadingManager readingManager = new ReadingManager();
        Main.initAdmin();

        User admin = new User("admin", "123", UserRoles.ADMIN);
        userManager.registerUser(admin);

        Main.processActions(admin, new BufferedReader(new InputStreamReader(System.in)));

        String output = outputStreamCaptor.toString().trim();
        assertTrue(output.contains("Введите номер действия:"));
        assertFalse(output.contains("Ошибка: пользователь не найден."));
        assertTrue(output.contains("Некорректный ввод. Попробуйте еще раз."));
    }

    @Test
    void showReadingsOfAllUsers_AdminShowsReadings_ReturnsReadings() {
        UserManager userManager = new UserManager();
        ReadingManager readingManager = new ReadingManager();
        Main.initAdmin();

        User user1 = new User("user1", "password", UserRoles.USER);
        userManager.registerUser(user1);
        User user2 = new User("user2", "password", UserRoles.USER);
        userManager.registerUser(user2);

        Reading reading1 = new Reading(user1.getId(), 1, 10.0, 20.0);
        readingManager.submitReading(reading1, user1);

        Reading reading2 = new Reading(user2.getId(), 2, 15.0, 25.0);
        readingManager.submitReading(reading2, user2);

        User admin = new User("admin", "123", UserRoles.ADMIN);
        userManager.registerUser(admin);

        Main.showReadingsOfAllUsers();

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
        Main.initAdmin();

        User user = new User("user", "password", UserRoles.USER);
        userManager.registerUser(user);

        Main.submitCounterData(user, new BufferedReader(new InputStreamReader(System.in)));

        String output = outputStreamCaptor.toString().trim();
        assertTrue(output.contains("Данные по счетчику успешно добавлены."));
    }

    @Test
    void submitCounterData_InvalidInput_PrintsErrorMessage() throws IOException {
        String input = "not_a_number\n20.0\n1\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        UserManager userManager = new UserManager();
        ReadingManager readingManager = new ReadingManager();
        Main.initAdmin();

        User user = new User("user", "password", UserRoles.USER);
        userManager.registerUser(user);

        Main.submitCounterData(user, new BufferedReader(new InputStreamReader(System.in)));

        // Проверяем, что в системе нет новых показаний
        List<Reading> actualReadings = readingManager.getLatestReadings(user.getId(), user.getRole());
        assertEquals(0, actualReadings.size());
    }
    @Test
    public void testShowMenuForRegularUser() throws IOException {
        User regularUser = new User("user1", "password1", UserRoles.USER);
        BufferedReader reader = new BufferedReader(new StringReader("3\n"));

        User resultUser = Main.showMenu(regularUser, reader);

        assertEquals(regularUser, resultUser);
    }

    @Test
    public void testShowMenuForAdminUser() throws IOException {
        User adminUser = new User("admin", "adminpass", UserRoles.ADMIN);
        BufferedReader reader = new BufferedReader(new StringReader("4\n"));

        User resultUser = Main.showMenu(adminUser, reader);

        assertEquals(adminUser, resultUser);
    }

    @Test
    public void testShowMenuWithNullUser() throws IOException {
        BufferedReader reader = new BufferedReader(new StringReader("3\n"));

        User resultUser = Main.showMenu(null,reader);

        assertNull(resultUser);
    }
}