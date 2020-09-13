package liangchen.wang.gradf.framework.commons.snowflake.worker.impl;


import liangchen.wang.gradf.framework.commons.snowflake.worker.IWorkerIdBuilder;

/**
 * @author LiangChen.Wang
 */
public class DefaultWorkerIdBuilder implements IWorkerIdBuilder {
    private final Long worker_id;

    public DefaultWorkerIdBuilder(Long worker_id) {
        this.worker_id = worker_id;
    }

    @Override
    public Long workerId() {
        return this.worker_id;
    }

}
