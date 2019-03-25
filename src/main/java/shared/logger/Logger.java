package shared.logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

public abstract class Logger {
    private File logFile;
    private static final String LOG_PATH = "./logs/log$.txt";

    public Logger(int id) throws IOException {
        this.logFile = initFile(LOG_PATH.replace("$", String.valueOf(id)));
    }

    public Logger() {
        // TODO: log files names
    }

    public static void clear() {
        File logDir = new File("logs");
        if (logDir.exists()) {
            try {
                Files.walk(Paths.get(logDir.getPath()))
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        logDir.mkdir();
    }

    public synchronized void writeToFile(String[] values) throws IOException {
        String toWrite = String.join("\t\t", values) + "\n";
        FileWriter fileWriter = new FileWriter(this.logFile, true);
        fileWriter.write(toWrite);
        fileWriter.flush();
        fileWriter.close();
    }

    public abstract void writeHeaders(File logFile);

    private File initFile(String path) throws IOException {
        File logFile = new File(path);
        if (!logFile.exists()) {
            logFile.createNewFile();
            this.writeHeaders(logFile);
        }
        return logFile;
    }
}