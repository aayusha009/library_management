import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;


public class LibraryManagement {

    private static final String URL = "jdbc:postgresql://localhost:5433/library";
    private static final String USER = "postgres"; 
    private static final String PASSWORD = "postgres"; 

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            while(true) {
                System.out.println("\nLibrary Management System");
                System.out.println("1. Add Author");
                System.out.println("2. Add Book");
                System.out.println("3. Add Member");
                System.out.println("4. View Authors");
                System.out.println("5. View Books");
                System.out.println("6. View Members");
                System.out.println("7. Update Author");
                System.out.println("8. Update Book");
                System.out.println("9. Update Member");
                System.out.println("10. Delete Author");
                System.out.println("11. Delete Book");
                System.out.println("12. Delete Member");
                System.out.println("13. Exit");

                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch(choice) {
                    case 1:
                        addAuthor(scanner);
                        break;
                    case 2:
                        addBook(scanner);
                        break;
                    case 3:
                        addMember(scanner);
                        break;
                    case 4:
                        viewAuthors();
                        break;
                    case 5:
                        viewBooks();
                        break;
                    case 6:
                        viewMembers();
                        break;
                    case 7:
                        updateAuthor(scanner);
                        break;
                    case 8:
                        updateBook(scanner);
                        break;
                    case 9:
                        updateMember(scanner);
                        break;
                    case 10:
                        deleteAuthor(scanner);
                        break;
                    case 11:
                        deleteBook(scanner);
                        break;
                    case 12:
                        deleteMember(scanner);
                        break;
                    case 13:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid option, try again.");
                        break;
                }
            }
        }
    }

   
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

  
    private static void addAuthor(Scanner scanner) {
        System.out.print("Enter author name: ");
        String name = scanner.nextLine();
        String sql = "INSERT INTO authors (name) VALUES (?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            System.out.println("Author added successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding author: " + e.getMessage());
        }
    }

    
    private static void addBook(Scanner scanner) {
        System.out.print("Enter book title: ");
        String title = scanner.nextLine();
        System.out.print("Enter author ID: ");
        int authorId = scanner.nextInt();
        System.out.print("Enter published year: ");
        int year = scanner.nextInt();
        String sql = "INSERT INTO books (title, author_id, published_year) VALUES (?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setInt(2, authorId);
            pstmt.setInt(3, year);
            pstmt.executeUpdate();
            System.out.println("Book added successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding book: " + e.getMessage());
        }
    }

    
    private static void addMember(Scanner scanner) {
        System.out.print("Enter member full name: ");
        String fullName = scanner.nextLine();
        String sql = "INSERT INTO members (full_name) VALUES (?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, fullName);
            pstmt.executeUpdate();
            System.out.println("Member added successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding member: " + e.getMessage());
        }
    }

    
    private static void viewAuthors() {
        String sql = "SELECT * FROM authors";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\nAuthors:");
            while (rs.next()) {
                System.out.println(rs.getInt("author_id") + ": " + rs.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println("Error viewing authors: " + e.getMessage());
        }
    }

    
    private static void viewBooks() {
        String sql = "SELECT b.book_id, b.title, a.name as author_name, b.published_year FROM books b " +
                    "JOIN authors a ON b.author_id = a.author_id";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\nBooks:");
            while (rs.next()) {
                System.out.println(rs.getInt("book_id") + ": " + rs.getString("title") + 
                                  " (Author: " + rs.getString("author_name") + 
                                  ", Year: " + rs.getInt("published_year") + ")");
            }
        } catch (SQLException e) {
            System.out.println("Error viewing books: " + e.getMessage());
        }
    }

   
    private static void viewMembers() {
        String sql = "SELECT * FROM members";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\nMembers:");
            while (rs.next()) {
                System.out.println(rs.getInt("member_id") + ": " + rs.getString("full_name") + 
                                 " (Joined: " + rs.getDate("join_date") + ")");
            }
        } catch (SQLException e) {
            System.out.println("Error viewing members: " + e.getMessage());
        }
    }

    
    private static void updateAuthor(Scanner scanner) {
        System.out.print("Enter author ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter new author name: ");
        String name = scanner.nextLine();
        String sql = "UPDATE authors SET name = ? WHERE author_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, id);
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Author updated successfully.");
            } else {
                System.out.println("No author found with ID: " + id);
            }
        } catch (SQLException e) {
            System.out.println("Error updating author: " + e.getMessage());
        }
    }

    
    private static void updateBook(Scanner scanner) {
        System.out.print("Enter book ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter new book title: ");
        String title = scanner.nextLine();
        System.out.print("Enter new author ID (or 0 to keep current): ");
        int authorId = scanner.nextInt();
        System.out.print("Enter new published year (or 0 to keep current): ");
        int year = scanner.nextInt();
        
        StringBuilder sqlBuilder = new StringBuilder("UPDATE books SET ");
        boolean needComma = false;
        
        if (!title.trim().isEmpty()) {
            sqlBuilder.append("title = ?");
            needComma = true;
        }
        
        if (authorId > 0) {
            if (needComma) sqlBuilder.append(", ");
            sqlBuilder.append("author_id = ?");
            needComma = true;
        }
        
        if (year > 0) {
            if (needComma) sqlBuilder.append(", ");
            sqlBuilder.append("published_year = ?");
        }
        
        sqlBuilder.append(" WHERE book_id = ?");
        String sql = sqlBuilder.toString();

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            int paramIndex = 1;
            
            if (!title.trim().isEmpty()) {
                pstmt.setString(paramIndex++, title);
            }
            
            if (authorId > 0) {
                pstmt.setInt(paramIndex++, authorId);
            }
            
            if (year > 0) {
                pstmt.setInt(paramIndex++, year);
            }
            
            pstmt.setInt(paramIndex, id);
            
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Book updated successfully.");
            } else {
                System.out.println("No book found with ID: " + id);
            }
        } catch (SQLException e) {
            System.out.println("Error updating book: " + e.getMessage());
        }
    }

    
    private static void updateMember(Scanner scanner) {
        System.out.print("Enter member ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter new member full name: ");
        String fullName = scanner.nextLine();
        String sql = "UPDATE members SET full_name = ? WHERE member_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, fullName);
            pstmt.setInt(2, id);
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Member updated successfully.");
            } else {
                System.out.println("No member found with ID: " + id);
            }
        } catch (SQLException e) {
            System.out.println("Error updating member: " + e.getMessage());
        }
    }

    
    private static void deleteAuthor(Scanner scanner) {
        System.out.print("Enter author ID to delete: ");
        int id = scanner.nextInt();
        
        try (Connection conn = getConnection()) {
           
            String checkSql = "SELECT COUNT(*) FROM books WHERE author_id = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, id);
                ResultSet rs = checkStmt.executeQuery();
                rs.next();
                int bookCount = rs.getInt(1);
                
                if (bookCount > 0) {
                    System.out.println("Cannot delete author with ID " + id + " because there are " + 
                                      bookCount + " books associated with this author.");
                    return;
                }
            }
            
            String deleteSql = "DELETE FROM authors WHERE author_id = ?";
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                deleteStmt.setInt(1, id);
                int rowsDeleted = deleteStmt.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println("Author deleted successfully.");
                } else {
                    System.out.println("No author found with ID: " + id);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error deleting author: " + e.getMessage());
        }
    }

    private static void deleteBook(Scanner scanner) {
        System.out.print("Enter book ID to delete: ");
        int id = scanner.nextInt();
        String sql = "DELETE FROM books WHERE book_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Book deleted successfully.");
            } else {
                System.out.println("No book found with ID: " + id);
            }
        } catch (SQLException e) {
            System.out.println("Error deleting book: " + e.getMessage());
        }
    }

   
    private static void deleteMember(Scanner scanner) {
        System.out.print("Enter member ID to delete: ");
        int id = scanner.nextInt();
        String sql = "DELETE FROM members WHERE member_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Member deleted successfully.");
            } else {
                System.out.println("No member found with ID: " + id);
            }
        } catch (SQLException e) {
            System.out.println("Error deleting member: " + e.getMessage());
        }
    }
}
