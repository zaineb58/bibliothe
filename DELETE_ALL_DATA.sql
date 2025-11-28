-- Script pour supprimer toutes les données de la bibliothèque

-- Supprimer tous les emprunts (doit être fait en premier à cause des clés étrangères)
DELETE FROM emprunt;

-- Supprimer tous les adhérents
DELETE FROM adherent;

-- Supprimer tous les livres
DELETE FROM livres;

-- Vérification
SELECT 'DONNÉES SUPPRIMÉES' as Status;
SELECT CONCAT('Emprunts restants: ', COUNT(*)) as Result FROM emprunt;
SELECT CONCAT('Adhérents restants: ', COUNT(*)) as Result FROM adherent;
SELECT CONCAT('Livres restants: ', COUNT(*)) as Result FROM livres;
SELECT 'Toutes les données ont été supprimées avec succès!' as Message;
