package liangchen.wang.gradf.framework.web.result;

/**
 * @author LiangChen.Wang
 */
public class Response {
    protected boolean success;
    /**
     * 1、正常返回的对象、数组
     * 2、异常返回的错误信息
     */
    protected Object result;

    public Response(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
