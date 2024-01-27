import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;


public class MainApplication {
    private final ReadingManager readingManager;
    private final UserManager userManager;
    private final UserAction userRole;

    public MainApplication(UserManager userManager, ReadingManager readingManager, UserAction userRole) {
        this.userManager = userManager;
        this.readingManager = readingManager;
        this.userRole = userRole;
    }
    public UserAction getUserRole() {
        return userRole;
    }
    private boolean hasPermission(User user, UserAction action) {
        // Получаем роль пользователя
        String userRole = user.getRole();

        // Проверяем разрешения в зависимости от роли
        switch (userRole) {
            case "ADMIN":
                // Администратор имеет права на все действия
                return true;
            case "USER":
                // Обычный пользователь имеет ограниченные права
                switch (action) {
                    case ADD_NEW_POKAZANIYA:
                        // Разрешено добавление новых показаний
                        return true;
                    // Добавьте другие разрешения для обычного пользователя при необходимости
                    default:
                        return false;
                }
                // Добавьте логику для других ролей при необходимости
            default:
                return false;
        }
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
    public List<Reading> getActualReadings(int userId, UserAction userRole) {
        // Изменение: Добавлена логика обработки полученных данных и их вывода
        List<Reading> actualReadings = readingManager.getLatestReadings(userId, userRole);
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
    public List<Reading> getReadingHistory(int userId, UserAction userRole) {
        // Изменение: Добавлена логика обработки полученных данных и их вывода
        List<Reading> historyReadings = readingManager.getReadingHistory(userId, userRole);
        System.out.println("Reading History: " + historyReadings);
        return historyReadings;
    }




    }
