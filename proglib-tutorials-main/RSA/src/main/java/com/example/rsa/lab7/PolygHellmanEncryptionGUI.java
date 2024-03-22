package com.example.rsa.lab7;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

import static com.example.rsa.lab7.PolygHellmanEncryption.generatePrivateKey;

public class PolygHellmanEncryptionGUI extends Application {


    int dValue = 0;
    public static List<Character> dict1 = new ArrayList<>();
    public static List<Character> dict2 = new ArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {




            // Заполнение словарей для английского языка
            for (char c = 'a'; c <= 'z'; c++) {
                dict1.add(c);
            }
            for (char c = 'A'; c <= 'Z'; c++) {
                dict1.add(c);
            }
            dict1.add(' '); // Добавляем пробел

            // Заполнение словарей для русского языка

            for (char c = 'а'; c <= 'е'; c++) {
                dict2.add(c);
            }
            dict2.add('ё');
            for (char c = 'ж'; c <= 'я'; c++) {
                dict2.add(c);
            }

            for (char c = 'А'; c <= 'Е'; c++) {
                dict2.add(c);
            }
            dict2.add('Ё');
            for (char c = 'Ж'; c <= 'Я'; c++) {
                dict2.add(c);
            }
            dict2.add(' '); // Добавляем пробел

            primaryStage.setTitle("Polyg Hellman Encryption");

            VBox vbox = new VBox();
            vbox.setPadding(new Insets(10));
            vbox.setSpacing(8);
            vbox.setBackground(new Background(new BackgroundFill(Color.LIGHTYELLOW, null, null)));

            ComboBox<String> languageComboBox = new ComboBox<>();
            languageComboBox.getItems().addAll("Русский", "Английский");
            languageComboBox.setValue("Русский");






            TextField eField = new TextField();
            eField.setPromptText("Введите значение e");

            TextField nField = new TextField();
            nField.setPromptText("Введите значение n");

            TextField dField = new TextField();
            dField.setPromptText("Введите начение d");

            TextArea inputText = new TextArea();
            inputText.setPromptText("Введите текст");

            ComboBox<String> actionComboBox = new ComboBox<>();
            actionComboBox.getItems().addAll("Зашифровать", "Расшифровать");
            actionComboBox.setValue("Зашифровать");

            Button executeButton = new Button("Выполнить");
            TextArea resultArea = new TextArea();
            resultArea.setEditable(false);

            executeButton.setOnAction(e -> {
                int languageChoice = languageComboBox.getSelectionModel().getSelectedIndex() + 1;
                List<Character> selectedDict = (languageChoice == 1) ? dict2 : dict1;


                int eValue,nValue;

                eValue = Integer.parseInt(eField.getText());
                nValue = Integer.parseInt(nField.getText());
                dValue = Integer.parseInt(dField.getText());

                // Проверяем, являются ли p и q простыми числами
                if (!isPrime(nValue)) {
                    resultArea.setText("n должно быть простым числом");
                    return;
                }

                if (!areCoPrime(eValue, dValue)) {
                    resultArea.setText("e и d должны быть взыимно простые");
                    return;
                }
                if (!areCoPrime(eValue, nValue-1)) {
                    resultArea.setText("e и n-1 должны быть взыимно простые");
                    return;
                }

                if (!isCoprime(dValue, eValue, nValue)) {
                    resultArea.setText("d не удовлетворяет условию D * E (mod (N - 1)) == 1");
                    return;
                }




                String input = inputText.getText();
                int action = actionComboBox.getSelectionModel().getSelectedIndex() + 1;

                /**
                if (action != 2){
                        dValue = generatePrivateKey(eValue, nValue);
                       dField.setText(String.valueOf(dValue));
                      dValue = Integer.parseInt(dField.getText());}
                else {
                    dValue = Integer.parseInt(dField.getText());
                }
                 **/




                if (action == 1) {
                    List<Integer> encrypted = encrypt(input, eValue, nValue, selectedDict);
                    StringBuilder result = new StringBuilder();
                    for (int num : encrypted) {
                        result.append(num).append(" ");
                    }
                    resultArea.setText("Зашифрованный текст:\n" + result.toString());
                } else if (action == 2) {
                    String decrypted = decrypt(input, dValue, nValue, selectedDict);
                    resultArea.setText("Расшифрованный текст:\n" + decrypted);
                } else {
                    resultArea.setText("Неверный выбор действия.");
                }
            });

            vbox.getChildren().addAll(
                    new Label("Выберите язык:"),
                    languageComboBox,
                    new Label("Введите значение e:"),
                    eField,
                    new Label("Введите значение d:"),
                    dField,
                    new Label("Введите значение n:"),
                    nField,
                    new Label("Введите текст:"),
                    inputText,
                    new Label("Выберите действие:"),
                    actionComboBox,
                    executeButton,
                    resultArea
            );


            Scene scene = new Scene(vbox, 400, 600);

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    public static List<Integer> encrypt(String plaintext, int e, int n, List<Character> dict) {
        try {
            List<Integer> encrypted = new ArrayList<>();
            for (char c : plaintext.toCharArray()) {
                if (dict.contains(c)) {
                    int charIndex = dict.indexOf(c);
                    int encryptedChar = modPow(charIndex, e, n);
                    encrypted.add(encryptedChar);
                }
            }
            return encrypted;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String decrypt(String cipherText, int d, int n, List<Character> dict) {
        try {
            StringBuilder decrypted = new StringBuilder();
            String[] cipherParts = cipherText.split(" ");
            for (String part : cipherParts) {
                int encryptedChar = Integer.parseInt(part);
                char decryptedChar = dict.get(modPow(encryptedChar, d, n));
                decrypted.append(decryptedChar);
            }
            return decrypted.toString();
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

    private static int modPow(int base, int exponent, int modulus) {
        try {
            if (modulus == 1) return 0;
            int result = 1;
            base = base % modulus;
            while (exponent > 0) {
                if (exponent % 2 == 1) {
                    result = (result * base) % modulus;
                }
                exponent = exponent >> 1;
                base = (base * base) % modulus;
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
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

    public static boolean areCoPrime(int a, int b) {
        if (a == 0 || b == 0) {
            return false;
        }
        for (int i = 2; i <= Math.min(a, b); i++) {
            if (a % i == 0 && b % i == 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean isCoprime(int d, int e, int n) {
        if ((d  *  e) % (n - 1) != 1){
            return false;
        }
        return true;
    }



}
