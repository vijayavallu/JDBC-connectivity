package school3;



import java.sql.*;
import java.util.Scanner;

public class Main {
    private static final String URL = "jdbc:mysql://localhost:3306/school3";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            stmt = conn.createStatement();
            StudentDetails.studentTable(stmt);
            MarksDetails.marksTable(stmt);
            Scanner scanner = new Scanner(System.in);
            int selection;
            do {
                System.out.println("1.Student details\n2.Marks details\n3.exit");
                System.out.print("Enter your choice ");
                selection = scanner.nextInt();
                switch (selection) {
                    case 1:
                        int choice;
                        do {
                            System.out.println("1. Create\n2. Read\n3. Update\n4. Delete\n5. Exit");
                            System.out.print("Enter your choice ");
                            choice = scanner.nextInt();

                            switch (choice) {
                                case 1:
                                    createRecord(stmt, scanner);
                                    break;
                                case 2:
                                    readRecords(stmt);
                                    break;
                                case 3:
                                    updateRecord(stmt, scanner);
                                    break;
                                case 4:
                                    deleteRecord(stmt, scanner);
                                    break;
                            }
                        } while (choice != 5);
                        break;

                    case 2:
                        retrieveMarkRecord(stmt, scanner);
                        break;
                }
            } while (selection <=2 );

            scanner.close();
        } catch (SQLException | ClassNotFoundException se) {
            se.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
    private static void retrieveMarkRecord(Statement stmt, Scanner scanner) throws SQLException {
        System.out.print("Enter the name to read: ");
        String rname = scanner.next();
        String retrieveStudentDetails = "SELECT * FROM school3table3 WHERE name=?";
        try (PreparedStatement pstmt = stmt.getConnection().prepareStatement(retrieveStudentDetails)) {
            pstmt.setString(1, rname);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                System.out.println("Name: " + resultSet.getString("name"));
                System.out.println("English marks: " + resultSet.getInt("english"));
                System.out.println("Maths marks: " + resultSet.getInt("mathematics"));
                System.out.println("Social marks: " + resultSet.getInt("social"));
            } else {
                System.out.println("Student not found.");
            }
        }
    }

    public static class StudentDetails {

        public static void studentTable(Statement stmt) throws SQLException {
            String studentTable = "CREATE TABLE IF NOT EXISTS school3table1 " +
                    "(redgno INT PRIMARY KEY, " +
                    "name VARCHAR(255), " +
                    "class INT, " +
                    "section INT, " +
                    "dob DATE)";

            stmt.executeUpdate(studentTable);
        }
    }

    public static class MarksDetails {

        public static void marksTable(Statement stmt) throws SQLException {
            String marksTable = "CREATE TABLE IF NOT EXISTS school3table3 " +
                    "(name VARCHAR(255), " +
                    "english INT, " +
                    "mathematics INT, " +
                    "science INT, " +
                    "social INT)";

            stmt.executeUpdate(marksTable);
        }
    }

    private static void createRecord(Statement stmt, Scanner scanner) throws SQLException {
        System.out.println("Enter student details:");
        System.out.print("Registration Number: ");
        int regNo = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Class: ");
        int studentClass = scanner.nextInt();
        System.out.print("Section:");
        int section = scanner.nextInt();
        System.out.print("Date of Birth (YYYY-MM-DD): ");
        String dob = scanner.next();
        String insertStudentDetails = "INSERT INTO school3table1 VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = stmt.getConnection().prepareStatement(insertStudentDetails)) {
            pstmt.setInt(1, regNo);
            pstmt.setString(2, name);
            pstmt.setInt(3, studentClass);
            pstmt.setInt(4, section);
            pstmt.setString(5, dob);
            pstmt.executeUpdate();
        }
           
        System.out.println("Student record added successfully.");
        System.out.println("enter the marks details of the student");
        System.out.print("enter the English marks");
        int eng = scanner.nextInt();
        System.out.print("enter the mathematics marks");
        int maths = scanner.nextInt();
        System.out.print("enter the science marks");
        int sci = scanner.nextInt();
        System.out.print("enter the social marks");
        int soc = scanner.nextInt();
        String insertStudentMarks = "INSERT INTO school3table3 VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = stmt.getConnection().prepareStatement(insertStudentMarks)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, eng);
            pstmt.setInt(3, maths);
            pstmt.setInt(4, sci);
            pstmt.setInt(5, soc);
            pstmt.executeUpdate();
        }

        System.out.println("Student marks details added successfully.");
    }

    private static void readRecords(Statement stmt) throws SQLException {
        System.out.println("Student Details:");
        ResultSet resultSet = stmt.executeQuery("SELECT * FROM school7table1");

        while (resultSet.next()) {
            System.out.println("Registration Number: " + resultSet.getInt("redgno"));
            System.out.println("Name: " + resultSet.getString("name"));
            System.out.println("Class: " + resultSet.getInt("class"));
            System.out.println("Section: " + resultSet.getInt("section"));
            System.out.println("Date of Birth: " + resultSet.getString("dob"));
            System.out.println("------------------------------");
        }
    }

    private static void updateRecord(Statement stmt, Scanner scanner) throws SQLException {
        System.out.print("Enter the Registration Number to update: ");
        int regNo = scanner.nextInt();
        System.out.println("Enter new student details:");
        System.out.print("Name: ");
        String name = scanner.next();
        System.out.print("Class: ");
        int studentClass = scanner.nextInt();
        System.out.print("Section: ");
        int section = scanner.nextInt();
        System.out.print("Date of Birth (YYYY-MM-DD): ");
        String dob = scanner.next();
        String updateStudentDetails = "UPDATE school3table1 SET name=?, class=?, section=?, dob=? WHERE redgno=?";
        try (PreparedStatement pstmt = stmt.getConnection().prepareStatement(updateStudentDetails)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, studentClass);
            pstmt.setInt(3, section);
            pstmt.setString(4, dob);
            pstmt.setInt(5, regNo);
            pstmt.executeUpdate();
        }
        System.out.println("Student record updated successfully.");
    }

    private static void deleteRecord(Statement stmt, Scanner scanner) throws SQLException {
        System.out.print("Enter the Registration Number to delete: ");
        int regNo = scanner.nextInt();
        String deleteStudentDetails = "DELETE FROM school7table1 WHERE redgno=?";
        try (PreparedStatement pstmt = stmt.getConnection().prepareStatement(deleteStudentDetails)) {
            pstmt.setInt(1, regNo);
            pstmt.executeUpdate();
        }
        System.out.println("Student record deleted successfully.");
    }
}