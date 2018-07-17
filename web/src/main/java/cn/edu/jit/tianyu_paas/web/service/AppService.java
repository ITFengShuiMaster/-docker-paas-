package cn.edu.jit.tianyu_paas.web.service;

import cn.edu.jit.tianyu_paas.shared.entity.*;
import cn.edu.jit.tianyu_paas.shared.util.*;
import cn.edu.jit.tianyu_paas.web.global.Constants;
import cn.edu.jit.tianyu_paas.web.mapper.AppMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.DockerCmdExecFactory;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.jaxrs.JerseyDockerCmdExecFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.*;

/**
 * @author 卢越
 * @date 2018-6-30 10.48
 */
@Service
public class AppService extends ServiceImpl<AppMapper, App> {

    private final HttpSession session;
    private final Logger logger = LoggerFactory.getLogger(AppService.class);
    private MachineService machineService;
    private MachinePortService machinePortService;
    private MarketAppService marketAppService;
    private MarketAppPortService marketAppPortService;
    private MarketAppVarService marketAppVarService;
    private AppVarService appVarService;
    private AppPortService appPortService;
    private AppInfoByDockerImageService appInfoByDockerImageService;
    private RedisTemplate redisTemplate;

    @Autowired
    public AppService(HttpSession session, MachineService machineService, MachinePortService machinePortService, AppInfoByDockerImageService appInfoByDockerImageService, MarketAppService marketAppService, MarketAppPortService marketAppPortService, MarketAppVarService marketAppVarService, AppVarService appVarService, AppPortService appPortService, RedisTemplate redisTemplate) {
        this.session = session;
        this.machineService = machineService;
        this.machinePortService = machinePortService;
        this.appInfoByDockerImageService = appInfoByDockerImageService;
        this.marketAppService = marketAppService;
        this.marketAppPortService = marketAppPortService;
        this.marketAppVarService = marketAppVarService;
        this.appVarService = appVarService;
        this.appPortService = appPortService;
        this.redisTemplate = redisTemplate;
    }

    @Cacheable(value = "app", key = "#id.toString()")
    @Override
    public App selectById(Serializable id) {
        return super.selectById(id);
    }

    @Override
    public boolean insert(App entity) {
        if (super.insert(entity)) {
            redisTemplate.opsForValue().set("app::" + entity.getAppId().toString(), baseMapper.selectById(entity.getAppId()));
            return true;
        }
        return false;
    }

    @CacheEvict(value = "app", key = "#id.toString()")
    @Override
    public boolean deleteById(Serializable id) {
        return super.deleteById(id);
    }

    @Override
    public boolean update(App entity, Wrapper<App> wrapper) {
        if (super.update(entity, wrapper)) {
            redisTemplate.opsForValue().set("app::" + entity.getAppId().toString(), baseMapper.selectById(entity.getAppId()));
            return true;
        }
        return false;
    }

    @Override
    public boolean updateById(App entity) {
        if (super.updateById(entity)) {
            redisTemplate.opsForValue().set("app::" + entity.getAppId().toString(), baseMapper.selectById(entity.getAppId()));
            return true;
        }
        return false;
    }

    /**
     * 总览分页
     *
     * @param app
     * @param pagination
     * @return
     */
    public Page<App> listAppsByNameAndStatus(App app, Pagination pagination) {
        // 当前页，条数 构造 page 对象
        Page<App> page = new Page<>(pagination.getCurrent(), pagination.getSize());
        return page.setRecords(baseMapper.selectListAppPage(app, page));
    }

    /**
     * 初始化appPort
     *
     * @param app
     * @param machinePorts
     * @return
     */
    private List<AppPort> initAppPort(App app, Map<MachinePort, Integer> machinePorts) {
        List<AppPort> appPorts = new ArrayList<>();
        for (MachinePort hostPort : machinePorts.keySet()) {
            AppPort appPort = new AppPort();
            appPort.setAppId(app.getAppId());
            appPort.setInsideAccessUrl("xxx");
            appPort.setInsideAlias("xxx");
            appPort.setIsInsideOpen(AppPort.INOPEN);
            appPort.setIsOutsideOpen(AppPort.OUTOPEN);
            appPort.setOutsideAccessUrl(machineService.selectOne(new EntityWrapper<Machine>().eq("machine_id", hostPort.getMachineId())).getMachineIp() + ":" + hostPort.getMachinePort());
            appPort.setHostPort(hostPort.getMachinePort());
            appPort.setContainerPort(machinePorts.get(hostPort));
            appPort.setProtocol(AppPort.MYSQL);
            appPort.setMachineId(hostPort.getMachineId());
            appPort.setGmtCreate(new Date());
            appPort.setGmtModified(new Date());
            appPorts.add(appPort);
        }
        return appPorts;
    }

