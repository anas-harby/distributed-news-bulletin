import shared.logger.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Start {
    //TODO: 2. Logger
    //TODO: 3. shared.Dispatcher
    //TODO: 4. SSH Command to run
    //TODO: 5. Integrate all
    public static void main(String[] args) {
        int id = 1;
        try {
            Logger logger = new Logger(id) {
                @Override
                public void writeHeaders(File logFile) {
                    try {
                        FileWriter writer = new FileWriter(logFile, true);
                        writer.write("Client ID: " + id + "\n");
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            logger.writeToFile(new String[] {"as", "be", "to"});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
