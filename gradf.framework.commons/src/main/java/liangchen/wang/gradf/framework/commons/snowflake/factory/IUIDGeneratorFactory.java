package liangchen.wang.gradf.framework.commons.snowflake.factory;


import liangchen.wang.gradf.framework.commons.snowflake.generator.IUIDGenerator;
import liangchen.wang.gradf.framework.commons.snowflake.worker.IWorkerIdBuilder;

/**
 * @author LiangChen.Wang
 */
public interface IUIDGeneratorFactory {
    IUIDGenerator create(IWorkerIdBuilder workerIdAssigner);

    IUIDGenerator create(Long workerId);
}
