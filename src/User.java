import java.io.BufferedReader;
import java.io.IOException;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class User implements Serializable {
    private static final long serialVersionUID = -8054866143814427354L;
    private static final AtomicInteger UserCounter = new AtomicInteger(0); // Счетчик пользователей
    private final int Id;

    private String username;
    private String password;
    private UserRoles role;
    private List<Reading> submittedReadings;

    public User(String username, String password, UserRoles role) {
        Id = UserCounter.incrementAndGet();
        this.username = username;
        this.password = password;
        this.role = role;
        this.submittedReadings = new ArrayList<>();
    }

    private LocalDateTime lastSubmissionDate;




    public List<Reading> getSubmittedReadings() {
        return submittedReadings;
    }

    public void addSubmittedReading(Reading reading) {
        submittedReadings.add(reading);
    }

    public LocalDateTime getLastSubmissionDate() {
        return lastSubmissionDate;
    }

    public void setLastSubmissionDate(LocalDateTime lastSubmissionDate) {
        this.lastSubmissionDate = lastSubmissionDate;
    }


    public void setRole(UserRoles role) {
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
            if (userManager.isUserExists(username)) {
                if ("admin".equals(username)) {

                    System.out.println("Введите пароль для администратора:");
                    String password = reader.readLine();


                    if ("123".equals(password)) {
                        System.out.println("Вы успешно вошли как администратор!");
                        return new User(username, password, UserRoles.ADMIN);
                    } else {
                        System.out.println("Неверный пароль для администратора. Попробуйте еще раз.");
                        return null;
                    }

                }
                System.out.println("Введите пароль:");
                String password = reader.readLine();


                for (User user : userManager.getUsers()) {
                    if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                        System.out.println("Авторизация успешна.");
                        return user;
                    }
                }
            }

            System.out.println("Пользователь с введенными данными не найден. Вам нужно зарегистрироваться");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;


    }

        public static User inputUserRegistration (BufferedReader reader,UserManager userManager){
            try {
                System.out.println("Введите имя пользователя:");
                String username = reader.readLine();


                if (userManager.isUserExists(username)) {
                    System.out.println("Пользователь с таким именем уже зарегистрирован. Попробуйте другое имя.");
                    return null;
                }

                System.out.println("Введите пароль:");
                String password = reader.readLine();

                // Возвращаем новый объект User для нового пользователя
                return new User(username, password, UserRoles.USER);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            return null;
        }

    public boolean hasSubmittedReadingForMonth(int month) {
        return submittedReadings.stream().anyMatch(reading -> reading.getMonth() == month);
    }}



