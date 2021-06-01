package liangchen.wang.gradf.framework.data.test;

import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * @author LiangChen.Wang 2021/5/31
 */
@Component
public class SwitchServiceImpl implements ISwitchService {

    @Override
    //@Transactional
    public void testTransactional() {
        System.out.println("TransactionSynchronizationManager.isSynchronizationActive():" + TransactionSynchronizationManager.isSynchronizationActive());
        System.out.println("TransactionSynchronizationManager.isActualTransactionActive():" + TransactionSynchronizationManager.isActualTransactionActive());
    }

}
