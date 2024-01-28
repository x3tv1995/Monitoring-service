import java.io.BufferedReader;
import java.io.IOException;

import java.time.LocalDateTime;
import java.util.List;


/**
 * Класс MainApplication представляет главное приложение для управления показаниями водосчетчиков.
 * Взаимодействует с UserManager и ReadingManager для обработки действий пользователей, связанных с показаниями водосчетчиков.
 */
public class MainApplication {
    private final ReadingManager readingManager;
    private final UserManager userManager;

    /**
     * Конструктор для создания нового экземпляра MainApplication с заданными UserManager и ReadingManager.
     *
     * @param userManager   Экземпляр UserManager для управления действиями, связанными с пользователями.
     * @param readingManager Экземпляр ReadingManager для управления показаниями водосчетчиков.
     */
    public MainApplication(UserManager userManager, ReadingManager readingManager) {
        this.userManager = userManager;
        this.readingManager = readingManager;
    }

    /**
     * Обрабатывает подачу показаний водосчетчика от пользователя.
     *
     * @param user   Пользователь, подающий показания.
     * @param reader BufferedReader для ввода данных пользователем.
     */
    public void submitCounterData(User user, BufferedReader reader) {
        LocalDateTime currentDate = LocalDateTime.now();
        try {
            // Проверяем, прошел ли месяц с последней подачи показаний
            if (user.getLastSubmissionDate() == null || user.getLastSubmissionDate().plusMonths(1).isBefore(currentDate)) {
                System.out.println("Введите данные по счетчику:");
                double hotWaterCounter = Double.parseDouble(reader.readLine());
                double coldWaterCounter = Double.parseDouble(reader.readLine());
                System.out.println("Введите месяц:");
                String month = reader.readLine();

                // Создаем объект Reading с введенными данными
                Reading newReading = new Reading(user.getId(), month, hotWaterCounter, coldWaterCounter);

                // Передаем объект Reading для обработки
                readingManager.submitReading(newReading, user);

                // Обновляем время последней подачи показаний
                user.setLastSubmissionDate(currentDate);

                System.out.println("Данные по счетчику успешно добавлены.");
            } else {
                System.out.println("Вы уже подавали показания в этом месяце. Подача показаний доступна один раз в месяц.");
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Ошибка ввода данных: " + e.getMessage());
        }
    }

    /**
     * Получает и выводит фактические показания водосчетчика для пользователя.
     *
     * @param user Пользователь, для которого нужно получить фактические показания.
     * @return Список фактических показаний.
     */
    public List<Reading> getActualReadings(User user) {
        // Получаем и выводим фактические показания из ReadingManager
        List<Reading> actualReadings = readingManager.getLatestReadings(user.getId(), user.getRole());
        System.out.println("Фактические показания: " + actualReadings);
        return actualReadings;
    }

    /**
     * Подает предварительно созданный объект Reading для пользователя.
     *
     * @param reading Объект Reading для подачи.
     * @param user    Пользователь, подающий показания.
     */
    public void submitReading(Reading reading, User user) {
        // Подаем предварительно созданный объект Reading для обработки ReadingManager
        readingManager.submitReading(reading, user);
    }

    /**
     * Получает и выводит показания водосчетчика за конкретный месяц для пользователя.
     *
     * @param userId Идентификатор пользователя.
     * @param month  Месяц, за который нужно получить показания.
     */
    public void getMonthReadings(int userId, String month) {
        // Получаем и выводим показания за конкретный месяц из ReadingManager
        List<Reading> monthReadings = readingManager.getMonthReadings(userId, month);
        System.out.println("Показания за " + month + ": " + monthReadings);
    }

    /**
     * Получает и выводит историю показаний водосчетчика для пользователя.
     *
     * @param user Пользователь, для которого нужно получить историю показаний.
     * @return Список исторических показаний.
     */
    public List<Reading> getReadingHistory(User user) {
        // Получаем и выводим историю показаний из ReadingManager
        List<Reading> historyReadings = readingManager.getReadingHistory(user.getId());
        System.out.println("История показаний: " + historyReadings);
        return historyReadings;
    }
}
