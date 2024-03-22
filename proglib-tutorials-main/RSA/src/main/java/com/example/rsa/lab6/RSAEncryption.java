package com.example.rsa.lab6;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import static javafx.application.Application.launch;

public class RSAEncryption {

    // Создаем два списка символов для английского и русского алфавитов
    public static List<Character> dict1 = new ArrayList<>();
    public static List<Character> dict2 = new ArrayList<>();


    public static void main(String[] args) {
        launch(RSAEncryptionGUI.class);
        Scanner scanner = new Scanner(System.in);

        // Заполняем алфавиты
        for (char ch = 'a'; ch <= 'z'; ch++) {
            dict1.add(ch);
        }
        dict1.add(' '); // добавляем пробел в английский алфавит

        for (char ch = 'а'; ch <= 'я'; ch++) {
            dict2.add(ch);
        }
        dict2.add('ё'); // добавляем ё в русский алфавит

        // Пользователь выбирает язык для работы (1 - Русский, 2 - Английский)
        System.out.println("Выберите язык (1 - Русский, 2 - Английский):");
        int languageChoice = scanner.nextInt();
        List<Character> selectedDict = (languageChoice == 1) ? dict2 : dict1;

        // Пользователь вводит два простых числа p и q для генерации ключей
        System.out.println("Введите P (простое число):");
        int p = scanner.nextInt();
        System.out.println("Введите Q (простое число):");
        int q = scanner.nextInt();

        int n = p * q; // Вычисляем произведение p и q (часть открытого ключа)
        if (n < selectedDict.size()) {
            System.out.println("Введены маленькие значения p и q, возможны ошибки");
            return;
        }

        int f = (p - 1) * (q - 1); // Вычисляем значение функции Эйлера для p и q

        int e;
        // Пользователь вводит открытый ключ e или программа генерирует его автоматически
        System.out.println("Введите открытый ключ e (взаимно простое с " + f + "):");
        try {
            e = scanner.nextInt();
            if (gcd(f, e) != 1) {
                System.out.println("Введенный открытый ключ не подходит");
                return;
            }
        } catch (Exception ex) {
            // Генерируем список взаимно простых чисел с f, если пользователь не ввел e
            List<Integer> es = new ArrayList<>();
            for (int i = 2; i < f; i++) {
                if (gcd(f, i) == 1) {
                    es.add(i);
                }
            }
            Random rand = new Random();
            e = es.get(rand.nextInt(es.size()));
        }

        int d = generatePrivateKey(e, f); // Генерируем закрытый ключ d

        // Выводим открытый и закрытый ключи
        System.out.println("Открытый ключ (e): " + e);
        System.out.println("Закрытый ключ (d): " + d);

        // Пользователь вводит текст для шифрования
        System.out.println("Введите текст для шифрования (разделять пробелами):");
        scanner.nextLine(); // Очищаем буфер
        String input = scanner.nextLine();

        // Шифруем введенный текст
        List<Integer> encrypted = encrypt(input, selectedDict, e, n);

        // Выводим зашифрованный текст
        System.out.println("Зашифрованный текст:");
        for (int num : encrypted) {
            System.out.print(num + " ");
        }
        System.out.println();

        // Пользователь вводит зашифрованный текст для дешифрования
        System.out.println("Введите закодированный текст для дешифрования (разделять пробелами):");
        String encryptedInput = scanner.nextLine();
        List<Integer> encryptedList = new ArrayList<>();
        for (String numStr : encryptedInput.split(" ")) {
            encryptedList.add(Integer.parseInt(numStr));
        }

        // Дешифруем текст и выводим результат
        String decrypted = decrypt(encryptedList, selectedDict, d, n);

        System.out.println("Дешифрованный текст:");
        System.out.println(decrypted);

        scanner.close();

    }

    // Находим наибольший общий делитель двух чисел (алгоритм Евклида)
    public static int gcd(int a, int b) {
        if (b == 0) {
            return a;
        }
        return gcd(b, a % b);
    }

    // Генерация закрытого ключа d
    public static int generatePrivateKey(int e, int f) {
        int d;
        Random rand = new Random();
        d = rand.nextInt(1000) + 1;
        while ((e * d) % f != 1) {
            d++;
        }
        return d;
    }

    // Метод для шифрования текста
    public static List<Integer> encrypt(String input, List<Character> dict, int e, int n) {

        List<Integer> encrypted = new ArrayList<>();
        for (char ch : input.toCharArray()) {
            int index = dict.indexOf(ch);
            if (index != -1) {
                encrypted.add(modPow(index, e, n));
            }
        }
        return encrypted;
    }

    // Метод для дешифрования текста
    public static String decrypt(List<Integer> encryptedList, List<Character> dict, int d, int n) {
        StringBuilder decrypted = new StringBuilder();
        for (int num : encryptedList) {
            char ch = dict.get(modPow(num, d, n));
            decrypted.append(ch);
        }
        return decrypted.toString();
    }

    // Вычисляем значение base в степени exponent по модулю modulus (быстрое возведение в степень по модулю)
    public static int modPow(int base, int exponent, int modulus) {
        int result = 1;
        base = base % modulus;
        while (exponent > 0) {
            if (exponent % 2 == 1) {
                result = (result * base) % modulus;
            }
            exponent = exponent >> 1; // эквивалент деления на 2
            base = (base * base) % modulus;
        }
        return result;
    }


}

