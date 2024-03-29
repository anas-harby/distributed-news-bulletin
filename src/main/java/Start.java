import server.Server;
import server.ServerArgs;
import server.ServerRunner;
import shared.Config;
import shared.Dispatcher;
import shared.logger.Logger;
import ssh.SshConnection;
import ssh.SshRunnable;

public class Start {
    private static final int DEFAULT_SSH_PORT = 22;

    public static void main(String[] args) {
        Logger.clear();
        Server server = ServerRunner.getServer(args);
        try {
            server.listen();
            System.out.println("--Server started--");
            System.out.println("Waiting for clients...");
            System.out.println("----------------------");

            Dispatcher sshDispatcher = dispatchSshThreads(ServerRunner.resolveArgs(args).getArch());
            server.accept();
            server.terminate();
            sshDispatcher.shutdown();
        } catch (Server.ServerException e) {
            e.printStackTrace();
        }
    }

    private static Dispatcher dispatchSshThreads(String arch) {
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
                        SshRunnable.Mode.READ,
                        arch.equals(ServerArgs.HTTP) ? SshRunnable.Arch.HTTP : SshRunnable.Arch.RMI
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
                        SshRunnable.Mode.WRITE,
                        arch.equals(ServerArgs.HTTP) ? SshRunnable.Arch.HTTP : SshRunnable.Arch.RMI
                ));
            } catch (SshConnection.SshException e) {
                e.printStackTrace();
            }
        }
        return sshDispatcher;
    }
}
