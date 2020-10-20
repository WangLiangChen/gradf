package liangchen.wang.gradf.framework.commons.http;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author LiangChen.Wang 2020/10/20
 */
public interface HttpResponse {
    void onResponse(InputStream inputStream) throws IOException;

    void onFailure(Exception e);
}