import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Класс Main представляет основное приложение для управления показаниями водосчетчиков.
 * Взаимодействует с UserManager, ReadingManager и другими классами для обработки действий пользователей.
 */
public class Main {
    private static UserManager userManager = new UserManager();
    private static ReadingManager readingManager = new ReadingManager();

    /**
     * Точка входа в программу.
     *
     * @param args Параметры командной строки (не используются).
     * @throws IOException Возможное исключение ввода/вывода при работе с BufferedReader.
     */
    public static void main(String[] args) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                User user = registerOrAuthorize(reader);
                if (user != null) {
                    processActions(user, reader);
                } else {
                    System.out.println("Пользователь не найден. Повторите попытку.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Регистрирует или авторизует пользователя в системе.
     *
     * @param reader BufferedReader для ввода данных пользователем.
     * @return Объект User, представляющий зарегистрированного или авторизованного пользователя.
     * @throws IOException Возможное исключение ввода/вывода при работе с BufferedReader.
     */
    private static User registerOrAuthorize(BufferedReader reader) throws IOException {
        User user = null;
        while (user == null) {
            System.out.println("Нажмите 1 для входа, 2 для регистрации:");
            String choice = reader.readLine();
            if ("1".equals(choice)) {
                user = User.inputUserAuthorization(reader, userManager);
                if (user == null) {
                    System.out.println("Авторизация не удалась. Повторите попытку.");
                }
            } else if ("2".equals(choice)) {
                user = User.inputUserRegistration(reader);
                userManager.registerUser(user);
            } else {
                System.out.println("Некорректный выбор.");
            }
        }
        return user;
    }

    /**
     * Обрабатывает действия пользователя в основном меню приложения.
     *
     * @param user   Текущий пользователь.
     * @param reader BufferedReader для ввода данных пользователем.
     * @throws IOException Возможное исключение ввода/вывода при работе с BufferedReader.
     */
    private static void processActions(User user, BufferedReader reader) throws IOException {
        initAdmin();
        while (true) {
            showMenu(user, reader);
            System.out.print("Введите номер действия: ");
            int inputAction;
            try {
                inputAction = Integer.parseInt(reader.readLine());
            } catch (NumberFormatException e) {
                System.out.println("Некорректный ввод. Попробуйте еще раз.");
                continue;
            }
            switch (inputAction) {
                case 1:
                    submitCounterData(user, reader);
                    break;
                case 2:
                    if (user != null) {
                        System.out.println("Ваши переданные показания: " + user.getSubmittedReadings());
                    } else {
                        System.out.println("Ошибка: пользователь не найден.");
                    }
                    break;
                case 3:
                    return;
                case 4:
                    if (user != null && user.getRole().equals(UserRoles.ADMIN)) {
                        showReadingsOfAllUsers();
                    }
                    break;
                default:
                    System.out.println("Введенное число не соответствует ни одному действию. Попробуйте еще раз.");
                    break;
            }
        }
    }

    /**
     * Отображает меню действий для пользователя.
     *
     * @param user   Текущий пользователь.
     * @param reader BufferedReader для ввода данных пользователем.
     * @return Текущий пользователь.
     * @throws IOException Возможное исключение ввода/вывода при работе с BufferedReader.
     */
    private static User showMenu(User user, BufferedReader reader) throws IOException {
        if (user == null) {
            System.out.println("Ошибка: пользователь не найден.");
            return registerOrAuthorize(reader);
        }
        System.out.println("Выберите действие:");
        System.out.println("1. Подача показаний");
        System.out.println("2. История показаний");
        System.out.println("3. Выйти");
        if (user.getRole().equals(UserRoles.ADMIN)) {
            System.out.println("4. Показать показания всех пользователей");
        }
        return user;
    }

    /**
     * Инициализирует администратора системы.
     */
    private static void initAdmin() {
        User admin = new User("admin", "123", UserRoles.ADMIN);
        userManager.registerUser(admin);
    }

    /**
     * Показывает показания водосчетчиков всех пользователей, кроме администратора.
     */
    private static void showReadingsOfAllUsers() {
        userManager.getUsers().forEach(u -> {
            if (!u.getRole().equals(UserRoles.ADMIN)) {
                System.out.print("Пользователь " + u.getUsername() + ", показания: ");
                List<Reading> readings = u.getSubmittedReadings();
                if (readings.isEmpty()) {
                    System.out.println("[]");
                } else {
                    readings.forEach(r -> System.out.println(r.toString()));
                }
            }
        });
    }
    private static void submitCounterData(User user, BufferedReader reader) throws IOException {
        System.out.println("Введите данные по счетчику:");
        double hotWaterCounter;
        while (true) {
            try {
                System.out.println("Горячая вода:");
                hotWaterCounter = Double.parseDouble(reader.readLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Некорректный ввод. Введите числовое значение: ");
            }
        }

        double coldWaterCounter;
        while (true) {
            try {
                System.out.println("Холодная вода:");
                coldWaterCounter = Double.parseDouble(reader.readLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Некорректный ввод. Введите числовое значение:");
            }
        }


        int month;
        while (true) {
            System.out.println("Введите месяц (от 1 до 12):");
            try {
                month = Integer.parseInt(reader.readLine());
                if (month >= 1 && month <= 12) {
                    break;
                } else {
                    System.out.println("Месяц должен быть в пределах от 1 до 12.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Некорректный ввод. Введите числовое значение:");
            }
        }

        // Создаем объект Reading с введенными данными
        Reading newReading = new Reading(user.getId(), String.valueOf(month), hotWaterCounter, coldWaterCounter);

        // Передаем объект Reading для обработки
        readingManager.submitReading(newReading, user);

        // Добавляем введенное показание в список поданных пользователем
        user.addSubmittedReading(newReading);

        System.out.println("Данные по счетчику успешно добавлены.");
    }


}


