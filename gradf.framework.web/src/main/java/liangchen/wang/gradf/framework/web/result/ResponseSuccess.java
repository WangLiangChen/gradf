package liangchen.wang.gradf.framework.web.result;

/**
 * @author LiangChen.Wang
 */
public class ResponseSuccess extends Response {

    public ResponseSuccess() {
        super(true);
    }

    public ResponseSuccess(Object data) {
        this();
        super.setResult(data);
    }
}
