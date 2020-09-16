package liangchen.wang.gradf.framework.data.transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

@Component("CrdfAfterCommitExecutor")
public class AfterCommitExecutorImpl extends TransactionSynchronizationAdapter implements AfterCommitExecutor {
    private static final Logger logger = LoggerFactory.getLogger(AfterCommitExecutorImpl.class);
    private static final ThreadLocal<List<Runnable>> runnables = new ThreadLocal<>();
    @Inject
    private Executor executor;

    @Override
    public void execute(Runnable runnable) {
        logger.debug("Submitting new runnable {} to run after commit", runnable);
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            logger.debug("Transaction synchronization is NOT ACTIVE. Executing right now runnable {}", runnable);
            executor.execute(runnable);
            return;
        }
        List<Runnable> threadRunnables = runnables.get();
        if (threadRunnables == null) {
            threadRunnables = new ArrayList<Runnable>();
            runnables.set(threadRunnables);
            //这个语句用来注册事务监听
            TransactionSynchronizationManager.registerSynchronization(this);
        }
        threadRunnables.add(runnable);
    }

    @Override
    public void afterCommit() {
        List<Runnable> threadRunnables = runnables.get();
        logger.debug("Transaction successfully committed, executing {} runnables", threadRunnables.size());
        threadRunnables.forEach(runnable->{
            logger.debug("Executing runnable {}", runnable);
            try {
                executor.execute(runnable);
            } catch (RuntimeException e) {
                logger.error("Failed to execute runnable " + runnable, e);
            }
        });
    }

    @Override
    public void afterCompletion(int status) {
        logger.debug("Transaction completed with status {}", status == STATUS_COMMITTED ? "COMMITTED" : "ROLLED_BACK");
        runnables.remove();
    }
}
