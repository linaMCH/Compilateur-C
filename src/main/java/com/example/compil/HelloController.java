package com.example.compil;

import com.example.compil.ast.StatementNode;
import com.example.compil.parser.Parser;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.geometry.Side;
import org.fxmisc.richtext.CodeArea;
import com.example.compil.execution.Interpreter;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HelloController {

    @FXML private CodeArea codeArea;
    @FXML private TextArea lexArea;
    @FXML private TextArea exeArea;
    @FXML private TextArea syntaxArea;
    @FXML private Label infoLabel;
    @FXML private Label statusLabel;
    @FXML private Button fileButton;
    @FXML private Button helpButton;

    // ===== MOTIFS POUR COLORATION =====
    private static final String KEYWORDS = "\\b(int|float|double|char|if|else|while|for|do|switch|case|return|void|const)\\b";
    private static final String NUMBERS  = "\\b\\d+\\b";
    private static final String STRINGS  = "\"([^\"\\\\]|\\\\.)*\"";
    private static final String COMMENTS = "//[^\n]*|/\\*(.|\\R)*?\\*/";
    private static final Pattern PATTERN = Pattern.compile(
            "(?<KEYWORD>" + KEYWORDS + ")"
                    + "|(?<NUMBER>" + NUMBERS + ")"
                    + "|(?<STRING>" + STRINGS + ")"
                    + "|(?<COMMENT>" + COMMENTS + ")"
    );

    // ===== MENU STYLE =====
    private void resetStyle() {
        String normal = "-fx-background-color:transparent; -fx-font-size:14;";
        fileButton.setStyle(normal);
        helpButton.setStyle(normal);
    }

    private void setActive(Button btn) {
        resetStyle();
        btn.setStyle("-fx-background-color:#e5e5e5; -fx-font-size:14;");
    }

    // ===== FILE MENU =====
    @FXML
    void showFileMenu() {
        setActive(fileButton);
        ContextMenu menu = new ContextMenu();

        MenuItem open = new MenuItem("📂 Ouvrir...");
        open.setOnAction(e -> openFile());

        MenuItem save = new MenuItem("💾 Enregistrer");
        save.setOnAction(e -> saveFile());

        MenuItem newFile = new MenuItem("📄 Nouveau fichier");
        newFile.setOnAction(e -> newFile());

        menu.getItems().addAll(open, save, new SeparatorMenuItem(), newFile);
        menu.setOnHidden(e -> resetStyle());
        menu.show(fileButton, Side.BOTTOM, 0, 0);
    }

    @FXML
    void showHelpMenu() {
        setActive(helpButton);
        ContextMenu menu = new ContextMenu();

        MenuItem about = new MenuItem("ℹ À propos");
        about.setOnAction(e -> showAbout());

        MenuItem doc = new MenuItem("Documentation");
        doc.setOnAction(e -> showDoc());

        menu.getItems().addAll(about, doc);
        menu.setOnHidden(e -> resetStyle());
        menu.show(helpButton, Side.BOTTOM, 0, 0);
    }

    // ===== FILE ACTIONS =====
    void newFile() {
        codeArea.clear();
        lexArea.clear();
        syntaxArea.clear();
        exeArea.clear();
        statusLabel.setText("Nouveau fichier");
    }

    void openFile() {
        FileChooser chooser = new FileChooser();
        File file = chooser.showOpenDialog(new Stage());
        try {
            if (file != null) {
                codeArea.replaceText(Files.readString(file.toPath()));
                statusLabel.setText("Fichier ouvert");
            }
        } catch (Exception ignored) {}
    }

    void saveFile() {
        FileChooser chooser = new FileChooser();
        File file = chooser.showSaveDialog(new Stage());
        try {
            if (file != null) {
                Files.writeString(Path.of(file.getAbsolutePath()), codeArea.getText());
                statusLabel.setText("Fichier enregistré");
            }
        } catch (Exception ignored) {}
    }

    // ===== OTHER ACTIONS =====
    void showAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("À propos");
        alert.setHeaderText("Compilateur C - Version 1.0");
        alert.setContentText(
                "Un environnement de développement intégré pour le langage C.\n\n" +
                        "Inclut l'analyse lexicale, syntaxique, exécution simulée et coloration syntaxique.");
        alert.showAndWait();
    }

    void showDoc() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Documentation");
        alert.setHeaderText("Documentation");
        alert.setContentText(
                "1. Écrire votre programme C.\n" +
                        "2. Cliquer sur Compiler.\n" +
                        "3. Cliquer sur Exécuter.\n" +
                        "4. Profiter de la coloration syntaxique.");
        alert.showAndWait();
    }

    @FXML
    void handleCompile() {
        String code = codeArea.getText();

        List<Token> tokens = LexerRunner.tokenize(code);

        // Affichage lexicale
        StringBuilder lexResult = new StringBuilder();
        for(Token t : tokens){
            lexResult.append(t.toString()).append("\n");
        }
        lexArea.setText(lexResult.toString());

        // Analyse syntaxique
        Parser parser = new Parser(tokens);
        List<StatementNode> statements = parser.parse();

        // Affichage syntaxique
        StringBuilder syntaxResult = new StringBuilder();
        for (StatementNode stmt : statements) {
            syntaxResult.append(stmt.prettyPrint(0)).append("\n");
        }

        // Affichage des erreurs
        List<String> errors = parser.getErrors();
        if (!errors.isEmpty()) {
            syntaxResult.append("\n--- ERREURS ---\n");
            for (String err : errors) {
                syntaxResult.append(err).append("\n");
            }
        }

        syntaxArea.setText(syntaxResult.toString());
    }

    @FXML
    void handleExecute() {
        try {

            String code = codeArea.getText();


            List<Token> tokens = LexerRunner.tokenize(code);
            Parser parser = new Parser(tokens);
            List<StatementNode> statements = parser.parse();

            if (!parser.getErrors().isEmpty()) {
                exeArea.setText(" Erreur de syntaxe. Corrigez le code avant d'exécuter.");
                return;
            }

            // 3. Initialisation de l'interpréteur
            com.example.compil.execution.Interpreter interpreter = new com.example.compil.execution.Interpreter();

            // 4. Exécution de chaque instruction
            StringBuilder output = new StringBuilder("--- Début de l'exécution ---\n\n");
            for (StatementNode stmt : statements) {
                stmt.accept(interpreter);
            }

            // 5. Affichage de l'état final de la mémoire (les variables)
            output.append("Exécution terminée avec succès.\n\n");
            output.append("État final des variables :\n");
            interpreter.getMemory().forEach((var, val) -> {
                output.append(" • ").append(var).append(" = ").append(val).append("\n");
            });

            exeArea.setText(output.toString());
            statusLabel.setText("Exécution terminée");

        } catch (Exception e) {
            // Capture des erreurs d'exécution (ex: division par zéro, variable non définie)
            exeArea.setText("⚠ ERREUR D'EXÉCUTION :\n" + e.getMessage());
            statusLabel.setText("Échec de l'exécution");
        }
    }

    @FXML
    void handleClear() {
        codeArea.clear();
        lexArea.clear();
        syntaxArea.clear();
        exeArea.clear();
        statusLabel.setText("Prêt");
        infoLabel.setText("");
    }

    @FXML
    public void initialize() {
        // Coloration syntaxique à chaque modification
        codeArea.textProperty().addListener((obs, oldText, newText) -> {
            applyHighlighting(codeArea);
            infoLabel.setText("Lignes: " + newText.split("\n").length + "   Caractères: " + newText.length());
        });
    }

    // ===== COLORATION SYNTAXIQUE =====
    private void applyHighlighting(CodeArea codeArea) {
        String text = codeArea.getText();
        codeArea.clearStyle(0, text.length());

        Matcher matcher = PATTERN.matcher(text);
        while (matcher.find()) {
            if (matcher.group("KEYWORD") != null) {
                codeArea.setStyleClass(matcher.start(), matcher.end(), "keyword");
            } else if (matcher.group("NUMBER") != null) {
                codeArea.setStyleClass(matcher.start(), matcher.end(), "number");
            } else if (matcher.group("STRING") != null) {
                codeArea.setStyleClass(matcher.start(), matcher.end(), "string");
            } else if (matcher.group("COMMENT") != null) {
                codeArea.setStyleClass(matcher.start(), matcher.end(), "comment");
            }
        }
    }
}