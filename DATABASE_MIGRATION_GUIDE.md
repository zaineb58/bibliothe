# How to Execute Database Migration

## Option 1: MySQL Command Line

1. Open Command Prompt or PowerShell
2. Navigate to the project directory:
   ```powershell
   cd "C:\Users\wassi\OneDrive\Bureau\biblio"
   ```

3. Connect to MySQL:
   ```powershell
   mysql -u root -p
   ```

4. Enter your MySQL password when prompted

5. Select your database:
   ```sql
   USE your_database_name;
   ```

6. Execute the migration:
   ```sql
   source UPDATE_DATABASE.sql;
   ```
   OR copy-paste the commands:
   ```sql
   ALTER TABLE livres ADD COLUMN nombreCopies INT DEFAULT 1;
   ALTER TABLE livres ADD COLUMN copiesDisponibles INT DEFAULT 1;
   UPDATE livres SET nombreCopies = 1, copiesDisponibles = 1 WHERE nombreCopies IS NULL;
   ```

7. Verify the migration:
   ```sql
   DESCRIBE livres;
   SELECT * FROM livres LIMIT 5;
   ```

8. Exit MySQL:
   ```sql
   EXIT;
   ```

## Option 2: MySQL Workbench

1. Open MySQL Workbench
2. Connect to your database server
3. Select your database from the left panel
4. Open the `UPDATE_DATABASE.sql` file in Workbench:
   - File → Open SQL Script
   - Navigate to: `C:\Users\wassi\OneDrive\Bureau\biblio\UPDATE_DATABASE.sql`
5. Click the lightning bolt icon (Execute) to run the script
6. Check the output panel for success messages
7. Verify by running:
   ```sql
   SELECT * FROM livres LIMIT 5;
   ```

## Option 3: PHPMyAdmin (if using XAMPP/WAMP)

1. Open your browser and go to `http://localhost/phpmyadmin`
2. Select your database from the left sidebar
3. Click the "SQL" tab at the top
4. Copy and paste the contents of `UPDATE_DATABASE.sql`:
   ```sql
   ALTER TABLE livres ADD COLUMN nombreCopies INT DEFAULT 1;
   ALTER TABLE livres ADD COLUMN copiesDisponibles INT DEFAULT 1;
   UPDATE livres SET nombreCopies = 1, copiesDisponibles = 1 WHERE nombreCopies IS NULL;
   ```
5. Click "Go" to execute
6. Verify by clicking "Browse" tab in the livres table

## Verification Steps

After executing the migration, verify with these queries:

```sql
-- Check table structure
DESCRIBE livres;

-- Should show:
-- | Field              | Type         | Null | Key | Default | Extra |
-- | nombreCopies       | int          | YES  |     | 1       |       |
-- | copiesDisponibles  | int          | YES  |     | 1       |       |

-- Check data
SELECT ISBN, titre, nombreCopies, copiesDisponibles, disponibilite 
FROM livres 
LIMIT 10;

-- All existing books should have:
-- nombreCopies = 1
-- copiesDisponibles = 1
```

## Troubleshooting

### Error: "Duplicate column name 'nombreCopies'"
**Cause**: Column already exists
**Solution**: Skip the ALTER TABLE statements, only run UPDATE if needed

### Error: "Access denied"
**Cause**: Insufficient privileges
**Solution**: Connect as root or user with ALTER TABLE privileges

### Error: "Unknown database"
**Cause**: Database name incorrect
**Solution**: 
```sql
SHOW DATABASES;  -- Find correct name
USE correct_database_name;
```

## Rollback (if needed)

If you need to undo the migration:

```sql
ALTER TABLE livres DROP COLUMN nombreCopies;
ALTER TABLE livres DROP COLUMN copiesDisponibles;
```

**Warning**: This will permanently delete the copy data!

## Next Steps After Migration

1. ✅ Compile the Java project
2. ✅ Run the application
3. ✅ Test adding a book with multiple copies
4. ✅ Test borrowing multiple copies by different adherents
5. ✅ Test returning copies individually
6. ✅ Verify copy counts update correctly in all views

---
**Status**: Ready to execute
**Estimated Time**: < 1 minute
**Risk Level**: Low (backward compatible)
