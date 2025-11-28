-- Vérifier la structure de la table adherent
DESCRIBE adherent;

-- Afficher tous les adhérents avec leurs téléphones
SELECT numero, nom, prenom, telephone, email FROM adherent;

-- Si la colonne telephone n'existe pas encore, l'ajouter
-- ALTER TABLE adherent ADD COLUMN telephone VARCHAR(20);
-- ALTER TABLE adherent ADD COLUMN email VARCHAR(100);

-- Mettre à jour un adhérent existant avec un numéro de téléphone
-- Exemple: UPDATE adherent SET telephone = '0612345678' WHERE numero = '0001';

-- Insérer un adhérent de test pour le client
-- INSERT INTO adherent (numero, nom, prenom, datenaissance, Premium, telephone, email)
-- VALUES ('TEST01', 'Dupont', 'Jean', '1990-01-01', false, '0612345678', 'jean.dupont@test.com');
