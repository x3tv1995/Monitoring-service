import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        UserManager userManager = new UserManager();
        ReadingManager readingManager = new ReadingManager();
        User newUser = null;
        MainApplication mainApp = null;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            User existingUser = null;
            String userRoleString = User.inputUserAuthorization(reader, userManager);

            if (userRoleString != null) {
                existingUser = userManager.findUserByRole(userRoleString);
            }
            UserAction userRole = existingUser != null ? UserAction.valueOf(existingUser.getRole()) : null;

            mainApp = new MainApplication(userManager, readingManager, userRole);

            if (existingUser != null && userRole != null) {
                // Вход выполнен успешно, выполните нужные действия
                // Например, получите показания, историю и т.д.

                // Пример использования класса MainApplication
                mainApp.getActualReadings(existingUser.getId(), userRole);
                mainApp.getReadingHistory(existingUser.getId(), userRole);
                mainApp.submitCounterData(existingUser); // Добавлен вызов метода ввода данных по счетчику
                // Добавьте другие действия по мере необходимости
            } else {
                newUser = User.inputUserRegistration(reader);

                if (newUser != null) {
                    userManager.getUsers().add(newUser);
                    userManager.registerUser(newUser);

                    // Пример использования класса MainApplication для нового пользователя
                    if (newUser.getRole() != null) {
                        userRole = UserAction.valueOf(newUser.getRole());
                        mainApp.getActualReadings(newUser.getId(), userRole);
                        mainApp.getReadingHistory(newUser.getId(), userRole);
                        mainApp.submitCounterData(newUser); // Добавлен вызов метода ввода данных по счетчику
                        // Добавьте другие действия по мере необходимости
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Пример использования класса MainApplication
        if (newUser != null && mainApp != null) {
            // Проверьте, что newUser и mainApp не являются null, прежде чем использовать их
            Reading newReading = new Reading(newUser.getId(), 100.0, "January");

            // Подаем показания
            mainApp.submitReading(newReading);

            // Получаем актуальные показания
            List<Reading> actualReadings = mainApp.getActualReadings(newUser.getId(), UserAction.valueOf(newUser.getRole()));
            System.out.println("Actual Readings: " + actualReadings);

            // Получаем историю показаний
            List<Reading> historyReadings = mainApp.getReadingHistory(newUser.getId(), UserAction.valueOf(newUser.getRole()));
            System.out.println("Reading History: " + historyReadings);
        }
    }
}