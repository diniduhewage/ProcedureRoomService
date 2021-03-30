/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.gov.health.procedureroomservice.service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;

/**
 *
 * @author user
 */
public abstract class AbstractFacade<T> {

    private Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    public void edit(T entity) {
        getEntityManager().merge(entity);
    }

    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public List<T> findByJpql(String jpql, Map<String, Object> parameters) {
        TypedQuery<T> qry = getEntityManager().createQuery(jpql, entityClass);
        Set s = parameters.entrySet();
        Iterator it = s.iterator();
        while (it.hasNext()) {
            Map.Entry m = (Map.Entry) it.next();
            String pPara = (String) m.getKey();
            if (m.getValue() instanceof Date) {
                Date pVal = (Date) m.getValue();
                qry.setParameter(pPara, pVal, TemporalType.DATE);
            } else {
                Object pVal = (Object) m.getValue();
                qry.setParameter(pPara, pVal);
            }
        }
        return qry.getResultList();
    }  
    
    public T findByField(String fieldName, String fieldValue) {
        List<T> lstAll = findExact(fieldName, fieldValue);
        if (lstAll.isEmpty()) {
            return null;
        } else {
            return lstAll.get(0);
        }
    }
    
    public List<T> findExact(String fieldName, String fieldValue) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<T> cq = cb.createQuery(entityClass);
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        ParameterExpression<String> p = cb.parameter(String.class);
//        Predicate predicateField = cb.like(rt.<String>get(fieldName), fieldValue);
        Predicate predicateField = cb.equal(cb.upper(rt.<String>get(fieldName)), fieldValue.toLowerCase());
//        Predicate predicateRetired = cb.equal(rt.<Boolean>get("retired"), false);
//        Predicate predicateFieldRetired = cb.and(predicateField, predicateRetired);

//        if (withoutRetired && !fieldValue.equals("")) {
//            cq.where(predicateFieldRetired);
//        } else if (withoutRetired) {
//            cq.where(predicateRetired);
//        } else if (!fieldValue.equals("")) {
//            cq.where(predicateField);
//        }
//        
        if (!fieldValue.equals("")) {
            cq.where(predicateField);
        }

        if (!fieldName.equals("")) {
            cq.orderBy(cb.asc(rt.get(fieldName)));
        }

        return getEntityManager().createQuery(cq).getResultList();
    }
}
