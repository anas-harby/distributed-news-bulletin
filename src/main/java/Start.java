import server.Server;
import shared.Config;
import shared.Dispatcher;
import shared.logger.Logger;
import ssh.SshConnection;
import ssh.SshRunnable;

import java.io.IOException;

public class Start {
    //TODO: 2. Logger
    //TODO: 3. shared.Dispatcher
    //TODO: 4. SSH Command to run
    //TODO: 5. Integrate all
    private static final int DEFAULT_SSH_PORT = 22;

    public static void main(String[] args) {
        Logger.clear();
        Server server = new Server();
        try {
            server.listen();
            System.out.println("--Server started--");
            System.out.println("Waiting for clients...");
            System.out.println("----------------------");

            dispatchSshThreads();
            server.accept();
            server.terminate();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void dispatchSshThreads() {
        Dispatcher sshDispatcher = new Dispatcher(
                Config.getNumOfReaders() + Config.getNumOfWriters());
        /* Dispatch Readers ssh invocation */
        for (int i = 0; i < Config.getNumOfReaders(); i++) {
            try {
                sshDispatcher.dispatch(new SshRunnable(
                        System.getProperty("user.name"),
                        Config.getReaders().get(i),
                        DEFAULT_SSH_PORT,
                        Config.getNumOfAccesses(),
                        i ,
                        SshRunnable.Mode.READ
                ));
            } catch (SshConnection.SshException e) {
                e.printStackTrace();
            }
        }

        /* Dispatch Writers ssh invocation */
        for (int i = 0; i < Config.getNumOfWriters(); i++) {
            try {
                sshDispatcher.dispatch(new SshRunnable(
                        System.getProperty("user.name"),
                        Config.getWriters().get(i),
                        DEFAULT_SSH_PORT,
                        Config.getNumOfAccesses(),
                        i + Config.getNumOfReaders(),
                        SshRunnable.Mode.WRITE
                ));
            } catch (SshConnection.SshException e) {
                e.printStackTrace();
            }
        }
    }
}
