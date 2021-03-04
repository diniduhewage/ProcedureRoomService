/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.gov.health.procedureroomservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javax.ws.rs.core.Response;
import lk.gov.health.procedureroomservice.MedProcedure;
import lk.gov.health.procedureroomservice.ProcedureRoomType;
import lk.gov.health.procedureroomservice.ProcedureType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author user
 */
@Stateless
@Path("lk.gov.health.procedureroomservice.medprocedure")
public class MedProcedureFacadeREST extends AbstractFacade<MedProcedure> {

    @PersistenceContext(unitName = "hmisPU")
    private EntityManager em;

    public MedProcedureFacadeREST() {
        super(MedProcedure.class);
    }

    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response Create(MedProcedure entity) {
        entity.setId(null);
        if (!checkExists(entity.getProcId())) {
            super.create(entity);
            return Response.status(Response.Status.ACCEPTED).build();
        } else {
            return Response.status(Response.Status.CONFLICT).entity("Duplicate Entry..!Unable to create new record").build();
        }
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response edit(@PathParam("id") Long id, MedProcedure entity) {
        if ((entity.getProcId() == super.find(id).getProcId()) || (!checkExists(entity.getProcId()))) {
            super.edit(entity);
            return Response.status(Response.Status.OK).build();
        } else {
            return Response.status(Response.Status.CONFLICT).entity("Duplicate Entry..!Unable update new record").build();
        }
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
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(super.find(id));
        } catch (JsonProcessingException ex) {
            Logger.getLogger(ProcedureLogFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public String getAll() {
        JSONArray array = new JSONArray();
        List<MedProcedure> object = super.findAll();

        for (int i = 0; i < object.size(); i++) {
            JSONObject jo = new JSONObject();
            jo.put("id", object.get(i).getId());
            jo.put("comment", object.get(i).getComment());
            jo.put("procId", object.get(i).getProcId());
            jo.put("description", object.get(i).getDescription());
            if(object.get(i).getRoomType() != null)
                jo.put("roomType", getRoomTypeObjct(object.get(i).getRoomType()));
            else
                jo.put("roomType", null);
            
            if(object.get(i).getProcType() != null)
                jo.put("procType", getProcTypeObjct(object.get(i).getProcType()));
            else
                jo.put("procType", null);
            
            array.add(jo);
        }
        return JSONArray.toJSONString(array);
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public String findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return JSONArray.toJSONString(super.findRange(new int[]{from, to}));
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

    public JSONObject getRoomTypeObjct(ProcedureRoomType obj) {
        JSONObject tempObj = new JSONObject();
        tempObj.put("id", obj.getId());
        tempObj.put("typeId", obj.getTypeId());
        tempObj.put("description", obj.getDescription());

        return tempObj;
    }

    public JSONObject getProcTypeObjct(ProcedureType obj) {
        JSONObject tempObj = new JSONObject();
        tempObj.put("id", obj.getId());
        tempObj.put("procedureType", obj.getProcedureType());
        tempObj.put("description", obj.getDescription());

        return tempObj;
    }

    public boolean checkExists(String searchVal) {
        String jpql;
        Map m = new HashMap();
        jpql = "SELECT pr FROM MedProcedure pr WHERE upper(pr.procId) like :searchVal";

        m.put("searchVal", searchVal.toUpperCase());

        List<MedProcedure> medProcedureList = super.findByJpql(jpql, m);

        if (medProcedureList.size() > 0) {
            return true;
        }
        return false;
    }
}
