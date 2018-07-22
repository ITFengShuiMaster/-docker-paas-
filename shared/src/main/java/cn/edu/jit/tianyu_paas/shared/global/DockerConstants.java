package cn.edu.jit.tianyu_paas.shared.global;

public class DockerConstants {

    public static String mountUrl = "http://%s:2375/containers/%s/archive?path=%s";

    public static String containerUrl = "http://%s:2375/containers/%s/export";
}