    /**
     * 初始化appVar
     *
     * @param app
     * @param marketAppVarList
     * @return
     */
    private List<AppVar> initAppVar(App app, List<MarketAppVar> marketAppVarList) {
        List<AppVar> list = new ArrayList<>();
        for (MarketAppVar marketAppVar : marketAppVarList) {
            AppVar appVar = new AppVar();
            appVar.setAppId(app.getAppId());
            appVar.setVarName(marketAppVar.getVarName());
            appVar.setVarValue(marketAppVar.getValue());
            appVar.setVarExplain(marketAppVar.getVarExplain());
            appVar.setGmtCreate(new Date());
            list.add(appVar);
        }
        return list;
    }

    /**
     * 获取可用端口的信息
     *
     * @return
     */
    private List<MachinePort> getUsablePort() {
        return machinePortService.selectList(new EntityWrapper<MachinePort>().eq("status", 1).last("limit 50"));
    }

    /**
     * 拉取镜相
     *
     * @return
     */
    private Boolean pullImage(String imageName) {
        RestTemplate template = new RestTemplate();

        ResponseEntity<String> responseEntity = template.exchange(String.format(Constants.CREATE_IMAGE, imageName),
                HttpMethod.POST, null, String.class);

        if (responseEntity.getStatusCodeValue() != 200) {
            return false;
        }

        //还需要判断返回结果中是否存在error，存在则说明镜相不存在，拉取失败
        return StringUtil.isEmpty(responseEntity.getBody()) || !responseEntity.getBody().contains("error");
    }

    /**
     * 创建容器之后失败的扫尾工作
     *
     * @param dockerClient
     * @param containerId
     * @param appId
     */
    private void free(DockerClient dockerClient, String containerId, Long appId) {
        this.deleteById(appId);
        dockerClient.stopContainerCmd(containerId).exec();
    }

    /**
     * 创建容器之后失败的扫尾工作
     *
     * @param dockerClient
     * @param containerId
     * @param appId
     * @param image
     */
    private void free(DockerClient dockerClient, String containerId, Long appId, String image) {
        this.deleteById(appId);
        appInfoByDockerImageService.delete(new EntityWrapper<AppInfoByDockerImage>().eq("app_id", appId).and().eq("image", image));
        dockerClient.stopContainerCmd(containerId).exec();
    }

