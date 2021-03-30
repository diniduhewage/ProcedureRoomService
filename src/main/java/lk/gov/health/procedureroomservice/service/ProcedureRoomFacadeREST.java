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
import lk.gov.health.procedureroomservice.ProcedureRoom;
import lk.gov.health.procedureroomservice.ProcedureRoomType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author user
 */
@Stateless
@Path("lk.gov.health.procedureroomservice.procedureroom")
public class ProcedureRoomFacadeREST extends AbstractFacade<ProcedureRoom> {

    @PersistenceContext(unitName = "hmisPU")
    private EntityManager em;

    public ProcedureRoomFacadeREST() {
        super(ProcedureRoom.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(ProcedureRoom entity) {
        entity.setId(null);
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, ProcedureRoom entity) {
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

        List<ProcedureRoom> procRoomList;
        procRoomList = super.findAll();

        for (ProcedureRoom procRoom : procRoomList) {
            ja_.add(getJSONObject(procRoom));
        }
        return JSONArray.toJSONString(ja_); 
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public String findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        JSONArray ja_ = new JSONArray();

        List<ProcedureRoom> procRoomList;
        procRoomList = super.findRange(new int[]{from, to});

        for (ProcedureRoom procRoom : procRoomList) {
            ja_.add(getJSONObject(procRoom));
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
    
    @GET
    @Path("/filer_list/{searchVal}")
    @Produces(MediaType.APPLICATION_JSON)
    public String findFilteredList(@PathParam("searchVal") String searchVal) {        
        JSONArray ja_ = new JSONArray();
        
        String jpql;
        Map m = new HashMap();
        jpql = "SELECT pr FROM ProcedureRoom pr WHERE upper(pr.roomId) like :searchVal";
        
        m.put("searchVal", "%" + searchVal.toUpperCase() + "%");
        
        List<ProcedureRoom> procRoomList = super.findByJpql(jpql, m);
        
        for (ProcedureRoom procRoom : procRoomList) {
            ja_.add(getJSONObject(procRoom));
        }
        return ja_.toString();
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    private JSONObject getJSONObject(ProcedureRoom procRoom) {
        JSONObject jo_ = new JSONObject();

        jo_.put("id", procRoom.getId());
        jo_.put("roomId", procRoom.getRoomId());
        jo_.put("description", procRoom.getDescription());
        jo_.put("type", getRoomTypeObjct(procRoom.getType()));
        jo_.put("instituteId", procRoom.getInstituteId());
        jo_.put("status", procRoom.getStatus().toString());

        return jo_;
    }
    
    public JSONObject getRoomTypeObjct(ProcedureRoomType obj){    
        JSONObject tempObj = new JSONObject();
        tempObj.put("id", obj.getId());
        tempObj.put("typeId", obj.getTypeId());
        tempObj.put("description", obj.getDescription());
        
        return tempObj;
    }
}
