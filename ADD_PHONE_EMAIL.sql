-- Ajouter les colonnes telephone et email à la table adherent (MySQL)
ALTER TABLE adherent ADD telephone VARCHAR(20);
ALTER TABLE adherent ADD email VARCHAR(100);
ALTER TABLE adherent ADD dateAjout DATE;

-- Mettre à jour les données existantes avec des valeurs par défaut si nécessaire
UPDATE adherent SET telephone = '0000000000' WHERE telephone IS NULL OR telephone = '';
UPDATE adherent SET email = CONCAT(LOWER(prenom), '.', LOWER(nom), '@biblio.com') WHERE email IS NULL OR email = '';
UPDATE adherent SET dateAjout = CURDATE() WHERE dateAjout IS NULL;

-- Afficher les adhérents mis à jour
SELECT * FROM adherent;
