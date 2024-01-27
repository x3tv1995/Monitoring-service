import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;


public class MainApplication {
    private final ReadingManager readingManager;
    private final UserManager userManager;


    public MainApplication(UserManager userManager, ReadingManager readingManager) {
        this.userManager = userManager;
        this.readingManager = readingManager;

    }


    // Метод для обработки эндпоинта ввода данных по счетчику
    public void submitCounterData(User user) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Введите данные по счетчику:");
            double counterValue = Double.parseDouble(reader.readLine());

            System.out.println("Введите месяц:");
            String month = reader.readLine();

            // Создаем объект Reading с введенными данными
            Reading newReading = new Reading(user.getId(), counterValue, month);

            // Передаем объект Reading для обработки
            readingManager.submitReading(newReading);

            System.out.println("Данные по счетчику успешно добавлены.");
        } catch (IOException | NumberFormatException e) {
            System.out.println("Ошибка ввода данных: " + e.getMessage());
        }
    }

    private void performDropProfile(User user) {
        System.out.println("Действие: Удаление профиля. Пользователь: " + user.getUsername());
        // Ваша логика удаления профиля
        userManager.getUsers().remove(user);
        System.out.println("Профиль пользователя успешно удален.");
    }


    // Метод для обработки эндпоинта получения актуальных показаний
    public List<Reading> getActualReadings(User user) {
        // Изменение: Добавлена логика обработки полученных данных и их вывода
        List<Reading> actualReadings = readingManager.getLatestReadings(user.getId(), user.getRole());
        System.out.println("Actual Readings: " + actualReadings);
        return actualReadings;
    }

    // Метод для обработки эндпоинта подачи показаний
    public void submitReading(Reading reading) {
        // Изменение: Передаем объект Reading, созданный в методе main
        readingManager.submitReading(reading);
    }

    // Метод для обработки эндпоинта получения показаний за конкретный месяц
    public void getMonthReadings(int userId, String month) {
        // Изменение: Добавлена логика обработки полученных данных и их вывода
        List<Reading> monthReadings = readingManager.getMonthReadings(userId, month);
        System.out.println("Readings for " + month + ": " + monthReadings);
    }

    // Метод для обработки эндпоинта получения истории показаний
    public List<Reading> getReadingHistory(User user) {
        // Изменение: Добавлена логика обработки полученных данных и их вывода
        List<Reading> historyReadings = readingManager.getReadingHistory(user.getId());
        System.out.println("Reading History: " + historyReadings);
        return historyReadings;
    }


}
