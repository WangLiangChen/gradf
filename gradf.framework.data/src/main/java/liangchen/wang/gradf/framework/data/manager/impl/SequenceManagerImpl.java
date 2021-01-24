package liangchen.wang.gradf.framework.data.manager.impl;

import liangchen.wang.gradf.framework.data.dao.ISequenceDao;
import liangchen.wang.gradf.framework.data.manager.ISequenceManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

@Component("Gradf_Data_SequenceManager")
@ConditionalOnBean(ISequenceDao.class)
public class SequenceManagerImpl implements ISequenceManager {
    private final ISequenceDao dao;

    @Inject
    public SequenceManagerImpl(@Named("Gradf_Data_SequenceDao") ISequenceDao dao) {
        this.dao = dao;
    }

    @Override
    public Long sequenceNumber(String sequenceKey, long initialValue) {
        return dao.sequenceNumber(sequenceKey, initialValue);
    }

    @Override
    public Long sequenceNumber(String sequenceKey) {
        return dao.sequenceNumber(sequenceKey, 0);
    }
}
