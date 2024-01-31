import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

class ReadingManager {
    private List<Reading> allReadings;                 // Список всех показаний
    private final Map<Integer, List<Reading>> readingsByUser;  // Словарь показаний по пользователю
    private static final Logger LOGGER = Logger.getLogger(ReadingManager.class.getName());

    /**
     * Конструктор создает объект ReadingManager и инициализирует списки показаний.
     */
    public ReadingManager() {
        this.readingsByUser = new HashMap<>();
        this.allReadings = new ArrayList<>();
    }

    /**
     * Метод добавляет новые показания в систему.
     *
     * @param reading Объект Reading, представляющий новые показания.
     * @param user    Пользователь, предоставивший показания.
     */
    public void submitReading(Reading reading, User user) {
        allReadings.add(reading);

        int userId = reading.getUserId();
        readingsByUser.computeIfAbsent(userId, k -> new ArrayList<>()).add(reading);
        user.getSubmittedReadings().add(reading);
        System.out.println("Reading submitted for user " + userId + " in month " + reading.getMonth());
    }

    /**
     * Метод возвращает последние показания в зависимости от роли пользователя.
     *
     * @param userId   Идентификатор пользователя.
     * @param userRole Роль пользователя (ADMIN или USER).
     * @return Список последних показаний.
     */
    public List<Reading> getLatestReadings(int userId, UserRoles userRole) {
        if (userRole == UserRoles.ADMIN) {
            return getAllReadings();
        } else {
            return getReadingsForUser(userId);
        }
    }

    /**
     * Метод возвращает все показания для администратора.
     *
     * @return Список всех показаний.
     */
    private List<Reading> getAllReadings() {
        return getAllReadingsForUser(UserRoles.ADMIN);
    }

    /**
     * Метод возвращает все показания для конкретного типа пользователя.
     *
     * @param userRole Роль пользователя (ADMIN или USER).
     * @return Список всех показаний для указанного типа пользователя.
     */
    private List<Reading> getAllReadingsForUser(UserRoles userRole) {
        List<Reading> allReadings = new ArrayList<>();
        for (List<Reading> userReadings : readingsByUser.values()) {
            allReadings.addAll(userReadings);
        }
        return allReadings;
    }

    /**
     * Метод возвращает историю показаний для конкретного пользователя.
     *
     * @param userId Идентификатор пользователя.
     * @return История показаний для пользователя.
     */
    public List<Reading> getReadingHistory(int userId) {
        return getReadingsForUser(userId);
    }


    List<Reading> getReadingsForUser(int userId) {
        return readingsByUser.getOrDefault(userId, new ArrayList<>());
    }
}
