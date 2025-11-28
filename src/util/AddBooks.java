package util;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class AddBooks {
    
    public static void main(String[] args) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = Connexion.getConnection();
            String sql = "INSERT INTO livres (ISBN, titre, auteur, categorie, disponibilite) VALUES (?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            
            // Fiction books
            addBook(pstmt, "978-0-06-112008-4", "To Kill a Mockingbird", "Harper Lee", "Fiction", true);
            addBook(pstmt, "978-0-7432-7356-5", "1984", "George Orwell", "Fiction", true);
            addBook(pstmt, "978-0-452-28423-4", "1984", "George Orwell", "Fiction", true); // Same title/author, different ISBN
            addBook(pstmt, "978-0-316-76948-0", "The Catcher in the Rye", "J.D. Salinger", "Fiction", true);
            addBook(pstmt, "978-0-14-028329-5", "The Great Gatsby", "F. Scott Fitzgerald", "Fiction", true);
            
            // Science Fiction
            addBook(pstmt, "978-0-441-17271-9", "Dune", "Frank Herbert", "Science Fiction", true);
            addBook(pstmt, "978-0-553-38034-8", "Dune", "Frank Herbert", "Science Fiction", true); // Same title/author, different ISBN
            addBook(pstmt, "978-0-553-29337-0", "Foundation", "Isaac Asimov", "Science Fiction", true);
            addBook(pstmt, "978-0-345-39180-3", "Ender's Game", "Orson Scott Card", "Science Fiction", true);
            
            // Mystery/Thriller
            addBook(pstmt, "978-0-307-58837-1", "Gone Girl", "Gillian Flynn", "Mystery", true);
            addBook(pstmt, "978-0-316-01681-3", "The Girl with the Dragon Tattoo", "Stieg Larsson", "Mystery", true);
            addBook(pstmt, "978-0-385-53785-8", "The Da Vinci Code", "Dan Brown", "Mystery", true);
            
            // Romance
            addBook(pstmt, "978-0-14-303416-8", "Pride and Prejudice", "Jane Austen", "Romance", true);
            addBook(pstmt, "978-0-553-21311-7", "Pride and Prejudice", "Jane Austen", "Romance", true); // Same title/author, different ISBN
            addBook(pstmt, "978-0-14-243762-9", "Jane Eyre", "Charlotte Bronte", "Romance", true);
            
            // Biography
            addBook(pstmt, "978-1-5011-2738-0", "Steve Jobs", "Walter Isaacson", "Biography", true);
            addBook(pstmt, "978-1-4767-3118-7", "Becoming", "Michelle Obama", "Biography", true);
            
            // Science/Technology
            addBook(pstmt, "978-0-385-50986-1", "A Brief History of Time", "Stephen Hawking", "Science", true);
            addBook(pstmt, "978-0-393-35457-0", "Sapiens", "Yuval Noah Harari", "Science", true);
            addBook(pstmt, "978-0-062-31609-7", "Sapiens", "Yuval Noah Harari", "Science", true); // Same title/author, different ISBN
            
            // Self-Help
            addBook(pstmt, "978-1-5011-7736-1", "Atomic Habits", "James Clear", "Self-Help", true);
            
            System.out.println("✅ Successfully added 20 books to the library!");
            System.out.println("\n📚 Books with duplicate titles (same title/author, different ISBN):");
            System.out.println("   - 1984 by George Orwell (2 copies)");
            System.out.println("   - Dune by Frank Herbert (2 copies)");
            System.out.println("   - Pride and Prejudice by Jane Austen (2 copies)");
            System.out.println("   - Sapiens by Yuval Noah Harari (2 copies)");
            
        } catch (Exception e) {
            System.err.println("❌ Error adding books: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private static void addBook(PreparedStatement pstmt, String isbn, String titre, String auteur, String categorie, boolean disponibilite) {
        try {
            pstmt.setString(1, isbn);
            pstmt.setString(2, titre);
            pstmt.setString(3, auteur);
            pstmt.setString(4, categorie);
            pstmt.setBoolean(5, disponibilite);
            pstmt.executeUpdate();
            System.out.println("✓ Added: " + titre + " by " + auteur + " [" + categorie + "]");
        } catch (Exception e) {
            System.err.println("✗ Failed to add: " + titre + " - " + e.getMessage());
        }
    }
}
