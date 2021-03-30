/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.gov.health.procedureroomservice.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
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
import lk.gov.health.procedureroomservice.ClientProcedure;
import lk.gov.health.procedureroomservice.Institute;
import lk.gov.health.procedureroomservice.MedProcedure;
import lk.gov.health.procedureroomservice.ProcedurePerClient;
import lk.gov.health.procedureroomservice.ProcedurePerInstitute;
import lk.gov.health.procedureroomservice.ProcedureRoomType;
import lk.gov.health.procedureroomservice.ProcedureType;
import lk.gov.health.procedureservice.enums.ProcPerClientStates;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author user
 */
@Stateless
@Path("lk.gov.health.procedureroomservice.procedureperclient")
public class ProcedurePerClientFacadeREST extends AbstractFacade<ProcedurePerClient> {

    @EJB
    private InstituteFacadeREST instituteFacadeREST;
    @EJB
    private ProcedurePerInstituteFacadeREST ProcedurePerInstituteFacadeREST;
    @EJB
    private ProcedureRoomFacadeREST ProcedureRoomFacadeREST;

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

    @POST
    @Path("/register_procedure")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void register_procedure(ClientProcedure entity) {
        ProcedurePerClient entity_ = new ProcedurePerClient();

        entity_.setId(null);
        entity_.setPhn(entity.getPhn());
        entity_.setInstituteId(getInstituteObj(entity.getInstituteCode()));
        entity_.setProcedureId(getProcedurePerInstitueObj(entity.getProcedureCode()));
        entity_.setRoomId(getProcedureRoomObj(entity.getRoomId()));
        entity_.setCreatedBy(entity.getCreatedBy());
        entity_.setCreatedAt(entity.getCreatedAt());
        entity_.setStatus(ProcPerClientStates.CREATED);
        
        super.create(entity_);
    }

    private Institute getInstituteObj(String instituteCode) {
        HashMap<String, Object> m_ = new HashMap<>();

        String jpql_ = "SELECT ins FROM Institute ins WHERE ins.code=:val_";
        m_.put("code", instituteCode);

        return this.instituteFacadeREST.findByJpql(jpql_, m_).get(0);
    }
    
    private ProcedurePerInstitute getProcedurePerInstitueObj(String procPerInstitute) {
        HashMap<String, Object> m_ = new HashMap<>();

        String jpql_ = "SELECT pins FROM ProcedurePerInstitute pins WHERE pins.procedure=:val_";
        m_.put("procedure", procPerInstitute);

        return this.ProcedurePerInstituteFacadeREST.findByJpql(jpql_, m_).get(0);
    }
    
    private Institute getProcedureRoomObj(String procRoom) {
        HashMap<String, Object> m_ = new HashMap<>();

        String jpql_ = "SELECT pr FROM Institute pr WHERE pr.instituteType = :type_ AND pr.roomId=:val_";
        m_.put("type_", "Procedure_Room");
        m_.put("val_", procRoom);

        return this.instituteFacadeREST.findByJpql(jpql_, m_).get(0);
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
        jo_.put("instituteId", getInstitute(procPerClient.getInstituteId()));
        jo_.put("procedureId", getProPerInstJSONObject(procPerClient.getProcedureId()));
        jo_.put("roomId", getInstitute(procPerClient.getRoomId()));
        jo_.put("createdBy", procPerClient.getCreatedBy());
        jo_.put("createdAt", new SimpleDateFormat("yyyy-MM-dd").format(procPerClient.getCreatedAt()));
        jo_.put("status", procPerClient.getStatus().toString());

        return jo_;
    }

    private JSONObject getProPerInstJSONObject(ProcedurePerInstitute proc) {
        JSONObject jo_ = new JSONObject();

        jo_.put("id", proc.getId());
        jo_.put("procedure", this.getMedProcJSONObject(proc.getProcedure()));
        jo_.put("institute", this.getInstitute(proc.getInstituteId()));

        return jo_;
    }

    private JSONObject getMedProcJSONObject(MedProcedure proc) {
        JSONObject jo_ = new JSONObject();

        jo_.put("id", proc.getId());
        jo_.put("procId", proc.getProcId());
        jo_.put("description", proc.getDescription());
        jo_.put("procType", getProcTypeObject(proc.getProcType()));
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

    public JSONObject getInstitute(Institute obj) {
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

    public InstituteFacadeREST getInstituteFacadeREST() {
        return instituteFacadeREST;
    }

    public void setInstituteFacadeREST(InstituteFacadeREST instituteFacadeREST) {
        this.instituteFacadeREST = instituteFacadeREST;
    }

    public ProcedurePerInstituteFacadeREST getProcedurePerInstituteFacadeREST() {
        return ProcedurePerInstituteFacadeREST;
    }

    public void setProcedurePerInstituteFacadeREST(ProcedurePerInstituteFacadeREST ProcedurePerInstituteFacadeREST) {
        this.ProcedurePerInstituteFacadeREST = ProcedurePerInstituteFacadeREST;
    }

    public ProcedureRoomFacadeREST getProcedureRoomFacadeREST() {
        return ProcedureRoomFacadeREST;
    }

    public void setProcedureRoomFacadeREST(ProcedureRoomFacadeREST ProcedureRoomFacadeREST) {
        this.ProcedureRoomFacadeREST = ProcedureRoomFacadeREST;
    }

}
