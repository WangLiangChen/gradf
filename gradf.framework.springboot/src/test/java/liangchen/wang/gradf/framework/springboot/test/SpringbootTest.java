package liangchen.wang.gradf.framework.springboot.test;

import liangchen.wang.gradf.framework.commons.utils.ConfigurationUtil;
import org.apache.commons.configuration2.Configuration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

/**
 * @author LiangChen.Wang 2020/9/16
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootTest {
    @Test
    public void testInit() {

    }

    @Test
    public void testConfig() {
        Configuration configuration = ConfigurationUtil.INSTANCE.getConfiguration("datasource.properties");
        Iterator<String> keys = configuration.getKeys();
        Map<String, Properties> datasourceMap = new HashMap<>();
        keys.forEachRemaining(key -> {
            String[] array = key.split("\\.");
            Properties properties = datasourceMap.get(array[0]);
            if(null==properties){
                properties = new Properties();
                datasourceMap.put(array[0],properties);
            }
            properties.setProperty(array[1],configuration.getString(key));
        });



        System.out.println(datasourceMap);
    }
}
