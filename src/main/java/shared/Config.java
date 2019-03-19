package shared;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Config {

    private static String serverAddress;
    private static int serverPort;

    private static int numOfReaders;
    private static List<String> readers;

    private static int numOfWriters;
    private static List<String> writers;

    private static int numOfAccesses;

    static {
        readers = new ArrayList<String>();
        writers = new ArrayList<String>();

        setConfig();
    }

    public static String getServerAddress() {
        return serverAddress;
    }

    public static int getServerPort() {
        return serverPort;
    }

    public static int getNumOfReaders() {
        return numOfReaders;
    }

    public static List<String> getReaders() {
        return readers;
    }

    public static int getNumOfWriters() {
        return numOfWriters;
    }

    public static List<String> getWriters() {
        return writers;
    }

    public static int getNumOfAccesses() {
        return numOfAccesses;
    }

    private static void setConfig() {
        Properties prop = new Properties();
        InputStream input = null;

        try {
            input = ClassLoader.getSystemClassLoader().getResourceAsStream("system.properties");
            prop.load(input);

            serverAddress = prop.getProperty("RW.server");
            serverPort = Integer.parseInt(prop.getProperty("RW.server.port"));

            numOfReaders = Integer.parseInt(prop.getProperty("RW.numberOfReaders"));
            for (int i = 0; i < numOfReaders; i++)
                readers.add(prop.getProperty("RW.reader") + i);

            numOfWriters = Integer.parseInt(prop.getProperty("RW.numberOfWriters"));
            for (int i = 0; i < numOfWriters; i++)
                readers.add(prop.getProperty("RW.writer") + i);

            numOfAccesses = Integer.parseInt(prop.getProperty("RW.numberOfAccesses"));
        } catch (IOException e) {
            System.out.println(e);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        }
    }
}
