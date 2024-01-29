import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ReadingTest {

    @Test
    void createReading_ValidInput_Success() {
        int userId = 1;
        int month = 1;
        double hotWaterCounter = 10.5;
        double coldWaterCounter = 20.0;

        Reading reading = new Reading(userId, month, hotWaterCounter, coldWaterCounter);

        assertEquals(userId, reading.getUserId());
        assertEquals(month, reading.getMonth());
        assertEquals(hotWaterCounter, reading.getHotWaterCounter());
        assertEquals(coldWaterCounter, reading.getColdWaterCounter());
    }

    @Test
    void toString_ReturnsExpectedString() {
        int userId = 1;
       int month = 1;
        double hotWaterCounter = 10.5;
        double coldWaterCounter = 20.0;

        Reading reading = new Reading(userId, month, hotWaterCounter, coldWaterCounter);

        String expectedString = "Reading{" +
                "userId=" + userId +
                ", hotWaterCounter=" + hotWaterCounter +
                ", coldWaterCounter=" + coldWaterCounter +
                ", month='" + month + '\'' +
                '}';

        assertEquals(expectedString, reading.toString());
    }
}

class ReadingManagerTest {

    @Test
    void submitReading_ValidInput_Success() {
        ReadingManager readingManager = new ReadingManager();
        User user = new User("testUser", "password", UserRoles.USER);
        readingManager.submitReading(new Reading(1, 1, 10.0, 20.0), user);

        List<Reading> userReadings = readingManager.getReadingsForUser(user.getId());
        assertEquals(1, userReadings.size());
    }

    @Test
    void getLatestReadings_AdminRole_ReturnsAllReadings() {
        ReadingManager readingManager = new ReadingManager();
        readingManager.submitReading(new Reading(1, 1, 10.0, 20.0), new User("admin", "admin", UserRoles.ADMIN));
        readingManager.submitReading(new Reading(2, 2, 15.0, 25.0), new User("testUser", "password", UserRoles.USER));

        List<Reading> actualReadings = readingManager.getLatestReadings(1, UserRoles.ADMIN);
        assertEquals(2, actualReadings.size());
    }

    @Test
    void getLatestReadings_UserRole_ReturnsUserReadings() {
        ReadingManager readingManager = new ReadingManager();
        readingManager.submitReading(new Reading(1, 1, 10.0, 20.0), new User("admin", "admin", UserRoles.ADMIN));
        readingManager.submitReading(new Reading(2, 2, 15.0, 25.0), new User("testUser", "password", UserRoles.USER));

        List<Reading> actualReadings = readingManager.getLatestReadings(2, UserRoles.USER);
        assertEquals(1, actualReadings.size());
    }


}