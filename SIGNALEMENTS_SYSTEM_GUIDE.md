# SYSTÈME DE SIGNALEMENTS POUR RETARDS

## Vue d'ensemble
Le système gère automatiquement les retards de retour de livres et applique des sanctions progressives aux adhérents.

## Fonctionnement

### 1. **Ajout du champ signalements**
- Chaque adhérent possède maintenant un compteur de signalements (warnings)
- Valeur initiale : 0
- Maximum avant suppression : 3

### 2. **Détection des retards**
Lorsqu'un livre est retourné :
- Le système compare la date de retour réelle avec la date prévue
- Si retard détecté → signalement automatique

### 3. **Sanctions progressives**

| Signalements | Action |
|--------------|--------|
| 1 | ⚠️ Premier avertissement |
| 2 | ⚠️ Deuxième avertissement |
| 3 | 🚫 **Suppression automatique de l'adhérent** |

## Installation

### Étape 1 : Mettre à jour la base de données
Exécutez le script SQL suivant dans MySQL Workbench ou en ligne de commande :

```sql
-- Dans MySQL
mysql -u root -p bibliothe < ADD_SIGNALEMENTS_COLUMN.sql
```

Ou manuellement :
```sql
USE bibliothe;
ALTER TABLE adherent ADD COLUMN signalements INT NOT NULL DEFAULT 0;
```

### Étape 2 : Recompiler l'application
```batch
cd c:\Users\wassi\OneDrive\Bureau\biblio
.\run.bat
```

## Utilisation

### Dans l'interface "Loan Management" :

1. **Enregistrer un emprunt** :
   - Saisir ISBN du livre
   - Numéro de l'adhérent
   - Date d'emprunt (aujourd'hui par défaut)
   - Date de retour prévue
   - Cliquer sur "Register Loan"

2. **Retourner un livre** :
   - Sélectionner l'emprunt dans le tableau
   - Cliquer sur "Return Book"
   - Le système vérifie automatiquement si le retour est en retard

### Messages affichés :

✅ **Retour à temps** :
```
✓ Book returned successfully.
```

⚠️ **Premier/Deuxième retard** :
```
⚠️ WARNING: Late return detected! Member now has X warning(s). 
After 3 warnings, the member will be automatically removed.
```

🚫 **Troisième retard (suppression)** :
```
⚠️ DELETED: Member [Nom Prénom] has been automatically removed 
after 3 warnings for late returns.
```

## Vérification des signalements

### Via la base de données :
```sql
-- Voir tous les adhérents avec leurs signalements
SELECT numero, nom, prenom, signalements 
FROM adherent 
ORDER BY signalements DESC;

-- Voir uniquement les adhérents avec des signalements
SELECT numero, nom, prenom, signalements 
FROM adherent 
WHERE signalements > 0;
```

### Dans l'application :
Les signalements peuvent être consultés dans la gestion des adhérents (future amélioration : afficher le nombre dans l'interface).

## Règles de gestion

1. **Un retard = un signalement** : Chaque retour tardif incrémente le compteur
2. **Suppression automatique** : À 3 signalements, l'adhérent est immédiatement supprimé
3. **Irréversible** : Une fois supprimé, l'adhérent doit être recréé manuellement
4. **Pas de réinitialisation** : Les signalements ne sont jamais effacés automatiquement

## Améliorations futures possibles

- [ ] Afficher le nombre de signalements dans la liste des adhérents
- [ ] Permettre à l'admin de réinitialiser les signalements manuellement
- [ ] Envoyer une notification avant suppression
- [ ] Ajouter un statut "suspendu" avant la suppression définitive
- [ ] Historique des signalements avec raisons détaillées

## Support technique

Si un adhérent est supprimé par erreur :
1. Le recréer manuellement via "Add Member"
2. Utiliser le même numéro de téléphone (numéro)
3. Les anciens emprunts resteront dans l'historique

Pour réinitialiser tous les signalements :
```sql
UPDATE adherent SET signalements = 0;
```
