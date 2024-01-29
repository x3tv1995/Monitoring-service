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

    public List<Reading> getActualReadings(User user) {
        List<Reading> actualReadings = readingManager.getLatestReadings(user.getId(), user.getRole());
        System.out.println("Фактические показания: " + actualReadings);
        return actualReadings;
    }



    /**
     * Получает и выводит историю показаний водосчетчика для пользователя.
     *
     * @param user Пользователь, для которого нужно получить историю показаний.
     * @return Список исторических показаний.
     */
    public List<Reading> getReadingHistory(User user) {
        List<Reading> historyReadings = readingManager.getReadingHistory(user.getId());
        System.out.println("История показаний: " + historyReadings);
        return historyReadings;
    }

}
