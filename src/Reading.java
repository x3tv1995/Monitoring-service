import java.io.Serializable;


// Класс, представляющий показания счетчика

/**
 * Класс Reading представляет сущность для хранения данных о показаниях водосчетчика.
 * Реализует интерфейс Serializable для обеспечения возможности сериализации объектов.
 */
public class Reading implements Serializable {
    private final int userId;


    private final int month;

    private double hotWaterCounter;
    private double coldWaterCounter;

    /**
     * Конструктор для создания объекта Reading с заданными параметрами.
     *
     * @param userId           Идентификатор пользователя.
     * @param month            Месяц, к которому относятся показания.
     * @param hotWaterCounter  Значение счетчика горячей воды.
     * @param coldWaterCounter Значение счетчика холодной воды.
     */
    public Reading(int userId, int month, double hotWaterCounter, double coldWaterCounter) {
        this.userId = userId;
        this.month = month;
        this.hotWaterCounter = hotWaterCounter;
        this.coldWaterCounter = coldWaterCounter;
    }

    /**
     * Получение идентификатора пользователя.
     *
     * @return Идентификатор пользователя.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Получение значения счетчика горячей воды.
     *
     * @return Значение счетчика горячей воды.
     */
    public double getHotWaterCounter() {
        return hotWaterCounter;
    }

    /**
     * Получение значения счетчика холодной воды.
     *
     * @return Значение счетчика холодной воды.
     */
    public double getColdWaterCounter() {
        return coldWaterCounter;
    }

    /**
     * Получение месяца, к которому относятся показания.
     *
     * @return Месяц.
     */
    public int getMonth() {
        return month;
    }


    /**
     * Переопределенный метод toString для возврата строкового представления объекта Reading.
     *
     * @return Строковое представление объекта Reading.
     */
    @Override
    public String toString() {
        return "Reading{" +
                "userId=" + userId +
                ", hotWaterCounter=" + hotWaterCounter +
                ", coldWaterCounter=" + coldWaterCounter +
                ", month='" + month + '\'' +
                '}';
    }
}

/**
 * Класс ReadingManager управляет данными о показаниях водосчетчиков.
 * Включает методы для добавления, получения и обработки данных о показаниях.
 */






