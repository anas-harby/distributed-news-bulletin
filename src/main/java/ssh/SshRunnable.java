package ssh;

import java.util.Random;

public class SshRunnable implements Runnable {
    public enum Mode {
        READ, WRITE
    }
    public enum Arch {
        HTTP, RMI
    }
    private SshConnection sshConnection;
    private int numberOfAccesses;
    private int id;
    private Mode mode;
    private Arch arch;
    private static final int MAX_SLEEPING_INTERVAL = 10000;

    public SshRunnable(String user, String host, int port, int numberOfAccesses, int id, Mode mode, Arch arch)
            throws SshConnection.SshException {
        this.sshConnection = new SshConnection(user, host, port);
        this.numberOfAccesses = numberOfAccesses;
        this.id = id;
        this.mode = mode;
        this.arch = arch;
    }

    private void executeCommand() throws SshConnection.SshException {
        String path = System.getProperty("user.dir").replaceAll(" ", "\\\\ ");
        String command = "cd " + path + " ;"
                       + "java -jar Client*.jar"
                       + " -id " + id
                       + " -mode " + (this.mode == Mode.READ ? "r" : "w")
                       + " -arch" + (this.arch == Arch.HTTP ? "http" : "rmi");
        System.out.println("Executing ssh command: " + command);
        this.sshConnection.runCommand(command);
    }

    @Override
    public void run() {
        try {
            while (this.numberOfAccesses > 0) {
                sshConnection.connect();
                this.executeCommand();
                sshConnection.disconnect();
                Thread.sleep(new Random().nextInt(MAX_SLEEPING_INTERVAL));
                this.numberOfAccesses--;
            }
        } catch (SshConnection.SshException | InterruptedException ignored) { }
    }
}
