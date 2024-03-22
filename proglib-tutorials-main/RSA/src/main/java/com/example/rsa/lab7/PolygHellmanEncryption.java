package com.example.rsa.lab7;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class PolygHellmanEncryption {

    public static List<Character> dict1 = new ArrayList<>();
    public static List<Character> dict2 = new ArrayList<>();

    public static void main(String[] args) {


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
            for (char c = 'а'; c <= 'я'; c++) {
                dict2.add(c);
            }
            for (char c = 'А'; c <= 'Я'; c++) {
                dict2.add(c);
            }
            dict2.add('ё');
            dict2.add('Ё');
            dict2.add(' '); // Добавляем пробел


            Scanner scanner = new Scanner(System.in);

            System.out.println("Выберите язык (1 - Русский, 2 - Английский):");
            int languageChoice = scanner.nextInt();
            List<Character> selectedDict = (languageChoice == 1) ? dict2 : dict1;

            System.out.println("Введите значение e:");
            int e = scanner.nextInt();


            System.out.println("Введите значение n:");
            int n = scanner.nextInt();

            int d = generatePrivateKey(e, n);
            System.out.println("Значение d:" + d);

            scanner.nextLine(); // Чтение перевода строки после числа

            System.out.println("Выберите действие (1 - Зашифровать, 2 - Расшифровать):");
            int action = scanner.nextInt();
            scanner.nextLine(); // Чтение перевода строки после числа

            if (action == 1) {
                System.out.println("Введите текст для шифрования:");
                String plaintext = scanner.nextLine();

                List<Integer> encrypted = encrypt(plaintext, e, n, selectedDict);
                System.out.println("Зашифрованный текст:");
                for (int num : encrypted) {
                    System.out.print(num + " ");
                }
            } else if (action == 2) {
                System.out.println("Введите числа для расшифровки (разделяйте пробелом):");
                String cipherText = scanner.nextLine();

                String decrypted = decrypt(cipherText, d, n, selectedDict);
                System.out.println("Расшифрованный текст:");
                System.out.println(decrypted);
            } else {
                System.out.println("Неверный выбор действия.");
            }

            scanner.close();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
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

    // Генерация закрытого ключа d
    public static int generatePrivateKey(int e, int n) {
        try {
            int d;
            Random rand = new Random();
            d = rand.nextInt(1000);
            while ((e * d) % (n - 1) != 1) {
                d++;
            }
            if (!areCoPrime(d, n)) {
                return d;
            }
            return d;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }


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
}





