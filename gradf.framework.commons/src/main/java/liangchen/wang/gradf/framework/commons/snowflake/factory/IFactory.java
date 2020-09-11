package liangchen.wang.gradf.framework.commons.snowflake.factory;


import liangchen.wang.crdf.framework.commons.snowflake.generator.IGenerator;
import liangchen.wang.crdf.framework.commons.snowflake.worker.IWorkerIdAssigner;

public interface IFactory {
	IGenerator create(IWorkerIdAssigner workerIdAssigner);

	IGenerator create(Long workerId);
}
