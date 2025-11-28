-- Script complet pour configurer la base de données avec le système de copies

-- ÉTAPE 1: Ajouter les colonnes si elles n'existent pas
ALTER TABLE livres ADD nombreCopies NUMBER DEFAULT 1;
ALTER TABLE livres ADD copiesDisponibles NUMBER DEFAULT 1;

-- ÉTAPE 2: Mettre à jour les livres existants
UPDATE livres SET nombreCopies = 1 WHERE nombreCopies IS NULL;
UPDATE livres SET copiesDisponibles = 1 WHERE copiesDisponibles IS NULL;

-- ÉTAPE 3: Supprimer toutes les données
DELETE FROM emprunt;
DELETE FROM adherent;
DELETE FROM livres;

-- ÉTAPE 4: Réinitialiser les sequences (optionnel pour Oracle)
-- ALTER SEQUENCE emprunt_seq RESTART START WITH 1;

-- ÉTAPE 5: Ajouter des adhérents de test
INSERT INTO adherent (numero, nom, prenom, datenaissance, Premium) VALUES
('A001', 'Alami', 'Hassan', '1995-03-15', TRUE),
('A002', 'Bennani', 'Fatima', '1998-07-22', FALSE),
('A003', 'Chakir', 'Mohammed', '1990-11-08', TRUE),
('A004', 'Idrissi', 'Amina', '2000-01-30', FALSE),
('A005', 'Mansouri', 'Youssef', '1992-05-18', TRUE);

-- ÉTAPE 6: Ajouter des livres de test avec plusieurs copies
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

-- ÉTAPE 7: Vérification
SELECT '=== VÉRIFICATION DES DONNÉES ===' as message FROM dual;
SELECT 'Adhérents: ' || COUNT(*) as total FROM adherent;
SELECT 'Livres: ' || COUNT(*) as total FROM livres;
SELECT 'Emprunts: ' || COUNT(*) as total FROM emprunt;

SELECT '=== DÉTAILS DES LIVRES ===' as message FROM dual;
SELECT titre, nombreCopies, copiesDisponibles, disponibilite FROM livres ORDER BY titre;

SELECT '=== DÉTAILS DES ADHÉRENTS ===' as message FROM dual;
SELECT numero, nom || ' ' || prenom as nom_complet, CASE WHEN Premium=1 THEN 'Premium' ELSE 'Standard' END as type_adherent FROM adherent ORDER BY nom;
