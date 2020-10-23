package liangchen.wang.gradf.framework.commons.utils;

import liangchen.wang.gradf.framework.commons.exception.ErrorException;
import liangchen.wang.gradf.framework.commons.validator.Assert;
import org.apache.commons.configuration2.*;
import org.apache.commons.configuration2.builder.ReloadingFileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.FileBasedBuilderParameters;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.reloading.PeriodicReloadingTrigger;

import java.net.URI;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * @author LiangChen.Wang
 */
public enum ConfigurationUtil {
    /**
     * instance
     */
    INSTANCE;
    private final static String XML = ".xml";
    private final static String PROPERTIES = ".properties";
    private final static String YML = ".yml";
    private final static String YAML = ".yaml";
    private final static String JSON = ".json";
    private final FileBasedBuilderParameters fileBasedBuilderParameters = new Parameters().fileBased().setListDelimiterHandler(new DefaultListDelimiterHandler(','));
    private final ReloadingFileBasedConfigurationBuilder<PropertiesConfiguration> propertiesConfigurationBuilder = new ReloadingFileBasedConfigurationBuilder<>(PropertiesConfiguration.class);
    private final ReloadingFileBasedConfigurationBuilder<YAMLConfiguration> yamlConfigurationBuilder = new ReloadingFileBasedConfigurationBuilder<>(YAMLConfiguration.class);
    private final ReloadingFileBasedConfigurationBuilder<JSONConfiguration> jsonConfigurationBuilder = new ReloadingFileBasedConfigurationBuilder<>(JSONConfiguration.class);
    private final ReloadingFileBasedConfigurationBuilder<XMLConfiguration> xmlConfigurationBuilder = new ReloadingFileBasedConfigurationBuilder<>(XMLConfiguration.class);

    private URI baseUri;

    public void setBasePath(String basePath, String... more) {
        Assert.INSTANCE.notBlank(basePath, "配置路径不能为空");
        baseUri = NetUtil.INSTANCE.toURI(basePath, more);
    }

    public String getBasePath() {
        return baseUri.toString();
    }

    public URL getFullUrl(String fileName) {
        return NetUtil.INSTANCE.toURL(getFullUri(fileName));
    }

    public URI getFullUri(String fileName) {
        Assert.INSTANCE.notBlank(fileName, "文件路径/名称不能为空");
        return baseUri.resolve(fileName);
    }

    public String getFullPath(String fileName) {
        return getFullUrl(fileName).toString();
    }


    public Configuration getConfiguration(String configurationFileName) {
        return getConfiguration(configurationFileName, 0, null);
    }

    public Configuration getConfiguration(String configurationFileName, long reloadPeriod, TimeUnit timeUnit) {
        if (configurationFileName.endsWith(PROPERTIES)) {
            return getPropertiesConfiguration(configurationFileName, reloadPeriod, timeUnit);
        }
        if (configurationFileName.endsWith(YML) || configurationFileName.endsWith(YAML)) {
            return getYamlConfiguration(configurationFileName, reloadPeriod, timeUnit);
        }
        if (configurationFileName.endsWith(JSON)) {
            return getJsonConfiguration(configurationFileName, reloadPeriod, timeUnit);
        }
        if (configurationFileName.endsWith(XML)) {
            return getXmlConfiguration(configurationFileName, reloadPeriod, timeUnit);
        }
        return null;
    }

    public Configuration getPropertiesConfiguration(String configurationFileName) {
        return getPropertiesConfiguration(configurationFileName, 0, null);
    }

    public Configuration getPropertiesConfiguration(String configurationFileName, long reloadPeriod, TimeUnit unit) {
        Assert.INSTANCE.notBlank(configurationFileName, "配置文件路径/名称不能为空");
        Assert.INSTANCE.isTrue(configurationFileName.endsWith(PROPERTIES), "要加载的配置文件不是properties格式,文件:{0}", configurationFileName);
        return buildConfiguration(propertiesConfigurationBuilder, configurationFileName, reloadPeriod, unit);
    }

    public Configuration getYamlConfiguration(String configurationFileName) {
        return getYamlConfiguration(configurationFileName, 0, null);
    }

    public Configuration getYamlConfiguration(String configurationFileName, long reloadPeriod, TimeUnit unit) {
        Assert.INSTANCE.notBlank(configurationFileName, "配置文件路径/名称不能为空");
        Assert.INSTANCE.isTrue(configurationFileName.endsWith(YML), "要加载的配置文件不是yml格式,文件:{0}", configurationFileName);
        return buildConfiguration(yamlConfigurationBuilder, configurationFileName, reloadPeriod, unit);
    }

    public Configuration getJsonConfiguration(String configurationFileName) {
        return getJsonConfiguration(configurationFileName, 0, null);
    }

    public Configuration getJsonConfiguration(String configurationFileName, long reloadPeriod, TimeUnit unit) {
        Assert.INSTANCE.notBlank(configurationFileName, "配置文件路径/名称不能为空");
        Assert.INSTANCE.isTrue(configurationFileName.endsWith(JSON), "要加载的配置文件不是yml格式,文件:{0}", configurationFileName);
        return buildConfiguration(jsonConfigurationBuilder, configurationFileName, reloadPeriod, unit);
    }

    public Configuration getXmlConfiguration(String configurationFileName) {
        return getXmlConfiguration(configurationFileName, 0, null);
    }

    public Configuration getXmlConfiguration(String configurationFileName, long reloadPeriod, TimeUnit unit) {
        Assert.INSTANCE.notBlank(configurationFileName, "配置文件路径/名称不能为空");
        Assert.INSTANCE.isTrue(configurationFileName.endsWith(XML), "要加载的配置文件不是xml格式,文件:{0}", configurationFileName);
        return buildConfiguration(xmlConfigurationBuilder, configurationFileName, reloadPeriod, unit);
    }


    private Configuration buildConfiguration(ReloadingFileBasedConfigurationBuilder<?> builder, String configurationFileName, long reloadPeriod, TimeUnit timeUnit) {
        URL url = getFullUrl(configurationFileName);
        builder.configure(fileBasedBuilderParameters.setURL(url));
        if (reloadPeriod > 0L) {
            PeriodicReloadingTrigger trigger = new PeriodicReloadingTrigger(builder.getReloadingController(), null, reloadPeriod, timeUnit);
            trigger.start();
        }
        try {
            return builder.getConfiguration();
        } catch (ConfigurationException e) {
            throw new ErrorException(e);
        }
    }

}
