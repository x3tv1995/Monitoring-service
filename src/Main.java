import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {
    private static UserManager userManager = new UserManager();
    private static ReadingManager readingManager = new ReadingManager();
    public static void main(String[] args) {

        User user = null;
        MainApplication mainApp = null;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            user = initMenu(reader) ;
            mainApp = processLogin(user, userManager, readingManager);
            if (mainApp != null) {
                // Остальной код приложения
                Reading newReading = new Reading(user.getId(), 100.0, "January");

                // Подаем показания
                mainApp.submitReading(newReading);

                // Получаем актуальные показания
                mainApp.getActualReadings(user);

                // Получаем историю показаний
                mainApp.getReadingHistory(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Пример использования класса MainApplication
        if (user != null && mainApp != null) {
            // Проверьте, что user и mainApp не являются null, прежде чем использовать их
            Reading newReading = new Reading(user.getId(), 100.0, "January");

            // Подаем показания
            mainApp.submitReading(newReading);

            // Получаем актуальные показания
            List<Reading> actualReadings = mainApp.getActualReadings(user);
            System.out.println("Actual Readings: " + actualReadings);

            // Получаем историю показаний
            List<Reading> historyReadings = mainApp.getReadingHistory(user);
            System.out.println("Reading History: " + historyReadings);
        }
    }

    private static MainApplication processLogin(User user, UserManager userManager, ReadingManager readingManager) {
        MainApplication mainApp = null;

        if (user != null) {


            mainApp = new MainApplication(userManager, readingManager);

            // Вход выполнен успешно, выполните нужные действия
            // Например, получите показания, историю и т.д.
            mainApp.getActualReadings(user);
            mainApp.getReadingHistory(user);
            mainApp.submitCounterData(user); // Добавлен вызов метода ввода данных по счетчику
            // Добавьте другие действия по мере необходимости
        }

        return mainApp;
    }
    private static  User initMenu(BufferedReader reader) throws IOException {
        boolean str = true;
        User user=null;

        while (str) {
            System.out.println("Нажмите 1 для входа, 2 для регистрации :");
            String choice = reader.readLine();
            if ("1".equals(choice)) {
                user = User.inputUserAuthorization(reader, userManager);

                str = false;
            } else if ("2".equals(choice)) {
                user = User.inputUserRegistration(reader);
                userManager.getUsers().add(user);
                userManager.registerUser(user);

                str = false;
            } else {

                System.out.println("Некорректный выбор.");
            }

        }
        return user;
    }

}