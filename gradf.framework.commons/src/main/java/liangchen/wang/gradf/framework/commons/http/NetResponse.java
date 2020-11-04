package liangchen.wang.gradf.framework.commons.http;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author LiangChen.Wang 2020/10/20
 */
public interface NetResponse {
    void onResponse(InputStream inputStream);

    void onFailure(Exception e);
}