package com.example.compil;

import com.example.compil.ast.StatementNode;
import com.example.compil.parser.Parser;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.geometry.Side;

import javax.swing.*;
import java.io.StringReader;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class HelloController {

    @FXML private TextArea codeArea;
    @FXML private TextArea lexArea;
    @FXML private TextArea exeArea;
    @FXML private Label infoLabel;
    @FXML private Label statusLabel;
    @FXML private Button fileButton;
    @FXML private Button helpButton;
    @FXML private TextArea syntaxArea;

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

        MenuItem doc = new MenuItem("Documentation");
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

        syntaxArea.setText(syntaxResult.toString()); // syntaxArea doit être un TextArea connecté via @FXML
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