package com.nncompany.impl.store;

import com.nncompany.api.interfaces.stors.IBriefingStore;
import com.nncompany.api.model.entities.Briefing;
import org.springframework.stereotype.Repository;

@Repository
public class BriefingStore extends AbstractDao<Briefing> implements IBriefingStore {

    public BriefingStore(){
        super(Briefing.class);
    }

}
