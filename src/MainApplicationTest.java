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
    void getActualReadings_ReturnsLatestReadings() {
        UserManager userManager = new UserManager();
        ReadingManager readingManager = new ReadingManager();
        MainApplication mainApplication = new MainApplication(userManager, readingManager);

        User user = new User("testUser", "password", UserRoles.USER);
        userManager.registerUser(user);

        Reading reading1 = new Reading(user.getId(), 1, 10.0, 20.0);
        readingManager.submitReading(reading1, user);

        Reading reading2 = new Reading(user.getId(), 2, 15.0, 25.0);
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

        Reading reading1 = new Reading(user.getId(), 1, 10.0, 20.0);
        readingManager.submitReading(reading1, user);

        Reading reading2 = new Reading(user.getId(), 2, 15.0, 25.0);
        readingManager.submitReading(reading2, user);

        List<Reading> historyReadings = mainApplication.getReadingHistory(user);
        List<Reading> expectedHistory = new ArrayList<>();
        expectedHistory.add(reading1);
        expectedHistory.add(reading2);

        assertEquals(expectedHistory, historyReadings);
    }
}