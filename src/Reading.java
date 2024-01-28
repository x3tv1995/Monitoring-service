import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

// Класс, представляющий показания счетчика
/**
 * Класс Reading представляет сущность для хранения данных о показаниях водосчетчика.
 * Реализует интерфейс Serializable для обеспечения возможности сериализации объектов.
 */
public class Reading implements Serializable {
    private final int userId;         // Идентификатор пользователя

    private final String month;        // Месяц

    private double hotWaterCounter;    // Значение счетчика горячей воды
    private double coldWaterCounter;   // Значение счетчика холодной воды

    /**
     * Конструктор для создания объекта Reading с заданными параметрами.
     *
     * @param userId           Идентификатор пользователя.
     * @param month            Месяц, к которому относятся показания.
     * @param hotWaterCounter  Значение счетчика горячей воды.
     * @param coldWaterCounter Значение счетчика холодной воды.
     */
    public Reading(int userId, String month, double hotWaterCounter, double coldWaterCounter) {
        this.userId = userId;
        this.month = month;
        this.hotWaterCounter = hotWaterCounter;
        this.coldWaterCounter = coldWaterCounter;
    }

    /**
     * Получение идентификатора пользователя.
     *
     * @return Идентификатор пользователя.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Получение значения счетчика горячей воды.
     *
     * @return Значение счетчика горячей воды.
     */
    public double getHotWaterCounter() {
        return hotWaterCounter;
    }

    /**
     * Получение значения счетчика холодной воды.
     *
     * @return Значение счетчика холодной воды.
     */
    public double getColdWaterCounter() {
        return coldWaterCounter;
    }

    /**
     * Получение месяца, к которому относятся показания.
     *
     * @return Месяц.
     */
    public String getMonth() {
        return month;
    }

    /**
     * Переопределенный метод toString для возврата строкового представления объекта Reading.
     *
     * @return Строковое представление объекта Reading.
     */
    @Override
    public String toString() {
        return "Reading{" +
                "userId=" + userId +
                ", hotWaterCounter=" + hotWaterCounter +
                ", coldWaterCounter=" + coldWaterCounter +
                ", month='" + month + '\'' +
                '}';
    }
}

/**
 * Класс ReadingManager управляет данными о показаниях водосчетчиков.
 * Включает методы для добавления, получения и обработки данных о показаниях.
 */
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

    /**
     * Метод возвращает показания за указанный месяц для конкретного пользователя.
     *
     * @param userId Идентификатор пользователя.
     * @param month  Месяц, за который запрашиваются показания.
     * @return Список показаний за указанный месяц.
     */
    public List<Reading> getMonthReadings(int userId, String month) {
        List<Reading> readings = getReadingsForUser(userId);
        List<Reading> monthReadings = new ArrayList<>();
        for (Reading reading : readings) {
            if (reading.getMonth().equals(month)) {
                monthReadings.add(reading);
            }
        }
        return monthReadings;
    }
    private List<Reading> getReadingsForUser(int userId) {
        return readingsByUser.getOrDefault(userId, new ArrayList<>());
    }
}





