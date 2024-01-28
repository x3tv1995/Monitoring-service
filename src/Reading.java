import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

// Класс, представляющий показания счетчика
public class Reading implements Serializable {
    private final int userId;         // Идентификатор пользователя
    private final double counterValue; // Значение счетчика
    private final String month;        // Месяц

    // Конструктор для создания объекта Reading
    public Reading(int userId, double counterValue, String month) {
        this.userId = userId;
        this.counterValue = counterValue;
        this.month = month;
    }

    // Геттер для получения идентификатора пользователя
    public int getUserId() {
        return userId;
    }

    // Геттер для получения значения счетчика
    public double getCounterValue() {
        return counterValue;
    }

    // Геттер для получения месяца
    public String getMonth() {
        return month;
    }


    @Override
    public String toString() {
        return "Reading{" +
                "userId=" + userId +
                ", counterValue=" + counterValue +
                ", month='" + month + '\'' +
                '}';
    }
}

// Класс, управляющий показаниями
class ReadingManager {
    private List<Reading> allReadings;
    private final Map<Integer, List<Reading>> readingsByUser;
    private static final Logger LOGGER = Logger.getLogger(ReadingManager.class.getName());

    public ReadingManager() {
        this.readingsByUser = new HashMap<>();
        this.allReadings = new ArrayList<>();
    }

    public void submitReading(Reading reading,User user) {
        allReadings.add(reading);

        int userId = reading.getUserId();
        readingsByUser.computeIfAbsent(userId, k -> new ArrayList<>()).add(reading);
        user.getSubmittedReadings().add(reading);
        System.out.println("Reading submitted for user " + userId + " in month " + reading.getMonth());
    }

    // Изменение: Использование UserRole вместо строки для сравнения
    public List<Reading> getLatestReadings(int userId, UserRoles userRole) {
        if (userRole == UserRoles.ADMIN) {
            return getAllReadings();
        } else {
            return getReadingsForUser(userId);
        }
    }

    // Изменение: Добавление логики для получения фактических данных
    private List<Reading> getAllReadings() {
        return getAllReadingsForUser(UserRoles.ADMIN);
    }

    private List<Reading> getAllReadingsForUser(UserRoles userRole) {
        List<Reading> allReadings = new ArrayList<>();
        for (List<Reading> userReadings : readingsByUser.values()) {
            allReadings.addAll(userReadings);
        }
        return allReadings;
    }


    public List<Reading> getReadingHistory(int userId) {
        // Возвращаем список показаний для пользователя, или пустой список, если нет данных
        return getReadingsForUser(userId);
    }

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



