package Helpers;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class AuditEngine {
    private static Path path;

    public static void initialize(String pathOfFile) {
        path = Paths.get(pathOfFile);
    }

    public static void log(String mainMessage, ArrayList<AdjustmentLog> logs) {
        // the CLI argument was set to '0', therefore initialize() was never called
        if (path == null)
            return;

        try {
            Files.writeString(path, "[LOG - " + LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME) + "] " + mainMessage + "\n", StandardOpenOption.CREATE, StandardOpenOption.APPEND);

            if (logs != null)
                for (AdjustmentLog log : logs)
                    Files.writeString(path, log + "\n", StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        }
        catch (IOException e) {
            System.out.println("[*] Log file related error.");
            System.exit(1);
        }
    }
}
