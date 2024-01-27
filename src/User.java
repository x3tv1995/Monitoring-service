import java.io.BufferedReader;
import java.io.IOException;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class User implements Serializable {
    private static final long serialVersionUID = -8054866143814427354L;
    private static final AtomicInteger UserCounter = new AtomicInteger(0); // Счетчик пользователей
    private final int Id;
    private String username;
    private String password;


    public void setRole(UserRoles role) {
        this.role = role;
    }

    private UserRoles role;

    public User(String username, String password, UserRoles role) {
        Id = UserCounter.incrementAndGet();
        this.username = username;
        this.password = password;
        this.role = role;
    }


    public int getId() {
        return Id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public UserRoles getRole() {
        return role;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static User inputUserAuthorization(BufferedReader reader, UserManager userManager) {

        try {
            System.out.println("Введите имя пользователя:");
            String username = reader.readLine();

            System.out.println("Введите пароль:");
            String password = reader.readLine();


            for (User user : userManager.getUsers()) {
                if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                    System.out.println("Авторизация успешна.");
                    // Установка роли пользователя
                    return user;
                }
            }


            System.out.println("Пользователь с введенными данными не найден. Вам нужно зарегистрироваться");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static User inputUserRegistration(BufferedReader reader) {
        try {
            System.out.println("Введите имя нового пользователя:");
            String username = reader.readLine();
            System.out.println("Введите пароль:");
            String password = reader.readLine();
            User user = new User(username, password, UserRoles.USER);
            user.setUsername(username);
            user.setPassword(password);
            return user;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
