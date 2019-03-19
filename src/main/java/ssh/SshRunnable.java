package ssh;

import java.util.Random;

public abstract class SshRunnable implements Runnable {
    SshConnection sshConnection;
    private int numberOfAccesses;
    private static final int MAX_SLEEPING_INTERVAL = 10000;

    public SshRunnable(String user, String host, int port, int numberOfAccesses) throws SshConnection.SshException {
        this.sshConnection = new SshConnection(user, host, port);
        this.numberOfAccesses = numberOfAccesses;
    }

    public abstract void executeCommand() throws SshConnection.SshException;

    @Override
    public void run() {
        while (this.numberOfAccesses > 0) {
            try {
                sshConnection.connect();
                this.executeCommand();
                sshConnection.disconnect();
            } catch (SshConnection.SshException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(new Random().nextInt(MAX_SLEEPING_INTERVAL));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            this.numberOfAccesses--;
        }
    }
}
