-- Script pour ajouter les colonnes nombreCopies et copiesDisponibles à la table livres

-- Ajouter la colonne nombreCopies (nombre total de copies)
ALTER TABLE livres ADD nombreCopies NUMBER DEFAULT 1;

-- Ajouter la colonne copiesDisponibles (copies disponibles pour emprunt)
ALTER TABLE livres ADD copiesDisponibles NUMBER DEFAULT 1;

-- Mettre à jour les livres existants avec des valeurs par défaut
UPDATE livres SET nombreCopies = 1, copiesDisponibles = 1 WHERE nombreCopies IS NULL;

-- Note: Exécutez ce script dans votre base de données MySQL avant de lancer l'application
