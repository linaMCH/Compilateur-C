#  Mini-Compilateur - Sous-ensemble du langage C

![Java](https://img.shields.io/badge/Java-17%2B-orange?style=for-the-badge&logo=java)
![JavaFX](https://img.shields.io/badge/JavaFX-GUI-blue?style=for-the-badge)
![JFlex](https://img.shields.io/badge/Lexer-JFlex-green?style=for-the-badge)
![License](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)

##  Description

Ce projet est un **mini-compilateur** pédagogique capable d’analyser, de vérifier et d’interpréter un sous-ensemble du langage C. Développé dans un cadre académique, il couvre l'intégralité du pipeline de compilation, de l'analyse lexicale à l'exécution finale.

###  Fonctionnalités principales
- **Analyse Lexicale :** Générée avec **JFlex** pour une tokenisation précise.
- **Analyse Syntaxique :** Implémentation d'un **parseur récursif descendant**.
- **Arbre Syntaxique Abstrait (AST) :** Construction d'une représentation intermédiaire structurée.
- **Interpréteur :** Exécution séquentielle avec gestion d'une table des symboles.
- **Gestion des Erreurs :** Localisation précise (ligne/colonne) des erreurs de syntaxe.
- **Interface Graphique :** Éditeur interactif moderne développé en **JavaFX**.

---

##  Technologies utilisées

* **Langage :** Java 17
* **Générateur Lexical :** JFlex
* **Interface Utilisateur :** JavaFX (avec RichTextFX pour l'édition)
* **Gestionnaire de Projet :** Maven

---

##  Installation & Utilisation

### 1. Prérequis
- JDK 17 ou supérieur.
- Maven installé (ou utiliser le wrapper inclus).

### 2. Clonage et Compilation
```bash
# Cloner le dépôt
git clone [https://github.com/linaMCH/Compilateur-C](https://github.com/linaMCH/Compilateur-C)

# Accéder au dossier
cd MiniCompilateur

# Compiler avec Maven
mvn clean install
```

### 3. Exécution
```bash
mvn javafx:run
```

---

##  Aperçu du Langage Supporté

Le compilateur reconnaît une syntaxe proche du C standard, voici un code d'exemple reconnu par le mini compilateur :

```c
int x;
x = 5;

// Structures de contrôle
if (x < 10) {
    x++;
} else {
    x = 0;
}

// Boucles complexes
for (int i = 0; i < 5; i++) {
    x = x + i;
}

while (x > 0) { x--; }

// Choix multiple
switch (x) {
    case 1: x = 100;
    case 2: x = 200;
}
```

---

##  Organisation du Projet

```text
src/main/
├── java/
│   └── com/example/compil/
│       ├── lexer/       # Définition JFlex et Analyseur Lexical
│       ├── parser/      # Parseur et classes de l'AST
│       ├── interpreter/ # Logique d'exécution et Table des Symboles
│       └── gui/         # Contrôleurs et vues JavaFX
├── jflex/               # Fichier .flex source
└── resources/           # Fichiers FXML et CSS (thème dark/glassmorphism)
```

---

##  Tests & Validation

Le projet a été validé via une suite de tests rigoureux :
- [x] Déclarations simples et multiples.
- [x] Priorité des opérateurs arithmétiques (`*`, `/` avant `+`, `-`).
- [x] Évaluation d'expressions logiques complexes (`&&`, `||`, `!`).
- [x] Robustesse des boucles imbriquées.
- [x] **Gestion d'erreurs :** Détection de `;` manquants, parenthèses non fermées, etc.

---

##  Perspectives d'amélioration

- [ ] **Analyse Sémantique :** Vérification des types et détection de variables non déclarées.
- [ ] **Support des fonctions :** Définition de prototypes et appels avec paramètres.
- [ ] **Optimisation :** Coloration syntaxique en temps réel et export de l'AST en image.

---

##  Équipe de développement (ING3)

* **Nour El Imene Sahi**
* **Lina Maouche**
* **Mathya Mouhous**
* **Warda Meklat**

---
