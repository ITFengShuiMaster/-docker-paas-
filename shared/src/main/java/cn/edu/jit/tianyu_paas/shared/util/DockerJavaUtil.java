package cn.edu.jit.tianyu_paas.shared.util;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.DockerCmdExecFactory;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.jaxrs.JerseyDockerCmdExecFactory;

public class DockerJavaUtil {

    public static DockerClient getDockerClient() {
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost("tcp://47.106.37.79:2375")
                .withRegistryUsername("itfengshuimaster")
                .withRegistryPassword("wxhzq520")
                .withRegistryEmail("wxhzq520@sina.com")
                .withRegistryUrl("https://hub.docker.com/r/itfengshuimaster/mydocker/")
                .build();
        DockerCmdExecFactory dockerCmdExecFactory = new JerseyDockerCmdExecFactory()
                .withReadTimeout(100000)
                .withConnectTimeout(100000)
                .withMaxTotalConnections(100)
                .withMaxPerRouteConnections(10);

        return DockerClientBuilder.getInstance(config)
                .withDockerCmdExecFactory(dockerCmdExecFactory)
                .build();
    }
}
