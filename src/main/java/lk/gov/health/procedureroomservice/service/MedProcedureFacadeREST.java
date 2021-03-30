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
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(MedProcedure entity) {
        entity.setId(null);
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, MedProcedure entity) {
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
        List<MedProcedure> medProcedureList = super.findAll();

        for (MedProcedure mp_ : medProcedureList) {
            array.add(getJSONObject(mp_));
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

    private JSONObject getJSONObject(MedProcedure mp_) {
        JSONObject jo_ = new JSONObject();
        jo_.put("id", mp_.getId());
        jo_.put("comment", mp_.getComment());
        jo_.put("procId", mp_.getProcId());
        jo_.put("description", mp_.getDescription());
        jo_.put("procType", getProcTypeObjct(mp_.getProcType()));
        jo_.put("status", mp_.getStatus().toString());

        return jo_;
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

    @GET
    @Path("/filer_list/{searchVal}")
    @Produces(MediaType.APPLICATION_JSON)
    public String findFilteredList(@PathParam("searchVal") String searchVal) {
        JSONArray ja_ = new JSONArray();

        String jpql;
        Map m = new HashMap();
        jpql = "SELECT pr FROM MedProcedure pr WHERE upper(pr.procId) like :searchVal";

        m.put("searchVal", "%" + searchVal.toUpperCase() + "%");

        List<MedProcedure> medProcedureList = super.findByJpql(jpql, m);

        for (MedProcedure mp_ : medProcedureList) {
            ja_.add(getJSONObject(mp_));
        }
        return ja_.toString();
    }
}
