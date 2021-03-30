/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.gov.health.procedureroomservice.service;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import lk.gov.health.procedureroomservice.CurrentHash;
import lk.gov.health.procedureroomservice.Institute;
import lk.gov.health.procedureservice.serviceutils.CurrentHashFacade;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author user
 */
@Stateless
@Path("lk.gov.health.procedureroomservice.institute")
public class InstituteFacadeREST extends AbstractFacade<Institute> {

    @PersistenceContext(unitName = "hmisPU")
    private EntityManager em;

    public InstituteFacadeREST() {
        super(Institute.class);
    }
    
    @EJB
    private CurrentHashFacade currHash;

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Institute entity) {
        entity.setId(null);
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, Institute entity) {
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
    public Institute find(@PathParam("id") Long id) {
        return super.find(id);
    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public String getAll() {
        JSONArray ja_ = new JSONArray();

        List<Institute> institutionList;
        institutionList = super.findAll();

        for (Institute institute : institutionList) {
            ja_.add(getJSONObject(institute));
        }
        return ja_.toString();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Institute> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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

    private JSONObject getJSONObject(Institute institute) {
        JSONObject jo_ = new JSONObject();

        jo_.put("id", institute.getId());
        jo_.put("code", institute.getCode());
        jo_.put("name", institute.getName());
        jo_.put("hin", institute.getHin());
        jo_.put("address", institute.getAddress());
        jo_.put("provinceId", institute.getProvinceId());
        jo_.put("districtId", institute.getDistrictId());

        return jo_;
    }

    @GET
    @Path("/filer_list/{searchVal}")
    @Produces(MediaType.APPLICATION_JSON)
    public String findFilteredList(@PathParam("searchVal") String searchVal) {
        JSONArray ja_ = new JSONArray();
        
        if(!Is_Sync()){
            Sync_Institutes();
            CurrentHash hashObj = currHash.findByField("owner", "INSTITUTE");            
            hashObj.setCurrHash(this.Get_Institute_Hash());            
            currHash.edit(hashObj);           
        }
        
        String jpql;
        Map m = new HashMap();
        jpql = "SELECT i FROM Institute i WHERE upper(i.code) like :searchVal";
        m.put("searchVal", "%" + searchVal.toUpperCase() + "%");

        List<Institute> instList = super.findByJpql(jpql, m);

        for (Institute inst : instList) {
            ja_.add(getJSONObject(inst));
        }
        return ja_.toString();
    }

    public void Sync_Institutes() {
        ArrayList<Institute> items;
        Institute selected = new Institute();
        try {
            Client client = Client.create();
            WebResource webResource1 = client.resource("https://chims.health.gov.lk/data?name=get_module_institutes_list");
            ClientResponse cr = webResource1.accept("application/json").get(ClientResponse.class);
            String outpt = cr.getEntity(String.class);
            items = selected.getObjectList((JSONArray) new JSONParser().parse(outpt));

            for (Institute inst : items) {
                inst.setId(null);                
                super.create(inst);
            }
        } catch (org.json.simple.parser.ParseException ex) {
            Logger.getLogger(InstituteFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean Is_Sync() {        
        return Get_Current_Hash().getCurrHash().equals(this.Get_Institute_Hash());
    }
    
    private CurrentHash Get_Current_Hash(){
        HashMap<String,Object> p_ = new HashMap<>();
        String jpql_ = "SELECT h FROM CurrentHash h WHERE h.owner = ?";
        p_.put("owner", "INSTITITUE");
        return (CurrentHash) this.currHash.findByJpql(jpql_, p_);
    }

    public String Get_Institute_Hash() {
        Client client = Client.create();
        WebResource webResource1 = client.resource("http://localhost:8080/ProcedureRoomService/resources/lk.gov.health.procedureroomservice.institute");
        ClientResponse cr = webResource1.accept("application/json").get(ClientResponse.class);
        String output = cr.getEntity(String.class);
        
        return output;
    }

    public CurrentHashFacade getCurrHash() {
        return currHash;
    }

    public void setCurrHash(CurrentHashFacade currHash) {
        this.currHash = currHash;
    }

}
