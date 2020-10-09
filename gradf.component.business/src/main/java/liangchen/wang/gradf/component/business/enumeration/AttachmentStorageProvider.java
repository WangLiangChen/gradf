package liangchen.wang.gradf.component.business.enumeration;

/**
 * @author LiangChen.Wang 2019/9/17 18:54
 */
public enum AttachmentStorageProvider {
    //
    local("本地存储"),intranet("内网存储"),aliyun("阿里云OSS"),unionpay("银联");

    private final String text;

    AttachmentStorageProvider(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
