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
            initAdmin();
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
        boolean isAuthenticated = false;

        while (!isAuthenticated) {
            System.out.println("Нажмите 1 для входа, 2 для регистрации:");
            String choice = reader.readLine();

            if ("1".equals(choice)) {
                user = User.inputUserAuthorization(reader, userManager);
                isAuthenticated = (user != null);
                if (user == null) {
                    System.out.println("Авторизация не удалась. Повторите попытку.");
                }
            } else if ("2".equals(choice)) {
                user = User.inputUserRegistration(reader, userManager);
                if (user != null) {
                    userManager.registerUser(user);
                    isAuthenticated = true;
                } else {
                    System.out.println("Регистрация не удалась. Попробуйте еще раз.");
                }
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
    static void processActions(User user, BufferedReader reader) throws IOException {

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
    static User showMenu(User user, BufferedReader reader) throws IOException {
        if (user == null) {
            System.out.println("Ошибка: пользователь не найден.");
            return registerOrAuthorize(reader);
        }

        System.out.println("Выберите действие:");

        if (user.getRole().equals(UserRoles.USER)) {

            System.out.println("1. Подача показаний");
            System.out.println("2. История показаний");
        }

        System.out.println("3. Выйти");

        if (user.getRole().equals(UserRoles.ADMIN)) {

            System.out.println("4. Показать показания всех пользователей");

        }

        return user;
    }

    /**
     * Инициализирует администратора системы.
     */
    protected static void initAdmin() {
        User admin = new User("admin", "123", UserRoles.ADMIN);
        userManager.registerUser(admin);
    }

    /**
     * Показывает показания водосчетчиков всех пользователей, кроме администратора.
     */
    static void showReadingsOfAllUsers() {
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

    /**
     * Метод для ввода данных по счетчику от пользователя.
     *
     * @param user   Пользователь, для которого вводятся данные.
     * @param reader BufferedReader для считывания ввода пользователя.
     * @throws IOException              Возможное исключение ввода/вывода при работе с BufferedReader.
     * @throws NumberFormatException    Исключение, возникающее при некорректном вводе числовых значений.
     * @throws IllegalArgumentException Исключение, возникающее при вводе данных, не соответствующих допустимым пределам.
     */
    static void submitCounterData(User user, BufferedReader reader) throws IOException {
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


        if (user.hasSubmittedReadingForMonth(month)) {
            System.out.println("Вы уже подали показания за этот месяц. Попробуйте в следующий раз.");
            return;
        }

        Reading newReading = new Reading(user.getId(), month, hotWaterCounter, coldWaterCounter);


        readingManager.submitReading(newReading, user);

        System.out.println("Данные по счетчику успешно добавлены.");
    }


}


