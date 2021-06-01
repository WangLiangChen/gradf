package liangchen.wang.gradf.framework.data.transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * @author LiangChen.Wang
 */
public class AfterCommitExecutor implements TransactionSynchronization, Executor {
    private static final Logger logger = LoggerFactory.getLogger(AfterCommitExecutor.class);
    private static final ThreadLocal<List<Runnable>> runnables = ThreadLocal.withInitial(() -> new ArrayList<>(10));
    private static final ThreadLocal<Boolean> registed = ThreadLocal.withInitial(() -> false);
    private final Executor executor;

    public AfterCommitExecutor(Executor executor) {
        this.executor = executor;
    }

    @Override
    public void execute(Runnable runnable) {
        logger.debug("Submitting new runnable {} to run after commit", runnable);
        // 如果事务同步不可用则事务已提交,立即执行
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            logger.debug("Transaction synchronization is not active. Executing right now runnable {}", runnable);
            executor.execute(runnable);
            return;
        }
        // 同一个事务的 在一起
        runnables.get().add(runnable);
        if (!registed.get()) {
            TransactionSynchronizationManager.registerSynchronization(this);
            registed.set(true);
        }
    }


    @Override
    public void afterCommit() {
        List<Runnable> threadRunnables = runnables.get();
        logger.debug("Transaction successfully committed, executing {} runnables", threadRunnables.size());
        threadRunnables.forEach(runnable -> {
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
        registed.remove();
    }
}
