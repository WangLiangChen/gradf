package liangchen.wang.gradf.framework.springboot.monitor;

import org.springframework.boot.*;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.context.*;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

/**
 * @author LiangChen.Wang 2021/7/2
 */
@Component
public class StartProcessMonitor implements EnvironmentPostProcessor
        , ApplicationContextInitializer<ConfigurableApplicationContext>
        , SpringApplicationRunListener
        , ApplicationListener<ApplicationEvent>
        , Lifecycle
        , ApplicationRunner
        , CommandLineRunner {
    private final String prefix = "------ StartProcessMonitor,interface:%s,method:%s";
    private boolean isRunning;

    public StartProcessMonitor(SpringApplication application, String[] args) {
        System.out.println(String.format(prefix, "SpringApplicationRunListener", "constructor"));
    }

    public StartProcessMonitor() {
    }

    @Override
    public void starting(ConfigurableBootstrapContext bootstrapContext) {
        System.out.println(String.format(prefix, "SpringApplicationRunListener", "starting"));
    }

    @Override
    public void environmentPrepared(ConfigurableBootstrapContext bootstrapContext, ConfigurableEnvironment environment) {
        System.out.println(String.format(prefix, "SpringApplicationRunListener", "environmentPrepared"));
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        System.out.println(String.format(prefix, "SpringApplicationRunListener", "contextPrepared"));
    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
        System.out.println(String.format(prefix, "SpringApplicationRunListener", "contextLoaded"));
    }

    @Override
    public void started(ConfigurableApplicationContext context) {
        System.out.println(String.format(prefix, "SpringApplicationRunListener", "started"));
    }

    @Override
    public void running(ConfigurableApplicationContext context) {
        System.out.println(String.format(prefix, "SpringApplicationRunListener", "running"));
    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {
        System.out.println(String.format(prefix, "SpringApplicationRunListener", "failed"));
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println(String.format(prefix, "ApplicationRunner", "run"));
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(String.format(prefix, "CommandLineRunner", "run"));
    }

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        System.out.println(String.format(prefix, "EnvironmentPostProcessor", "postProcessEnvironment"));
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        System.out.println(String.format(prefix, "ApplicationContextInitializer", "initialize"));
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        System.out.println(String.format(prefix, "ApplicationListener", "onApplicationEvent"));
    }

    @Override
    public void start() {
        this.isRunning = true;
        System.out.println(String.format(prefix, "Lifecycle", "start"));
    }

    @Override
    public void stop() {
        this.isRunning = false;
        System.out.println(String.format(prefix, "Lifecycle", "stop"));
    }

    @Override
    public boolean isRunning() {
        return this.isRunning;
    }
}
