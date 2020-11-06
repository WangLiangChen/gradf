package liangchen.wang.gradf.framework.commons.http;

/**
 * @author LiangChen.Wang 2020/10/20
 */
public interface NetResponse<T> {
    void onResponse(T t);

    void onFailure(Exception e);
}