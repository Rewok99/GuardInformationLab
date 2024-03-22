import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PythonCodeExecutorGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Python Code Executor");

        // Создание элементов GUI
        Label codeLabel = new Label("Введите код на Python:");
        TextArea codeTextArea = new TextArea();
        Button executeButton = new Button("Выполнить");
        TextArea resultTextArea = new TextArea();

        // Настройка обработчика нажатия кнопки "Выполнить"
        executeButton.setOnAction(e -> {
            String pythonCode = codeTextArea.getText();
            String result = executePythonCode(pythonCode);
            resultTextArea.setText(result);
        });

        // Настройка компоновки элементов в интерфейсе
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.getChildren().addAll(codeLabel, codeTextArea, executeButton, resultTextArea);

        // Создание сцены и установка на основной Stage
        Scene scene = new Scene(vbox, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Метод для выполнения кода на Python
    private String executePythonCode(String pythonCode) {
        try {
            ProcessBuilder pb = new ProcessBuilder("python", "-c", pythonCode);
            Process process = pb.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }

            int exitCode = process.waitFor();
            result.append("Python код выполнен. Код завершился с кодом: ").append(exitCode);

            return result.toString();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "Ошибка выполнения Python кода: " + e.getMessage();
        }
    }
}
