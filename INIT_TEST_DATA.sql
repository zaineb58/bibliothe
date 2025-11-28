-- Script pour réinitialiser et ajouter des données de test

-- 1. Supprimer toutes les données existantes (dans l'ordre à cause des clés étrangères)
DELETE FROM emprunt;
DELETE FROM adherent;
DELETE FROM livres;

-- 2. Ajouter des adhérents de test
INSERT INTO adherent (numero, nom, prenom, datenaissance, Premium) VALUES
('A001', 'Alami', 'Hassan', '1995-03-15', TRUE),
('A002', 'Bennani', 'Fatima', '1998-07-22', FALSE),
('A003', 'Chakir', 'Mohammed', '1990-11-08', TRUE),
('A004', 'Idrissi', 'Amina', '2000-01-30', FALSE),
('A005', 'Mansouri', 'Youssef', '1992-05-18', TRUE);

-- 3. Ajouter des livres de test avec plusieurs copies
INSERT INTO livres (ISBN, titre, auteur, categorie, disponibilite, nombreCopies, copiesDisponibles) VALUES
('978-1-234-56789-0', 'Le Petit Prince', 'Antoine de Saint-Exupéry', 'Roman', TRUE, 5, 5),
('978-1-234-56789-1', 'Les Misérables', 'Victor Hugo', 'Roman', TRUE, 3, 3),
('978-1-234-56789-2', 'Harry Potter à l''école des sorciers', 'J.K. Rowling', 'Fantasy', TRUE, 4, 4),
('978-1-234-56789-3', 'Le Seigneur des Anneaux', 'J.R.R. Tolkien', 'Fantasy', TRUE, 2, 2),
('978-1-234-56789-4', '1984', 'George Orwell', 'Science-Fiction', TRUE, 3, 3),
('978-1-234-56789-5', 'Pride and Prejudice', 'Jane Austen', 'Romance', TRUE, 2, 2),
('978-1-234-56789-6', 'To Kill a Mockingbird', 'Harper Lee', 'Roman', TRUE, 3, 3),
('978-1-234-56789-7', 'L''Étranger', 'Albert Camus', 'Philosophie', TRUE, 4, 4),
('978-1-234-56789-8', 'Candide', 'Voltaire', 'Philosophie', TRUE, 2, 2),
('978-1-234-56789-9', 'Don Quichotte', 'Miguel de Cervantes', 'Roman', TRUE, 1, 1);

-- 4. Vérifier les données insérées
SELECT 'Adhérents créés:' as Info, COUNT(*) as Total FROM adherent
UNION ALL
SELECT 'Livres créés:', COUNT(*) FROM livres
UNION ALL
SELECT 'Emprunts créés:', COUNT(*) FROM emprunt;

-- 5. Afficher les détails des livres
SELECT ISBN, titre, auteur, nombreCopies, copiesDisponibles FROM livres ORDER BY titre;

-- 6. Afficher les adhérents
SELECT numero, nom, prenom, Premium FROM adherent ORDER BY nom;
