package liangchen.wang.gradf.framework.commons.http;

/**
 * @author LiangChen.Wang
 */
public class ResponseHeaders {
    private long contentLength;
    private String contentType;

    public static ResponseHeaders newInstance() {
        return new ResponseHeaders();
    }

    public static ResponseHeaders newInstance(long contentLength, String contentType) {
        ResponseHeaders responseHeaders = new ResponseHeaders();
        responseHeaders.contentLength = contentLength;
        responseHeaders.contentType = contentType;
        return responseHeaders;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
