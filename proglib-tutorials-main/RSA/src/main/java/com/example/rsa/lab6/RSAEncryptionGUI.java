package com.example.rsa.lab6;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class RSAEncryptionGUI extends Application {

    private TextField pField;
    private TextField qField;
    private TextField eField;
    private TextArea inputArea;
    private TextArea outputArea;
    private Label dLabel;
    private TextField dField;
    private boolean isRussian = false; // флаг выбранного языка

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("RSA Encryption");

        // Создаем элементы управления
        Label pLabel = new Label("P (простое число):");
        pField = new TextField();
        Label qLabel = new Label("Q (простое число):");
        qField = new TextField();
        Label eLabel = new Label("Открытый ключ e:");
        eField = new TextField();
        Label inputLabel = new Label("Открытый текст:");
        inputArea = new TextArea();
        Label outputLabel = new Label("Зашифрованный:");
        outputArea = new TextArea();
        outputArea.setEditable(true);
        Label dTextLabel = new Label("Закрытый ключ d:");
        dField = new TextField();
        dField.setEditable(false);

        Button encryptButton = new Button("Зашифровать");
        encryptButton.setOnAction(e -> encrypt());

        Button decryptButton = new Button("Дешифровать");
        decryptButton.setOnAction(e -> decrypt());

        // Выбор языка
        Label languageLabel = new Label("Выберите язык:");
        Button russianButton = new Button("Русский");
        russianButton.setOnAction(e -> setLanguage("russian"));
        Button englishButton = new Button("Английский");
        englishButton.setOnAction(e -> setLanguage("english"));

        // Создаем сетку для размещения элементов управления
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(10);
        grid.setHgap(10);

        grid.add(pLabel, 0, 0);
        grid.add(pField, 1, 0);
        grid.add(qLabel, 0, 1);
        grid.add(qField, 1, 1);
        grid.add(eLabel, 0, 2);
        grid.add(eField, 1, 2);
        grid.add(inputLabel, 0, 3, 2, 1);
        grid.add(inputArea, 0, 4, 2, 1);
        grid.add(outputLabel, 0, 5, 2, 1);
        grid.add(outputArea, 0, 6, 2, 1);
        grid.add(dTextLabel, 0, 7);
        grid.add(dField, 1, 7);
        grid.add(encryptButton, 0, 8);
        grid.add(decryptButton, 1, 8);
        grid.add(languageLabel, 0, 9);
        grid.add(new HBox(russianButton, englishButton), 1, 9);

        // Создаем главный контейнер
        VBox root = new VBox();
        root.getChildren().addAll(grid);
        root.setAlignment(Pos.CENTER);

        // Отображаем сцену
        Scene scene = new Scene(root, 400, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void encrypt() {
        try {
            // Получаем значения из полей ввода
            int p, q, e;
            try {
                p = Integer.parseInt(pField.getText());
                q = Integer.parseInt(qField.getText());
                e = Integer.parseInt(eField.getText());
            } catch (NumberFormatException ex) {
                outputArea.setText("Введите целые числа в поля P, Q и открытый ключ E");
                return;
            }

            // Проверяем, являются ли p и q простыми числами
            if (!isPrime(p) || !isPrime(q)) {
                outputArea.setText("P и Q должны быть простыми числами");
                return;
            }
            String inputText = inputArea.getText();


            int n = p * q;
            if (n < RSAEncryption.dict1.size()) {
                outputArea.setText("Введены маленькие значения p и q, возможны ошибки");
                return;
            }

            int f = (p - 1) * (q - 1);
            if (RSAEncryption.gcd(f, e) != 1) {
                outputArea.setText("Введенный открытый ключ не подходит");
                return;
            }

            List<Character> selectedDict;
            if (isRussian) {
                selectedDict = RSAEncryption.dict2;
            } else {
                selectedDict = RSAEncryption.dict1;
            }

            // Генерируем закрытый ключ d
            int d = RSAEncryption.generatePrivateKey(e, f);

            // Выводим значение d
            dField.setText(String.valueOf(d));

            // Шифруем введенный текст
            List<Integer> encrypted = new ArrayList<>();
            for (char ch : inputText.toCharArray()) {
                int index = selectedDict.indexOf(ch);
                if (index != -1) {
                    encrypted.add(RSAEncryption.modPow(index, e, n));
                }
            }

            // Выводим зашифрованный текст
            StringBuilder result = new StringBuilder();
            for (int num : encrypted) {
                result.append(num).append(" ");
            }
            outputArea.setText(result.toString());
        }catch (Exception e){
            outputArea.setText("Ошибка");
        }
    }

    private void decrypt() {
        try {


        // Получаем значения из полей ввода
        int p = Integer.parseInt(pField.getText());
        int q = Integer.parseInt(qField.getText());
        int d = Integer.parseInt(dField.getText());
        String inputText = outputArea.getText();

        int n = p * q;
        if (n < RSAEncryption.dict1.size()) {
            outputArea.setText("Введены маленькие значения p и q, возможны ошибки");
            return;
        }

        List<Character> selectedDict;
        if (isRussian) {
            selectedDict = RSAEncryption.dict2;
        } else {
            selectedDict = RSAEncryption.dict1;
        }

        // Дешифруем текст
        List<Integer> encryptedList = parseInputText(inputText);
        StringBuilder decrypted = new StringBuilder();
        for (int num : encryptedList) {
            char ch = selectedDict.get(RSAEncryption.modPow(num, d, n));
            decrypted.append(ch);
        }

        // Выводим дешифрованный текст
        inputArea.setText(decrypted.toString());
        }catch (Exception e){
            outputArea.setText("Ошибка");
        }
    }

    private List<Integer> parseInputText(String inputText) {

        List<Integer> encryptedList = new ArrayList<>();
        StringBuilder numStrBuilder = new StringBuilder();
        for (char ch : inputText.toCharArray()) {
            if (ch == ' ') {
                if (numStrBuilder.length() > 0) {
                    encryptedList.add(Integer.parseInt(numStrBuilder.toString()));
                    numStrBuilder = new StringBuilder();
                }
            } else {
                numStrBuilder.append(ch);
            }
        }
        if (numStrBuilder.length() > 0) {
            encryptedList.add(Integer.parseInt(numStrBuilder.toString()));
        }
        return encryptedList;

    }

    private void setLanguage(String language) {
        if (language.equals("russian")) {
            isRussian = true;
            RSAEncryption.dict1.clear();
            RSAEncryption.dict2.clear();
            for (char ch = 'а'; ch <= 'я'; ch++) {
                RSAEncryption.dict2.add(ch);
            }
            RSAEncryption.dict2.add('ё');

        } else if (language.equals("english")) {
            isRussian = false;
            RSAEncryption.dict1.clear();
            RSAEncryption.dict2.clear();
            for (char ch = 'a'; ch <= 'z'; ch++) {
                RSAEncryption.dict1.add(ch);
            }
            RSAEncryption.dict1.add(' ');

        }
    }

    private boolean isPrime(int number) {
        if (number <= 1) {
            return false;
        }
        if (number == 2) {
            return true;
        }
        if (number % 2 == 0) {
            return false;
        }
        for (int i = 3; i * i <= number; i += 2) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }

}
