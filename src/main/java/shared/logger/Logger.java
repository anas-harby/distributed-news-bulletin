package shared.logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {
    private FileWriter fileWriter = null;
    private static final String LOG_PATH = "./logs/log$.txt";

    public Logger(int id) throws IOException {
        File logFile = initFile(LOG_PATH.replace("$", String.valueOf(id)));
        this.fileWriter = new FileWriter(logFile, true);
    }

    private File initFile(String path) throws IOException {
        File logFile = new File(path);
        if (!logFile.exists())
            logFile.createNewFile();
        return logFile;
    }

    public synchronized void writeToFile(String[] values) throws IOException {
        String toWrite = String.join("\t", values);
        this.fileWriter.write(toWrite);
        this.fileWriter.flush();
    }

    public synchronized void close() throws IOException {
        this.fileWriter.close();
    }
}
