package com.nncompany.impl.sevice;

import com.nncompany.api.interfaces.services.IBriefingService;
import com.nncompany.api.interfaces.stors.IBriefingStore;
import com.nncompany.api.model.entities.Briefing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BriefingService implements IBriefingService {

    @Autowired
    private IBriefingStore briefingStore;

    @Override
    public Briefing get(int id) {
        return briefingStore.get(id);
    }

    @Override
    public List<Briefing> getAll() {
        return briefingStore.getAll();
    }

    @Override
    public List<Briefing> getWithPagination(Integer offset, Integer limit) {
        return briefingStore.getWithPagination(offset, limit);
    }

    @Override
    public void save(Briefing briefing) {
        briefingStore.save(briefing);
    }

    @Override
    public void update(Briefing briefing) {
        briefingStore.update(briefing);
    }

    @Override
    public void delete(Briefing briefing) {
        briefingStore.delete(briefing);
    }
}
