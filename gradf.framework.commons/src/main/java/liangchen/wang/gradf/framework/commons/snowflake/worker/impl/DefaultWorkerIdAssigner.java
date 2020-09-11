package liangchen.wang.gradf.framework.commons.snowflake.worker.impl;

import liangchen.wang.crdf.framework.commons.snowflake.worker.IWorkerIdAssigner;

public class DefaultWorkerIdAssigner implements IWorkerIdAssigner {
	private Long worker_id;
	
	public DefaultWorkerIdAssigner(Long worker_id) {
		this.worker_id = worker_id;
	}

	@Override
	public Long workerId() {
		return this.worker_id;
	}
		
}
