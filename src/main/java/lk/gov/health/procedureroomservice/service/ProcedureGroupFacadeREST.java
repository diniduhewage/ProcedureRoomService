/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.gov.health.procedureroomservice.service;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import lk.gov.health.procedureroomservice.ProcedureGroup;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author user
 */
@Stateless
@Path("lk.gov.health.procedureroomservice.proceduregroup")
public class ProcedureGroupFacadeREST extends AbstractFacade<ProcedureGroup> {

    @PersistenceContext(unitName = "hmisPU")
    private EntityManager em;

    public ProcedureGroupFacadeREST() {
        super(ProcedureGroup.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(ProcedureGroup entity) {
        entity.setId(null);  
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, ProcedureGroup entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public String find(@PathParam("id") Long id) {
        return getJSONObject(super.find(id)).toString();
    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public String getdAll() {
        JSONArray ja_ = new JSONArray();

        List<ProcedureGroup> procGroupList;
        procGroupList = super.findAll();

        for (ProcedureGroup procType : procGroupList) {
            ja_.add(getJSONObject(procType));
        }
        return ja_.toString(); 
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public String findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        JSONArray ja_ = new JSONArray();

        List<ProcedureGroup> procGroupList;
        procGroupList = super.findRange(new int[]{from, to});

        for (ProcedureGroup procGroup : procGroupList) {
            ja_.add(getJSONObject(procGroup));
        }
        return ja_.toString();
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    private JSONObject getJSONObject(ProcedureGroup procGroup) {
        JSONObject jo_ = new JSONObject();

        jo_.put("id", procGroup.getId());
        jo_.put("procGroup", procGroup.getProcGroup());
        jo_.put("description", procGroup.getDescription());

        return jo_;
    }    
}
