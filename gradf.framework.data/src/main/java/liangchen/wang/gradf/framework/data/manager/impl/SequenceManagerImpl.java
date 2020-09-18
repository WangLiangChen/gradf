package liangchen.wang.gradf.framework.data.manager.impl;

import liangchen.wang.gradf.framework.data.dao.ISequenceDao;
import liangchen.wang.gradf.framework.data.manager.ISequenceManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

@Component("Crdf_Data_SequenceManager")
@ConditionalOnBean(ISequenceDao.class)
public class SequenceManagerImpl implements ISequenceManager {
    private final ISequenceDao dao;

    @Inject
    public SequenceManagerImpl(@Named("Crdf_Data_SequenceDao") ISequenceDao dao) {
        this.dao = dao;
    }

    @Override
    @Transactional
    public Long sequenceNumber(String sequenceKey, int initialValue) {
        return dao.sequenceNumber(sequenceKey, initialValue);
    }

    @Override
    @Transactional
    public Long sequenceNumber(String sequenceKey) {
        return dao.sequenceNumber(sequenceKey, 0);
    }
}
