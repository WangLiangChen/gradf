package liangchen.wang.gradf.framework.data.utils;

import liangchen.wang.gradf.framework.commons.exception.ErrorException;
import liangchen.wang.gradf.framework.data.transaction.AfterCommitExecutorImpl;
import liangchen.wang.gradf.framework.data.transaction.IAfterCommitExecutor;
import liangchen.wang.gradf.framework.springboot.context.BeanLoader;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.function.Supplier;

/**
 * @author LiangChen.Wang
 */
public enum TransactionUtil {
    /**
     *
     */
    INSTANCE;
    private final PlatformTransactionManager transactionManager = BeanLoader.INSTANCE.getBean(PlatformTransactionManager.class);
    private final IAfterCommitExecutor afterCommitExecutor = new AfterCommitExecutorImpl();

    public void after(Runnable runnable) {
        afterCommitExecutor.execute(runnable);
    }

    public <T> T execute(Supplier<T> supplier) {
        return execute(supplier, Propagation.REQUIRED, Isolation.DEFAULT);
    }

    public <T> T execute(Supplier<T> supplier, Propagation propagation) {
        return execute(supplier, propagation, Isolation.DEFAULT);
    }

    public <T> T execute(Supplier<T> supplier, Isolation isolationLevel) {
        return execute(supplier, Propagation.REQUIRED, isolationLevel);
    }

    public <T> T execute(Supplier<T> supplier, Propagation propagation, Isolation isolationLevel) {
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setPropagationBehavior(propagation.value());
        transactionDefinition.setIsolationLevel(isolationLevel.value());
        TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);
        try {
            T t = supplier.get();
            transactionManager.commit(transactionStatus);
            return t;
        } catch (Exception e) {
            transactionManager.rollback(transactionStatus);
            throw new ErrorException(e);
        }
    }

    public void execute(Runnable runnable) {
        execute(runnable, Propagation.REQUIRED, Isolation.DEFAULT);
    }

    public void execute(Runnable runnable, Propagation propagation) {
        execute(runnable, propagation, Isolation.DEFAULT);
    }

    public void execute(Runnable runnable, Isolation isolationLevel) {
        execute(runnable, Propagation.REQUIRED, isolationLevel);
    }

    public void execute(Runnable runnable, Propagation propagation, Isolation isolationLevel) {
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setPropagationBehavior(propagation.value());
        transactionDefinition.setIsolationLevel(isolationLevel.value());
        TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);
        try {
            runnable.run();
            transactionManager.commit(transactionStatus);
        } catch (Exception e) {
            transactionManager.rollback(transactionStatus);
            throw e;
        }
    }
}
