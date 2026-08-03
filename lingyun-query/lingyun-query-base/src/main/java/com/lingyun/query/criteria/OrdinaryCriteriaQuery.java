package com.lingyun.query.criteria;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.lingyun.query.condition.QueryCondition;

public class OrdinaryCriteriaQuery<T extends Serializable> implements CriteriaQuery<T> {

    private T raw;
    private Collection<QueryCondition> conditions;

    public OrdinaryCriteriaQuery(T raw) {
        this.raw = raw;
    }

    @Override
    public T getRaw() {
        return raw;
    }

    @Override
    public Collection<QueryCondition> getConditions() {
        if (conditions == null) {
            return Collections.emptyList();
        }
        
        return List.copyOf(conditions);
    }

    @Override
    public void setConditions(Collection<? extends QueryCondition> conditions) {
        this.conditions = new ArrayList<>(conditions);
    }

}
