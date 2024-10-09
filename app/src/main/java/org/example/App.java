package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class App {
    // URL для подключения к базе данных Firebird с указанием кодировки UTF-8
    private static final String URL = "jdbc:firebirdsql://localhost:3050/C:/databases/heat_company.fbd?encoding=UTF8";
    // Имя пользователя для подключения к базе данных
    private static final String USER = "sysdba";
    // Пароль для подключения к базе данных
    private static final String PASSWORD = "bd";

    public static void main(String[] args) {
        // Создаем экземпляр класса App
        App app = new App();
        // Вставляем данные в таблицу clients
        app.insertClient();
        // Просматриваем данные из таблицы clients
        app.viewClients();
    }

    public void insertClient() {
        // Подключаемся к базе данных
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.println("Connected to Firebird database!");

            // SQL-запрос для получения последнего номера личного счета
            String selectSQL = "SELECT MAX(account_number) AS max_account_number FROM clients";
            int newAccountNumber = 1; // Начальное значение, если таблица пуста

            // Выполняем запрос для получения последнего номера личного счета
            try (PreparedStatement pstmt = connection.prepareStatement(selectSQL);
                 ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    newAccountNumber = rs.getInt("max_account_number") + 1;
                }
            }

            // SQL-запрос для вставки данных в таблицу clients
            String insertSQL = "INSERT INTO clients (account_number, last_name, first_name, middle_name, address, current_charges, previous_month_debt, discount_coefficient) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            // Подготавливаем запрос
            try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
                // Устанавливаем значения для параметров запроса
                pstmt.setInt(1, newAccountNumber);
                pstmt.setString(2, "Іванов");
                pstmt.setString(3, "Іван");
                pstmt.setString(4, "Іванович");
                pstmt.setString(5, "вул. Шевченка, 1");
                pstmt.setDouble(6, 500.75);
                pstmt.setDouble(7, 200.50);
                pstmt.setDouble(8, 0.85);
                // Выполняем запрос на вставку данных
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            // Обрабатываем исключения, если они возникнут
            e.printStackTrace();
        }
    }

    public void viewClients() {
        // Подключаемся к базе данных
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // SQL-запрос для выбора всех данных из таблицы clients
            String selectSQL = "SELECT * FROM clients";
            // Подготавливаем и выполняем запрос
            try (PreparedStatement pstmt = connection.prepareStatement(selectSQL);
                 ResultSet rs = pstmt.executeQuery()) {
                // Проходим по всем строкам результата запроса
                while (rs.next()) {
                    // Выводим значения столбцов для каждой строки
                    System.out.println("Особистий рахунок: " + rs.getInt("account_number"));
                    System.out.println("Прізвище: " + rs.getString("last_name"));
                    System.out.println("Ім'я: " + rs.getString("first_name"));
                    System.out.println("По батькові: " + rs.getString("middle_name"));
                    System.out.println("Адреса приміщення: " + rs.getString("address"));
                    System.out.println("Поточне нарахування за місяць: " + rs.getDouble("current_charges"));
                    System.out.println("Заборгованість за попередній місяць: " + rs.getDouble("previous_month_debt"));
                    System.out.println("Коефіцієнт для нарахування пільги: " + rs.getDouble("discount_coefficient"));
                    System.out.println("---------------------------------------------------");
                }
            }
        } catch (SQLException e) {
            // Обрабатываем исключения, если они возникнут
            e.printStackTrace();
        }
    }
}
