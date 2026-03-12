package com.example.compil;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.geometry.Side;
import java.io.StringReader;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class HelloController {

    @FXML private TextArea codeArea;
    @FXML private TextArea lexArea;
    @FXML private TextArea exeArea;
    @FXML private Label infoLabel;
    @FXML private Label statusLabel;
    @FXML private Button fileButton;
    @FXML private Button helpButton;

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

        menu.getItems().addAll(open, save,
                new SeparatorMenuItem(), newFile);

        menu.setOnHidden(e -> resetStyle());

        menu.show(fileButton, Side.BOTTOM, 0, 0);
    }

    @FXML
    void showHelpMenu() {

        setActive(helpButton);

        ContextMenu menu = new ContextMenu();

        MenuItem about = new MenuItem("ℹ À propos");
        about.setOnAction(e -> showAbout());

        MenuItem doc = new MenuItem("📘 Documentation");
        doc.setOnAction(e -> showDoc());

        menu.getItems().addAll(about, doc);

        menu.setOnHidden(e -> resetStyle());

        menu.show(helpButton, Side.BOTTOM, 0, 0);
    }

    // ===== FILE ACTIONS =====

    void newFile() {
        codeArea.clear();
        statusLabel.setText("Nouveau fichier");
    }

    void openFile() {
        FileChooser chooser = new FileChooser();
        File file = chooser.showOpenDialog(new Stage());
        try {
            if (file != null) {
                codeArea.setText(Files.readString(file.toPath()));
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
                        "Inclut l'analyse lexicale, syntaxique et l'exécution simulée.");
        alert.showAndWait();
    }

    void showDoc() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Documentation");
        alert.setHeaderText("Documentation");
        alert.setContentText(
                "1. Écrire votre programme C.\n" +
                        "2. Cliquer sur Compiler.\n" +
                        "3. Cliquer sur Exécuter.");
        alert.showAndWait();
    }

    @FXML
    void handleCompile() {
        String source = codeArea.getText();
        if (source.isEmpty()) {
            lexArea.setText(" ");
            return;
        }

        StringReader reader = new StringReader(source);
        Analyseur_lex lexer = new Analyseur_lex(reader);

        StringBuilder output = new StringBuilder();
        output.append("--- RÉSULTAT DE L'ANALYSE LEXICALE ---\n\n");

        try {
            String tokenType;
            while ((tokenType = lexer.yylex()) != null) {
                String lexeme = lexer.yytext();
                output.append(String.format("[%s] \t %s\n", tokenType, lexeme));
            }
            lexArea.setText(output.toString());
            statusLabel.setText("Analyse lexicale terminée");

        } catch (Exception e) {
            lexArea.setText("Erreur durant l'analyse :\n" + e.getMessage());
            statusLabel.setText("Échec de l'analyse");
        }
    }

    @FXML
    void handleExecute() {
        exeArea.setText("Programme exécuté ✔");
        statusLabel.setText("Exécution terminée");
    }

    @FXML
    void handleClear() {
        codeArea.clear();
    }

    @FXML
    public void initialize() {
        codeArea.textProperty().addListener((obs, o, n) -> {
            infoLabel.setText("Lignes: " +
                    n.split("\n").length +
                    "   Caractères: " +
                    n.length());
        });
    }
}