    /**
     * 获得dockerClient
     *
     * @return
     */
    public DockerClient getDockerClient() {
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost("tcp://120.77.146.118:2375")
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

    /**
     * 获得容器需要暴露的端口
     *
     * @param marketAppPortList
     * @return
     */
    public List<ExposedPort> listExposedPorts(List<MarketAppPort> marketAppPortList) {
        List<ExposedPort> exposedPorts = new ArrayList<>();
        for (MarketAppPort marketAppPort : marketAppPortList) {
            ExposedPort port = ExposedPort.tcp(marketAppPort.getPort());
            exposedPorts.add(port);
        }
        return exposedPorts;
    }

    /**
     * 获得容器绑定的端口
     *
     * @param exposedPorts
     * @param machinePorts
     * @param usedMachinePortList
     * @return
     */
    public Ports getPorts(List<ExposedPort> exposedPorts, List<MachinePort> machinePorts, Map<MachinePort, Integer> usedMachinePortList) {
        Ports portBindings = new Ports();
        //保存机器中已使用的端口
        for (ExposedPort exposedPort : exposedPorts) {
            portBindings.bind(exposedPort, Ports.Binding.bindPort(machinePorts.get(0).getMachinePort()));
            //将使用过的机器端口添加到集合中
            MachinePort machinePort = machinePorts.remove(0);
            machinePort.setStatus(2);
            usedMachinePortList.put(machinePort, exposedPort.getPort());
        }

        return portBindings;
    }

    /**
     * 获得容器需要的变量
     *
     * @param marketAppVarList
     * @return
     */
    public List<String> listEnvs(List<MarketAppVar> marketAppVarList) {
        List<String> envs = new ArrayList<>();

        for (MarketAppVar var : marketAppVarList) {
            if (StringUtil.isEmpty(var.getValue())) {
                var.setValue(PassUtil.getMD5(MailUtil.getRandomEmailCode()));
            }
            envs.add(var.getVarName() + "=" + var.getValue());
        }
        return envs;
    }

    /**
     * 创建容器
     *
     * @param exposedPorts
     * @param portBindings
     * @param envs
     * @param image
     * @return
     */
    public CreateContainerResponse createContainerResponse(List<ExposedPort> exposedPorts, Ports portBindings, List<String> envs, String image) {
        DockerClient dockerClient = getDockerClient();
        //配置所需要的容器信息
        CreateContainerCmd createContainerCmd = dockerClient.createContainerCmd(image)
                .withExposedPorts(exposedPorts)
                .withPortBindings(portBindings)
                .withEnv(envs);
        CreateContainerResponse createContainerResponse = null;
        try {
            createContainerResponse = createContainerCmd.exec();
            dockerClient.startContainerCmd(createContainerResponse.getId()).exec();
        } catch (Exception e) {
            logger.error("容器创建失败");
            e.printStackTrace();
            return createContainerResponse;
        }
        return createContainerResponse;
    }

    /**
     * 初始化容器
     *
     * @param app
     * @param dockerImage
     * @return
     */
    public TResult initContainer(App app, AppInfoByDockerImage dockerImage) {

        DockerClient dockerClient = getDockerClient();

        MarketApp marketApp = marketAppService.selectOne(new EntityWrapper<MarketApp>().eq("name", dockerImage.getImage()));
        if (marketApp != null) {
            //拉取镜相
            if (!pullImage(dockerImage.getImage())) {
                return TResult.failure("没有该镜像");
            }
            //判断容器所需端口和所需的变量
            List<MarketAppPort> marketAppPortList = marketAppPortService.selectList(new EntityWrapper<MarketAppPort>().eq("market_app_id", marketApp.getMarketAppId()));
            List<MarketAppVar> marketAppVarList = marketAppVarService.selectList(new EntityWrapper<MarketAppVar>().eq("market_app_id", marketApp.getMarketAppId()));

            //容器需要向外暴露的端口
            List<ExposedPort> exposedPorts = listExposedPorts(marketAppPortList);
            //获得可用端口信息
            List<MachinePort> machinePorts = getUsablePort();
            //保存机器中已使用的端口
            Map<MachinePort, Integer> usedMachinePortList = new HashMap<>();
            //将可用的机器端口映射到容器端口上
            Ports portBindings = getPorts(exposedPorts, machinePorts, usedMachinePortList);
            //获得容器所需的环境变量
            List<String> envs = listEnvs(marketAppVarList);

            CreateContainerResponse createContainerResponse = createContainerResponse(exposedPorts, portBindings, envs, dockerImage.getImage());
            if (createContainerResponse == null) {
                return TResult.failure(TResultCode.BUSINESS_ERROR);
            }

            app.setContainerId(createContainerResponse.getId());
            app.setMarketAppId(marketApp.getMarketAppId());
            if (!this.insert(app)) {
                dockerClient.stopContainerCmd(createContainerResponse.getId()).exec();
                return TResult.failure(TResultCode.BUSINESS_ERROR);
            }

            dockerImage.setAppId(app.getAppId());
            if (!appInfoByDockerImageService.insert(dockerImage)) {
                free(dockerClient, createContainerResponse.getId(), dockerImage.getAppId());
                return TResult.failure(TResultCode.BUSINESS_ERROR);
            }

            //将容器变量信息保存到app_var表中
            List<AppVar> varList = initAppVar(app, marketAppVarList);
            if (varList.size() != 0) {
                if (!appVarService.insertBatch(varList)) {
                    free(dockerClient, createContainerResponse.getId(), dockerImage.getAppId(), dockerImage.getImage());
                    return TResult.failure(TResultCode.BUSINESS_ERROR);
                }
            }

            //将容器端口信息保存到app_port表中
            List<AppPort> portList = initAppPort(app, usedMachinePortList);
            if (portList.size() != 0) {
                if (!appPortService.insertBatch(portList)) {
                    free(dockerClient, createContainerResponse.getId(), dockerImage.getAppId(), dockerImage.getImage());
                    return TResult.failure(TResultCode.BUSINESS_ERROR);
                }
            }

            //更新机器被占用端口的状态
            if (!machinePortService.updateMachinePortStatusByIdAndPort(usedMachinePortList)) {
                free(dockerClient, createContainerResponse.getId(), dockerImage.getAppId(), dockerImage.getImage());
                return TResult.failure(TResultCode.BUSINESS_ERROR);
            }

            return TResult.success();
        }
        return TResult.failure("数据库中无该应用！");
    }



}
