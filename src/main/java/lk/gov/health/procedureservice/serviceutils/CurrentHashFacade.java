/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.gov.health.procedureservice.serviceutils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lk.gov.health.procedureroomservice.CurrentHash;
import lk.gov.health.procedureroomservice.service.AbstractFacade;

/**
 *
 * @author user
 */
public class CurrentHashFacade extends AbstractFacade<CurrentHash>{
    @PersistenceContext(unitName = "hmisPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CurrentHashFacade() {
        super(CurrentHash.class);
    }
}
