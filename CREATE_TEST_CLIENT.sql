-- Script pour créer un adhérent de test complet

-- 1. Vérifier si les colonnes existent
SELECT column_name, data_type, nullable FROM user_tab_columns WHERE table_name = 'ADHERENT';

-- 2. Ajouter les colonnes si elles n'existent pas (décommenter si nécessaire)
-- ALTER TABLE adherent ADD COLUMN telephone VARCHAR(20);
-- ALTER TABLE adherent ADD COLUMN email VARCHAR(100);
-- ALTER TABLE adherent ADD COLUMN dateAjout DATE DEFAULT (CURDATE());

-- 3. Supprimer l'adhérent de test s'il existe déjà
DELETE FROM adherent WHERE numero = 'TEST01';

-- 4. Créer un adhérent de test
INSERT INTO adherent (numero, nom, prenom, datenaissance, Premium, telephone, email, dateAjout)
VALUES ('TEST01', 'Dupont', 'Jean', '1990-01-01', false, '0612345678', 'jean.dupont@biblio.com', CURDATE());

-- 5. Vérifier que l'adhérent a été créé
SELECT * FROM adherent WHERE numero = 'TEST01';

-- 6. Pour se connecter en tant que client :
-- Nom d'utilisateur : Jean Dupont
-- Mot de passe (téléphone) : 0612345678
