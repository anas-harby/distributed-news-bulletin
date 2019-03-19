package ssh;

import com.jcraft.jsch.*;

import java.io.IOException;
import java.io.InputStream;

public class SshConnection {
    class SshException extends Exception {
        SshException(Exception e) {
            super(e.getMessage(), e.getCause());
        }
    }
    private String username;
    private String host;
    private int port;

    private JSch jsch;
    private Session session;

    public SshConnection(String username, String host, int port) throws SshException {
        this.username = username;
        this.host = host;
        this.port = port;

        try {
            this.jsch = new JSch();
            this.setupSshKeys();
            this.session = this.jsch.getSession(username, host, port);
            this.configureSession();
        } catch (JSchException e) {
            throw new SshException(e);
        }
    }

    private void setupSshKeys() throws JSchException {
        String privateKey = "/home/" + username + "/.ssh/id_rsa";
        String knownHosts = "/home/" + username + "/.ssh/known_hosts";

        this.jsch.addIdentity(privateKey);
        this.jsch.setKnownHosts(knownHosts);
    }

    private void configureSession() {
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        this.session.setConfig(config);
    }

    public void connect() throws SshException {
        try {
            this.session.connect();
        } catch (JSchException e) {
            throw new SshException(e);
        }
    }

    public void runCommand(String command) throws SshException {
        try {
            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);

            channel.connect();
            InputStream in = channel.getInputStream();

            byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0)
                        break;
                    System.out.print(new String(tmp, 0, i));
                }
                if (channel.isClosed()) {
                    if (in.available() > 0)
                        continue;
                    System.out.println("exit-status: " + channel.getExitStatus());
                    break;
                }
                try {
                    Thread.sleep(100);
                } catch (Exception ignored) { }
            }

            channel.disconnect();
        } catch (JSchException | IOException e) {
            throw new SshException(e);
        }
    }

    public void disconnect() {
        this.session.disconnect();
    }
}
