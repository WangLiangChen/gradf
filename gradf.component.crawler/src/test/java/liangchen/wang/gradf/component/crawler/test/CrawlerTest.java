package liangchen.wang.gradf.component.crawler.test;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Comparator;
import java.util.Optional;

/**
 * @author LiangChen.Wang 2020/9/16
 */
@SpringBootTest
public class CrawlerTest {


    @Test
    public void testJsoup() throws IOException {
        String url = "http://archive.routeviews.org/bgpdata/2001.10/UPDATES/";
        Optional<String> updates = routeFile(url, "updates");
        updates.ifPresent(System.out::println);
    }

    private Optional<String> routeFile(String url, String filePrefix) throws IOException {
        Connection connect = Jsoup.connect(url);
        Document document = connect.get();
        Elements as = document.select("body > table > tbody > tr > td > a");
        return as.stream().map(e -> e.attr("href")).filter(e -> e.startsWith(filePrefix)).sorted(Comparator.reverseOrder()).findFirst();
    }

}
