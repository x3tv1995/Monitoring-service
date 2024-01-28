import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Main {
    private static UserManager userManager = new UserManager();
    private static ReadingManager readingManager = new ReadingManager();

    public static void main(String[] args) throws IOException {
        User user = null;
        MainApplication mainApp = null;

        BufferedReader reader = null;
        try {

            reader = new BufferedReader(new InputStreamReader(System.in));
            user = initMenu(reader);
            mainApp = processLogin(user, userManager, readingManager, reader);

            if (mainApp != null) {
                // Остальной код приложения
                Reading newReading = new Reading(user.getId(), 100.0, "January");

                // Подаем показания
                mainApp.submitReading(newReading, user);

                // Получаем актуальные показания
                mainApp.getActualReadings(user);

                // Получаем историю показаний
                mainApp.getReadingHistory(user);
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        // Пример использования класса MainApplication
        if (user != null && mainApp != null) {
            // Проверьте, что user и mainApp не являются null, прежде чем использовать их
            Reading newReading = new Reading(user.getId(), 100.0, "January");

            // Подаем показания
            mainApp.submitReading(newReading, user);

            // Получаем актуальные показания
            List<Reading> actualReadings = mainApp.getActualReadings(user);
            System.out.println("Actual Readings: " + actualReadings);

            // Получаем историю показаний
            List<Reading> historyReadings = mainApp.getReadingHistory(user);
            System.out.println("Reading History: " + historyReadings);
        }
    }

    private static MainApplication processLogin(User user, UserManager userManager, ReadingManager readingManager, BufferedReader reader) throws IOException {
        MainApplication mainApp = null;

        if (user != null) {
            mainApp = new MainApplication(userManager, readingManager);

            // Вход выполнен успешно, выполните нужные действия
            // Например, получите показания, историю и т.д.
            showUserMenu(user, mainApp, reader);
            mainApp.getActualReadings(user);
            mainApp.getReadingHistory(user);

            mainApp.submitCounterData(user, reader); // Добавлен вызов метода ввода данных по счетчику
            // Добавьте другие действия по мере необходимости
        }

        return mainApp;
    }

    private static User initMenu(BufferedReader reader) throws IOException {
        boolean str = true;
        User user = null;

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

    private static User showUserMenu(User user, MainApplication mainApp, BufferedReader reader) throws IOException {
        boolean exitMenu = false;


        try {
            while (!exitMenu) {
                System.out.println("Выберите действие:");
                System.out.println("1. Подача показаний");
                System.out.println("2. История показаний");
                System.out.println("3. Выйти");


                String choice = reader.readLine();
                switch (choice) {
                    case "1":
                        mainApp.submitCounterData(user, reader);
                        break;
                    case "2":
                        initMenu(reader);
                        break;
                    case "3":
                        showReadingHistory(user);
                        break;
                    default:
                        System.out.println("Некорректный выбор.");
                        break;
                }
            }
        } finally {
            // Закрываем BufferedReader в блоке finally
            if (reader != null) {
                reader.close();
            }
        }
        return user;
    }

    private static void showReadingHistory(User user) {
        List<Reading> historyReadings = user.getSubmittedReadings();
        if (historyReadings.isEmpty()) {
            System.out.println("У вас нет поданных показаний.");
        } else {
            System.out.println("История поданных показаний:");
            for (Reading reading : historyReadings) {
                System.out.println("Месяц: " + reading.getMonth() + ", Показания: " + reading.getCounterValue());
            }
        }
    }
}