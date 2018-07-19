package cn.edu.jit.tianyu_paas.shared.util;

import com.jcraft.jsch.Session;

public class DockerRunUtils {

    public static String dockerRunCmd(String hostName, String username, String pwd, String cmd) {
        String stdout = null;
        try {
            DockerRunSSHUtils.DestHost host = new DockerRunSSHUtils.DestHost(hostName, username, pwd);

            Session shellSession = DockerRunSSHUtils.getJSchSession(host);
            stdout = DockerRunSSHUtils.execCommandByJSch(shellSession, cmd);
            System.out.println(stdout);
            shellSession.disconnect();

            return stdout;
        } catch (Exception e) {
            e.printStackTrace();

            return stdout;
        }
    }
}
