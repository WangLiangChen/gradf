package liangchen.wang.gradf.component.foura.utils;

import liangchen.wang.gradf.framework.commons.json.JsonUtil;
import liangchen.wang.gradf.framework.commons.utils.ContextUtil;

/**
 * @author LiangChen.Wang
 */
public enum OperationLogUtil {
    // 
    INSTANCE;

    public OperationLogUtil putBusinessId(Long businessId) {
        this.putParameter("BusinessId", businessId);
        return OperationLogUtil.INSTANCE;
    }

    public Long getBusinessId() {
        return this.getParameter("BusinessId");
    }

    private void putParameter(String key, Object value) {
        key = String.format("OperationLog_%s", key);
        ContextUtil.INSTANCE.put(key, value);
    }

    private <T> T getParameter(String key) {
        key = String.format("OperationLog_%s", key);
        return ContextUtil.INSTANCE.get(key);
    }

    public OperationLogUtil putData(Object originalData, Object newData) {
        this.putParameter("OriginalData", originalData);
        this.putParameter("NewData", newData);
        return OperationLogUtil.INSTANCE;
    }

    public String getOriginalData() {
        Object originalData = this.getParameter("OriginalData");
        if (null == originalData) {
            return null;
        }
        return JsonUtil.INSTANCE.toJsonString(originalData);
    }

    public String getNewData() {
        Object newData = this.getParameter("NewData");
        if (null == newData) {
            return null;
        }
        return JsonUtil.INSTANCE.toJsonString(newData);
    }
}
