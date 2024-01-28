import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MainApplicationTest {

    @Test
    void submitCounterData_ValidInput_Success() throws IOException {
        UserManager userManager = new UserManager();
        ReadingManager readingManager = new ReadingManager();
        MainApplication mainApplication = new MainApplication(userManager, readingManager);

        User user = new User("testUser", "password", UserRoles.USER);
        userManager.registerUser(user);

        String input = "10.5\n20.0\n1\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        mainApplication.submitCounterData(user, reader);

        List<Reading> actualReadings = readingManager.getLatestReadings(user.getId(), user.getRole());
        assertEquals(1, actualReadings.size());
    }

    @Test
    void submitCounterData_InvalidInput_PrintsErrorMessage() throws IOException {
        UserManager userManager = new UserManager();
        ReadingManager readingManager = new ReadingManager();
        MainApplication mainApplication = new MainApplication(userManager, readingManager);

        User user = new User("testUser", "password", UserRoles.USER);
        userManager.registerUser(user);

        String input = "not_a_number\n20.0\n1\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        mainApplication.submitCounterData(user, reader);

        // Проверяем, что в системе нет новых показаний
        List<Reading> actualReadings = readingManager.getLatestReadings(user.getId(), user.getRole());
        assertEquals(0, actualReadings.size());
    }

    @Test
    void getActualReadings_ReturnsLatestReadings() {
        UserManager userManager = new UserManager();
        ReadingManager readingManager = new ReadingManager();
        MainApplication mainApplication = new MainApplication(userManager, readingManager);

        User user = new User("testUser", "password", UserRoles.USER);
        userManager.registerUser(user);

        Reading reading1 = new Reading(user.getId(), "January", 10.0, 20.0);
        readingManager.submitReading(reading1, user);

        Reading reading2 = new Reading(user.getId(), "February", 15.0, 25.0);
        readingManager.submitReading(reading2, user);

        List<Reading> actualReadings = mainApplication.getActualReadings(user);
        assertEquals(1, actualReadings.size());
        assertEquals(reading2, actualReadings.get(0));
    }

    @Test
    void getReadingHistory_ReturnsReadingHistory() {
        UserManager userManager = new UserManager();
        ReadingManager readingManager = new ReadingManager();
        MainApplication mainApplication = new MainApplication(userManager, readingManager);

        User user = new User("testUser", "password", UserRoles.USER);
        userManager.registerUser(user);

        Reading reading1 = new Reading(user.getId(), "January", 10.0, 20.0);
        readingManager.submitReading(reading1, user);

        Reading reading2 = new Reading(user.getId(), "February", 15.0, 25.0);
        readingManager.submitReading(reading2, user);

        List<Reading> historyReadings = mainApplication.getReadingHistory(user);
        List<Reading> expectedHistory = new ArrayList<>();
        expectedHistory.add(reading1);
        expectedHistory.add(reading2);

        assertEquals(expectedHistory, historyReadings);
    }
}