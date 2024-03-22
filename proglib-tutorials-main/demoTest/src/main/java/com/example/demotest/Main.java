package com.example.demotest;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Main extends Application {

    private final Map<String, String> languageCommands = new HashMap<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        languageCommands.put("Python", "C:\\Users\\pai2\\AppData\\Local\\Programs\\Python\\Python312\\python");
        languageCommands.put("JavaScript", "node");

        ComboBox<String> languageComboBox = new ComboBox<>();
        languageComboBox.getItems().addAll("Python", "JavaScript");
        languageComboBox.setValue("Python");

        TextArea codeArea = new TextArea();
        codeArea.setPromptText("Enter your code here");

        TextArea outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setWrapText(true);

        Button executeButton = new Button("Execute");
        executeButton.setOnAction(e -> {
            String selectedLanguage = languageComboBox.getValue();
            String code = codeArea.getText();
            String result = executeCode(selectedLanguage, code);
            outputArea.setText(result);
        });

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        root.getChildren().addAll(new Label("Select Language:"), languageComboBox, codeArea, executeButton, new Label("Output:"), outputArea);

        Scene scene = new Scene(root, 600, 400);

        primaryStage.setTitle("Code Executor");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private String executeCode(String language, String code) {
        String command = languageCommands.get(language);
        if (command == null) {
            return "Unsupported language: " + language;
        }

        try {
            ProcessBuilder pb = new ProcessBuilder(command, "-c", code);
            pb.redirectErrorStream(true); // Redirect stderr to stdout
            Process process = pb.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            while ((line = errorReader.readLine()) != null) {
                output.append(line).append("\n");
            }

            int exitCode = process.waitFor();
            output.append("Code executed with exit code: ").append(exitCode).append("\n");
            return output.toString();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "Error executing code: " + e.getMessage();
        }
    }
}


