
# ProjetWS - Gestion des Étudiants

Application Android pour gérer une liste d'étudiants avec Web Service PHP/MySQL.

## Technologies
- **Android** : Java + Volley + Gson
- **Backend** : PHP 8 + MySQL
- **Serveur** : XAMPP/WAMP

## Installation

### Serveur PHP
Copier le dossier `ws` dans : `C:\xampp\htdocs\projet\ws\`

### Application Android
- Ouvrir le projet dans Android Studio
- Modifier les URLs si nécessaire :
  - `http://10.0.2.2/projet/ws/createEtudiant.php`
  - `http://10.0.2.2/projet/ws/loadEtudiant.php`
- Synchroniser et exécuter

## Structure
```
projetws/
├── app/src/main/java/.../
│   ├── MainActivity.java      # Accueil
│   ├── AddEtudiant.java       # Ajout
│   ├── ListEtudiant.java      # Liste
│   └── beans/Etudiant.java    # Modèle
└── server/ws/
    ├── createEtudiant.php
    ├── loadEtudiant.php
    └── service/EtudiantService.php
```

## Utilisation
1. Démarrer XAMPP (Apache + MySQL)
2. Lancer l'application
3. Ajouter des étudiants
4. Voir la liste

## URLs importantes
- Émulateur : `http://10.0.2.2/projet/ws/`
- Navigateur PC : `http://localhost/projet/ws/`

## Demo

https://github.com/user-attachments/assets/3d874a18-6e83-499e-9585-c2452263f9d3

