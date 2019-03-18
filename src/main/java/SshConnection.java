import com.jcraft.jsch.*;

import java.io.*;

public class SshConnection {
    public static void main(String[] args) {
        JSch jsch = new JSch();

        String loc = System.getProperty("user.name") + "@localhost";
        String user = loc.substring(0, loc.indexOf("@"));
        String host = loc.substring(loc.indexOf("@") + 1);
        int port = 22;

        String privateKey = System.getProperty("user.home") + "/.ssh/id_rsa";
        String knownHosts = System.getProperty("user.home") + "/.ssh/known_hosts";

        try {
            jsch.addIdentity(privateKey);
            jsch.setKnownHosts(knownHosts);

            System.out.println("Connecting via ssh to " + loc);
            Session session = jsch.getSession(user, host, port);

            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

            session.connect();
            System.out.println("Session connected to " + loc);

            String command = "set | grep SSH";
            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);

            System.out.println("Executing command: " + command + "\n");
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
            session.disconnect();
        } catch (JSchException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
