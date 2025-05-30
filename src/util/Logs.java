package util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Logs {

    private static final String LOG_FILE = "src/Files/log.txt";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");



    private File createFile() {
        File file = new File(LOG_FILE);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            return file;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao criar arquivo de log: " + e.getMessage());
        }
    }

    public void writeLog(String description) {
        File logFile = createFile();
        try (FileWriter writer = new FileWriter(logFile, true)) {
            String logEntry = String.format("[%s] %s%n",
                    LocalDate.now().format(DATE_FORMATTER),
                    description);
            writer.write(logEntry);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao escrever no arquivo de log: " + e.getMessage());
        }
    }

}
