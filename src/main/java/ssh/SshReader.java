package ssh;

public class SshReader extends SshRunnable {
    public SshReader(String user, String host, int port, int numberOfAccesses) throws SshConnection.SshException {
        super(user, host, port, numberOfAccesses);
    }

    @Override
    public void executeCommand() throws SshConnection.SshException {
        //TODO: figure out what command to run.
        super.sshConnection.runCommand("RUN CLIENT -id 5 -mode r");
    }
}
