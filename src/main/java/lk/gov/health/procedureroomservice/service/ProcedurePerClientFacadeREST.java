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
import lk.gov.health.procedureroomservice.ProcedurePerClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author user
 */
@Stateless
@Path("lk.gov.health.procedureroomservice.procedureperclient")
public class ProcedurePerClientFacadeREST extends AbstractFacade<ProcedurePerClient> {

    @PersistenceContext(unitName = "hmisPU")
    private EntityManager em;

    public ProcedurePerClientFacadeREST() {
        super(ProcedurePerClient.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(ProcedurePerClient entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, ProcedurePerClient entity) {
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

        List<ProcedurePerClient> procPerClientList;
        procPerClientList = super.findAll();

        for (ProcedurePerClient procPerClient : procPerClientList) {
            ja_.add(getJSONObject(procPerClient));
        }
        return ja_.toString(); 
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public String findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        JSONArray ja_ = new JSONArray();

        List<ProcedurePerClient> procPerClientList;
        procPerClientList = super.findRange(new int[]{from, to});

        for (ProcedurePerClient procPerClient : procPerClientList) {
            ja_.add(getJSONObject(procPerClient));
        }
        return ja_.toString();
    }

    @GET
    @Path("count")
    @Produces(MediaType.APPLICATION_JSON)
    public String countREST() {
        JSONObject jo = new JSONObject();
        jo.put("count", String.valueOf(super.count()));
        return jo.toJSONString();
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    private JSONObject getJSONObject(ProcedurePerClient procPerClient) {
        JSONObject jo_ = new JSONObject();

        jo_.put("id", procPerClient.getId());
        jo_.put("phn", procPerClient.getPhn());
        jo_.put("instituteId", procPerClient.getInstituteId());
        jo_.put("procedureId", procPerClient.getProcedureId());
        jo_.put("roomId", procPerClient.getRoomId());
        jo_.put("createdBy", procPerClient.getCreatedBy());
        jo_.put("createdAt", procPerClient.getCreatedAt());
        jo_.put("status", procPerClient.getStatus());

        return jo_;
    }
    
}
