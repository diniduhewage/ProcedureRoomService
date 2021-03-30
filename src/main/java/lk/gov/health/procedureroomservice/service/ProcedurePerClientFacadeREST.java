/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.gov.health.procedureroomservice.service;

import java.text.SimpleDateFormat;
import java.util.Date;
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
import lk.gov.health.procedureroomservice.MedProcedure;
import lk.gov.health.procedureroomservice.ProcedurePerClient;
import lk.gov.health.procedureroomservice.ProcedureRoom;
import lk.gov.health.procedureroomservice.ProcedureRoomType;
import lk.gov.health.procedureroomservice.ProcedureType;
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
        entity.setId(null);
        entity.setCreatedAt(new Date());
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
        jo_.put("procedureId", getProcedureJSONObject(procPerClient.getProcedureId()));
        jo_.put("roomId", getRoomJSONObject(procPerClient.getRoomId()));
        jo_.put("createdBy", procPerClient.getCreatedBy());
        jo_.put("createdAt", new SimpleDateFormat("yyyy-MM-dd").format(procPerClient.getCreatedAt()));
        jo_.put("status", procPerClient.getStatus().toString());
        
        return jo_;
    }

    private JSONObject getProcedureJSONObject(MedProcedure proc) {
        JSONObject jo_ = new JSONObject();

        jo_.put("id", proc.getId());
        jo_.put("procId", proc.getProcId());
        jo_.put("description", proc.getDescription());
        jo_.put("procType", getProcTypeObject(proc.getProcType()));
        jo_.put("roomType", getRoomTypeObjct(proc.getRoomType()));
        jo_.put("comment", proc.getComment());
        jo_.put("status", proc.getStatus().toString());

        return jo_;
    }

    public JSONObject getRoomTypeObjct(ProcedureRoomType obj) {
        JSONObject tempObj = new JSONObject();
        tempObj.put("id", obj.getId());
        tempObj.put("typeId", obj.getTypeId());
        tempObj.put("description", obj.getDescription());

        return tempObj;
    }

    public JSONObject getProcTypeObject(ProcedureType obj) {
        JSONObject tempObj = new JSONObject();
        tempObj.put("id", obj.getId());
        tempObj.put("procedureType", obj.getProcedureType());
        tempObj.put("description", obj.getDescription());

        return tempObj;
    }

    private JSONObject getRoomJSONObject(ProcedureRoom procRoom) {
        JSONObject jo_ = new JSONObject();

        jo_.put("id", procRoom.getId());
        jo_.put("roomId", procRoom.getRoomId());
        jo_.put("description", procRoom.getDescription());
        jo_.put("type", getRoomTypeObjct(procRoom.getType()));
        jo_.put("instituteId", procRoom.getInstituteId());
        jo_.put("status", procRoom.getStatus().toString());

        return jo_;
    }

}
