import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


/**
 * Класс UserManager управляет пользователями, их регистрацией и данными.
 * Реализует функции регистрации, проверки существования пользователя и ведения лога действий.
 */
public class UserManager {
    private static final Logger LOGGER = Logger.getLogger(UserManager.class.getName());

    private final List<User> users;         // Список зарегистрированных пользователей
    private final String dataFileName = "user_data.ser"; // Название файла для сохранения данных

    /**
     * Конструктор создает объект UserManager и инициализирует список пользователей данными из файла.
     */
    public UserManager() {
        this.users = readUserDataFromFile();
    }

    /**
     * Метод возвращает список всех зарегистрированных пользователей.
     *
     * @return Список пользователей.
     */
    public List<User> getUsers() {
        return users;
    }

    /**
     * Метод регистрирует нового пользователя.
     *
     * @param user Новый пользователь для регистрации.
     */
    public void registerUser(User user) {
        users.add(user);
        saveUserDataToFile();
        System.out.println(UserAction.User_registered.getDescription());
        logUserAction(user, UserAction.REGISTER);
    }

    /**
     * Метод проверяет существование пользователя по его имени.
     *
     * @param username Имя пользователя для проверки.
     * @return true, если пользователь существует, в противном случае - false.
     */
    public boolean isUserExists(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Метод записывает информацию о действии пользователя в лог.
     *
     * @param user   Пользователь, совершивший действие.
     * @param action Тип действия пользователя.
     */
    private void logUserAction(User user, UserAction action) {
        LOGGER.info("User " + user.getUsername() + " " + action.getDescription());
    }

    /**
     * Метод сохраняет данные пользователей в файл.
     */
    private void saveUserDataToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFileName))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод читает данные пользователей из файла.
     *
     * @return Список пользователей, прочитанный из файла.
     */
    @SuppressWarnings("unchecked")
    private List<User> readUserDataFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFileName))) {
            Object obj = ois.readObject();
            if (obj instanceof List<?>) {
                return (List<User>) obj;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Ошибка во время чтения файла: " + e);
        }
        return new ArrayList<>();
    }
}}


