import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) {
        try {
            // Код на Python
            String pythonCode = "print('Hello from Python!')";

            // Создаем процесс Python
            ProcessBuilder pb = new ProcessBuilder("python", "-c", pythonCode);
            Process process = pb.start();

            // Получаем вывод выполнения
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // Ждем завершения процесса
            int exitCode = process.waitFor();
            System.out.println("Python код выполнен. Код завершился с кодом: " + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
