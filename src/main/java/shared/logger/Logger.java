package shared.logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public abstract class Logger {
    private File logFile;
    private static final String LOG_PATH = "./logs/log$.txt";

    static {
        new File("logs").mkdir();
    }

    public Logger(int id) throws IOException {
        this.logFile = initFile(LOG_PATH.replace("$", String.valueOf(id)));
    }

    private File initFile(String path) throws IOException {
        File logFile = new File(path);
        if (!logFile.exists()) {
            logFile.createNewFile();
            this.writeHeaders(logFile);
        }
        return logFile;
    }

    public synchronized void writeToFile(String[] values) throws IOException {
        String toWrite = String.join("\t\t", values) + "\n";
        FileWriter fileWriter = new FileWriter(this.logFile, true);
        fileWriter.write(toWrite);
        fileWriter.flush();
        fileWriter.close();
    }

    public abstract void writeHeaders(File logFile);
}