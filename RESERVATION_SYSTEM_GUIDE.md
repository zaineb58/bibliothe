# Guide d'Installation du Système de Réservation

## 📋 Étape 1: Créer la table reservation dans la base de données

### Option 1: Via MySQL Workbench (Recommandé)
1. Ouvrez MySQL Workbench
2. Connectez-vous à votre serveur MySQL (root)
3. Sélectionnez la base de données `bibliothe`
4. Ouvrez le fichier `CREATE_RESERVATION_TABLE.sql`
5. Cliquez sur l'icône éclair ⚡ pour exécuter le script
6. Vérifiez que le message "Reservation table created successfully!" apparaît

### Option 2: Via ligne de commande MySQL
```bash
mysql -u root -p bibliothe < CREATE_RESERVATION_TABLE.sql
```

### Option 3: Copier-coller manuel
1. Ouvrez MySQL Workbench ou tout client MySQL
2. Sélectionnez la base `bibliothe`
3. Copiez et exécutez le contenu de `CREATE_RESERVATION_TABLE.sql`

## ✅ Vérification
Exécutez cette requête pour vérifier que la table existe :
```sql
SHOW TABLES LIKE 'reservation';
DESCRIBE reservation;
```

## 🚀 Étape 2: Lancer l'application
Une fois la table créée, lancez l'application normalement :
```bash
java --module-path "C:\Users\wassi\Downloads\openjfx-25.0.1_windows-x64_bin-sdk\javafx-sdk-25.0.1\lib" --add-modules javafx.controls,javafx.fxml -cp "lib\mysql-connector-j-8.3.0.jar;bin" Main
```

## 📚 Fonctionnalités du Système de Réservation

### Pour les Membres (Clients):
1. **Réserver un livre indisponible**
   - Cliquez sur un livre emprunté
   - Cliquez sur "📌 Reserve this Book"
   - Vous êtes ajouté à la file d'attente (FIFO)
   - Vous voyez votre position dans la queue

2. **Voir vos réservations**
   - Allez dans "Personal Info"
   - Section "📌 My Reservations"
   - Voir la position dans la queue
   - Annuler une réservation si besoin

3. **Attribution automatique**
   - Quand le livre est retourné, la première personne en queue le reçoit
   - Vous avez 3 jours pour emprunter le livre attribué
   - Si vous ne l'empruntez pas, la réservation expire

### Pour les Administrateurs:
- Les réservations sont traitées automatiquement lors du retour des livres
- Message affiché quand une réservation est attribuée
- Système de file d'attente FIFO (Premier arrivé, premier servi)

## 🔧 Structure de la Table Reservation

| Colonne | Type | Description |
|---------|------|-------------|
| id | INT | Identifiant unique (auto-incrémenté) |
| isbnLivre | VARCHAR(50) | ISBN du livre réservé |
| numeroAdherent | VARCHAR(50) | Numéro du membre |
| dateReservation | DATE | Date de création de la réservation |
| statut | ENUM | en_attente, attribuee, annulee, expiree |
| dateAttribution | DATE | Date d'attribution au membre |
| dateExpiration | DATE | Date limite pour emprunter (3 jours après attribution) |

## 📝 Notes Importantes

1. **File d'attente FIFO**: Les réservations sont traitées dans l'ordre chronologique
2. **Limite d'emprunt**: Respecte toujours la limite (3 pour standard, 5 pour premium)
3. **Pas de doublon**: Un membre ne peut pas réserver deux fois le même livre
4. **Expiration**: 3 jours pour emprunter après attribution
5. **Nettoyage**: Les réservations expirées sont marquées automatiquement

## 🐛 Dépannage

### La table existe déjà?
Pas de problème ! Le script utilise `CREATE TABLE IF NOT EXISTS`, il ne fera rien si la table existe.

### Erreur de clé étrangère?
Vérifiez que :
- La table `livres` a une colonne `ISBN`
- La table `adherent` a une colonne `numero`

### Les réservations ne s'affichent pas?
1. Vérifiez que la table est créée : `SHOW TABLES;`
2. Vérifiez les données : `SELECT * FROM reservation;`
3. Recompilez l'application
