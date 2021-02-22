/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.gov.health.procedureroomservice.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import lk.gov.health.procedureroomservice.ProcedureType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author user
 */
@Stateless
@Path("lk.gov.health.procedureroomservice.proceduretype")
public class ProcedureTypeFacadeREST extends AbstractFacade<ProcedureType> {

    @PersistenceContext(unitName = "hmisPU")
    private EntityManager em;

    public ProcedureTypeFacadeREST() {
        super(ProcedureType.class);
    }
    
    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(ProcedureType entity) {
        entity.setId(null);        
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, ProcedureType entity) {
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
    public String getAll() {
        JSONArray ja_ = new JSONArray();

        List<ProcedureType> procTypeList;
        procTypeList = super.findAll();

        for (ProcedureType procType : procTypeList) {
            ja_.add(getJSONObject(procType));
        }
        return ja_.toString(); 
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public String findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        JSONArray ja_ = new JSONArray();

        List<ProcedureType> procTypeList;
        procTypeList = super.findRange(new int[]{from, to});

        for (ProcedureType procType : procTypeList) {
            ja_.add(getJSONObject(procType));
        }
        return ja_.toString();
    }

    @GET
    @Path("count")
    @Produces(MediaType.APPLICATION_JSON)
    public String countREST() {
        JSONObject jo_ = new JSONObject();
        jo_.put("count", String.valueOf(super.count()));

        return jo_.toString();
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    private JSONObject getJSONObject(ProcedureType procType) {
        JSONObject jo_ = new JSONObject();

        jo_.put("id", procType.getId());
        jo_.put("procedureType", procType.getProcedureType());
        jo_.put("description", procType.getDescription());

        return jo_;
    }
    
    @GET
    @Path("/filer_list/{searchVal}")
    @Produces(MediaType.APPLICATION_JSON)
    public String findFilteredList(@PathParam("searchVal") String searchVal) {        
        JSONArray ja_ = new JSONArray();
        
        String jpql;
        Map m = new HashMap();
        jpql = "SELECT pt FROM ProcedureType pt WHERE upper(pt.procedureType) like :searchVal";
        
        m.put("searchVal", "%" + searchVal.toUpperCase() + "%");
        
        List<ProcedureType> procTypeList = super.findByJpql(jpql, m);
        
        for (ProcedureType procType : procTypeList) {
            ja_.add(getJSONObject(procType));
        }
        return ja_.toString();
    }
}
