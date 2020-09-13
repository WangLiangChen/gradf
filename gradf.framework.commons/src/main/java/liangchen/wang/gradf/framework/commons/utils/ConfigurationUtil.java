package liangchen.wang.gradf.framework.commons.utils;

import liangchen.wang.gradf.framework.commons.exception.ErrorException;
import liangchen.wang.gradf.framework.commons.validator.Assert;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.YAMLConfiguration;
import org.apache.commons.configuration2.builder.ReloadingFileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.reloading.PeriodicReloadingTrigger;

import java.net.URL;
import java.util.concurrent.TimeUnit;

public enum ConfigurationUtil {
    /**
     * instance
     */
    INSTANCE;
    private final static String XML = "xml";
    private final static String PROPERTIES = "properties";
    private final static String YML = "yml";
    private final static String YAML = "yaml";

    private URL baseUrl;

    public void setBasePath(String basePath, String... more) {
        Assert.INSTANCE.notBlank(basePath, "配置路径不能为空");
        baseUrl = NetUtil.INSTANCE.toURL(basePath, more);
    }

    public String getBasePath() {
        return baseUrl.toString();
    }

    public URL getFullUrl(String fileName) {
        Assert.INSTANCE.notBlank(fileName, "文件路径/名称不能为空");
        return NetUtil.INSTANCE.toURL(baseUrl, fileName);
    }

    public String getFullPath(String fileName) {
        return getFullUrl(fileName).toString();
    }


    public Configuration getConfiguration(String configurationFileName) {
        return getConfiguration(configurationFileName, 0, null);
    }

    public Configuration getConfiguration(String configurationFileName, long reloadPeriod, TimeUnit unit) {
        if (configurationFileName.endsWith(PROPERTIES)) {
            return getPropertiesConfiguration(configurationFileName, reloadPeriod, unit);
        }
        if (configurationFileName.endsWith(YML)) {
            return getYamlConfiguration(configurationFileName, reloadPeriod, unit);
        }
        if (configurationFileName.endsWith(XML)) {
            return getXmlConfiguration(configurationFileName, reloadPeriod, unit);
        }
        return null;
    }

    public Configuration getYamlConfiguration(String configurationFileName) {
        return getYamlConfiguration(configurationFileName, 0, null);
    }

    public Configuration getYamlConfiguration(String configurationFileName, long reloadPeriod, TimeUnit unit) {
        Assert.INSTANCE.notBlank(configurationFileName, "配置文件路径/名称不能为空");
        Assert.INSTANCE.isTrue(configurationFileName.endsWith(YML), "要加载的配置文件不是yml格式,文件:{0}", configurationFileName);
        ReloadingFileBasedConfigurationBuilder<YAMLConfiguration> builder = new ReloadingFileBasedConfigurationBuilder<>(YAMLConfiguration.class);
        return buildConfiguration(builder, configurationFileName, reloadPeriod, unit);
    }

    public Configuration getXmlConfiguration(String configurationFileName) {
        return getXmlConfiguration(configurationFileName, 0, null);
    }

    public Configuration getXmlConfiguration(String configurationFileName, long reloadPeriod, TimeUnit unit) {
        Assert.INSTANCE.notBlank(configurationFileName, "配置文件路径/名称不能为空");
        Assert.INSTANCE.isTrue(configurationFileName.endsWith(XML), "要加载的配置文件不是xml格式,文件:{0}", configurationFileName);
        ReloadingFileBasedConfigurationBuilder<XMLConfiguration> builder = new ReloadingFileBasedConfigurationBuilder<>(XMLConfiguration.class);
        return buildConfiguration(builder, configurationFileName, reloadPeriod, unit);
    }

    public Configuration getPropertiesConfiguration(String configurationFileName) {
        return getPropertiesConfiguration(configurationFileName, 0, null);
    }

    public Configuration getPropertiesConfiguration(String configurationFileName, long reloadPeriod, TimeUnit unit) {
        Assert.INSTANCE.notBlank(configurationFileName, "配置文件路径/名称不能为空");
        Assert.INSTANCE.isTrue(configurationFileName.endsWith(PROPERTIES), "要加载的配置文件不是properties格式,文件:{0}", configurationFileName);
        ReloadingFileBasedConfigurationBuilder<PropertiesConfiguration> builder = new ReloadingFileBasedConfigurationBuilder<>(PropertiesConfiguration.class);
        return buildConfiguration(builder, configurationFileName, reloadPeriod, unit);
    }

    private Configuration buildConfiguration(ReloadingFileBasedConfigurationBuilder<?> builder, String configurationFileName, long reloadPeriod, TimeUnit unit) {
        Parameters params = new Parameters();
        URL url = getFullUrl(configurationFileName);
        builder.configure(params.fileBased().setURL(url));
        if (reloadPeriod > 0L) {
            PeriodicReloadingTrigger trigger = new PeriodicReloadingTrigger(builder.getReloadingController(), null, reloadPeriod, unit);
            trigger.start();
        }
        try {
            return builder.getConfiguration();
        } catch (ConfigurationException e) {
            throw new ErrorException(e.getMessage());
        }
    }

}
