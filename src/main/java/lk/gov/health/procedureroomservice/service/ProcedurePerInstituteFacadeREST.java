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
import lk.gov.health.procedureroomservice.Institute;
import lk.gov.health.procedureroomservice.MedProcedure;
import lk.gov.health.procedureroomservice.ProcedurePerInstitute;
import lk.gov.health.procedureroomservice.ProcedureRoomType;
import lk.gov.health.procedureroomservice.ProcedureType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author user
 */
@Stateless
@Path("lk.gov.health.procedureroomservice.procedureperinstitute")
public class ProcedurePerInstituteFacadeREST extends AbstractFacade<ProcedurePerInstitute> {

    @PersistenceContext(unitName = "hmisPU")
    private EntityManager em;

    public ProcedurePerInstituteFacadeREST() {
        super(ProcedurePerInstitute.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(ProcedurePerInstitute entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, ProcedurePerInstitute entity) {
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
    public ProcedurePerInstitute find(@PathParam("id") Long id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<ProcedurePerInstitute> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<ProcedurePerInstitute> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
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
    
    @GET
    @Path("/filer_list/{instVal}/{searchVal}")
    @Produces(MediaType.APPLICATION_JSON)
    public String findFilteredList(
            @PathParam("searchVal") String searchVal,
            @PathParam("instVal") String instVal) {        
        JSONArray ja_ = new JSONArray();
        
        String jpql;
        Map m = new HashMap();
        jpql = "SELECT pi FROM ProcedurePerInstitute pi WHERE pi.institute = :instVal AND upper(pi.procedure) like :searchVal";
        
        m.put("searchVal", "%" + searchVal.toUpperCase() + "%");
        m.put("instVal", instVal);
        
        List<ProcedurePerInstitute> procPerInstList = super.findByJpql(jpql, m);        
        for (ProcedurePerInstitute proc : procPerInstList) {
            ja_.add(getJSONObject(proc));
        }
        return ja_.toString();
    }
    
    private JSONObject getJSONObject(ProcedurePerInstitute proc) {
        JSONObject jo_ = new JSONObject();

        jo_.put("id", proc.getId());
        jo_.put("procedure", this.getMedProcJSONObject(proc.getProcedure()));
        jo_.put("institute", this.getInstitute(proc.getInstituteId()));

        return jo_;
    }
    
    public JSONObject getInstitute(Institute obj){    
        JSONObject tempObj = new JSONObject();
        tempObj.put("id", obj.getId());
        tempObj.put("code", obj.getCode());
        tempObj.put("institute_type_db", obj.getIntituteTypeDb());
        tempObj.put("institute_type", obj.getIntituteType());
        tempObj.put("hin", obj.getHin());
        tempObj.put("address", obj.getAddress());
        tempObj.put("provinceId", obj.getProvinceId());
        tempObj.put("districtId", obj.getDistrictId());
        
        return tempObj;
    }     
    
    private JSONObject getMedProcJSONObject(MedProcedure mp_) {
        JSONObject jo_ = new JSONObject();

            jo_.put("id", mp_.getId());
            jo_.put("comment", mp_.getComment());
            jo_.put("procId", mp_.getProcId());
            jo_.put("description", mp_.getDescription());
            jo_.put("procType", getProcTypeObjct(mp_.getProcType()));

        return jo_;
    }    
            
    public JSONObject getRoomTypeObjct(ProcedureRoomType obj){    
        JSONObject tempObj = new JSONObject();
        tempObj.put("id", obj.getId());
        tempObj.put("typeId", obj.getTypeId());
        tempObj.put("description", obj.getDescription());
        
        return tempObj;
    }
    
    public JSONObject getProcTypeObjct(ProcedureType obj){    
        JSONObject tempObj = new JSONObject();
        tempObj.put("id", obj.getId());
        tempObj.put("procedureType", obj.getProcedureType());
        tempObj.put("description", obj.getDescription());
        
        return tempObj;
    }   
    
    @GET
    @Path("/procedure_list/{instVal}")
    @Produces(MediaType.APPLICATION_JSON)
    public String findFilteredList(@PathParam("instVal") String instVal){
        JSONArray ja_ = new JSONArray();
        
        String jpql;
        Map m = new HashMap();
        jpql = "SELECT pi FROM ProcedurePerInstitute pi WHERE pi.institute = :instVal";
        
        m.put("instVal", instVal);
        
        List<ProcedurePerInstitute> procPerInstList = super.findByJpql(jpql, m);        
        procPerInstList.forEach(proc -> {
            ja_.add(getJSONObject(proc));
        });
        return ja_.toString();
    }
}
