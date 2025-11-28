-- Add 20 books with different categories
-- Including some books with same title/author but different ISBN

-- Fiction books
INSERT INTO livres (ISBN, titre, auteur, categorie, disponibilite) VALUES
('978-0-06-112008-4', 'To Kill a Mockingbird', 'Harper Lee', 'Fiction', TRUE),
('978-0-7432-7356-5', '1984', 'George Orwell', 'Fiction', TRUE),
('978-0-452-28423-4', '1984', 'George Orwell', 'Fiction', TRUE), -- Same title/author, different ISBN
('978-0-316-76948-0', 'The Catcher in the Rye', 'J.D. Salinger', 'Fiction', TRUE),
('978-0-14-028329-5', 'The Great Gatsby', 'F. Scott Fitzgerald', 'Fiction', TRUE);

-- Science Fiction
INSERT INTO livres (ISBN, titre, auteur, categorie, disponibilite) VALUES
('978-0-441-17271-9', 'Dune', 'Frank Herbert', 'Science Fiction', TRUE),
('978-0-553-38034-8', 'Dune', 'Frank Herbert', 'Science Fiction', TRUE), -- Same title/author, different ISBN
('978-0-553-29337-0', 'Foundation', 'Isaac Asimov', 'Science Fiction', TRUE),
('978-0-345-39180-3', 'Ender''s Game', 'Orson Scott Card', 'Science Fiction', TRUE);

-- Mystery/Thriller
INSERT INTO livres (ISBN, titre, auteur, categorie, disponibilite) VALUES
('978-0-307-58837-1', 'Gone Girl', 'Gillian Flynn', 'Mystery', TRUE),
('978-0-316-01681-3', 'The Girl with the Dragon Tattoo', 'Stieg Larsson', 'Mystery', TRUE),
('978-0-385-53785-8', 'The Da Vinci Code', 'Dan Brown', 'Mystery', TRUE);

-- Romance
INSERT INTO livres (ISBN, titre, auteur, categorie, disponibilite) VALUES
('978-0-14-303416-8', 'Pride and Prejudice', 'Jane Austen', 'Romance', TRUE),
('978-0-553-21311-7', 'Pride and Prejudice', 'Jane Austen', 'Romance', TRUE), -- Same title/author, different ISBN
('978-0-14-243762-9', 'Jane Eyre', 'Charlotte Bronte', 'Romance', TRUE);

-- Biography
INSERT INTO livres (ISBN, titre, auteur, categorie, disponibilite) VALUES
('978-1-5011-2738-0', 'Steve Jobs', 'Walter Isaacson', 'Biography', TRUE),
('978-1-4767-3118-7', 'Becoming', 'Michelle Obama', 'Biography', TRUE);

-- Science/Technology
INSERT INTO livres (ISBN, titre, auteur, categorie, disponibilite) VALUES
('978-0-385-50986-1', 'A Brief History of Time', 'Stephen Hawking', 'Science', TRUE),
('978-0-393-35457-0', 'Sapiens', 'Yuval Noah Harari', 'Science', TRUE),
('978-0-062-31609-7', 'Sapiens', 'Yuval Noah Harari', 'Science', TRUE); -- Same title/author, different ISBN

-- Self-Help
INSERT INTO livres (ISBN, titre, auteur, categorie, disponibilite) VALUES
('978-1-5011-7736-1', 'Atomic Habits', 'James Clear', 'Self-Help', TRUE);

SELECT 'Successfully added 20 books to the library!' as Status;

-- Display the newly added books
SELECT * FROM livres ORDER BY categorie, titre;
