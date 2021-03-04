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
import javax.ws.rs.core.Response;
import lk.gov.health.procedureroomservice.ProcedureRoomType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author user
 */
@Stateless
@Path("lk.gov.health.procedureroomservice.procedureroomtype")
public class ProcedureRoomTypeFacadeREST extends AbstractFacade<ProcedureRoomType> {

    @PersistenceContext(unitName = "hmisPU")
    private EntityManager em;

    public ProcedureRoomTypeFacadeREST() {
        super(ProcedureRoomType.class);
    }

    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response Create(ProcedureRoomType entity) {
        entity.setId(null);
        System.out.println("Status" + checkExists(entity.getTypeId()) );
        if (!checkExists(entity.getTypeId())) {
            super.create(entity);
            return Response.status(Response.Status.ACCEPTED).build();
        } else {
            return Response.status(Response.Status.CONFLICT).entity("Duplicate Entry..!Unable to create new record").build();
        }
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response edit(@PathParam("id") Long id, ProcedureRoomType entity) {
        if ((entity.getTypeId() == super.find(id).getTypeId()) || (!checkExists(entity.getTypeId()))) {
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
        return getJSONObject(super.find(id)).toString();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAll() {
        JSONArray ja_ = new JSONArray();

        List<ProcedureRoomType> procRoomTypeList;
        procRoomTypeList = super.findAll();

        for (ProcedureRoomType procRoomType : procRoomTypeList) {
            ja_.add(getJSONObject(procRoomType));
        }
        return ja_.toString();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public String findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        JSONArray ja_ = new JSONArray();

        List<ProcedureRoomType> procRoomTypeList;
        procRoomTypeList = super.findRange(new int[]{from, to});

        for (ProcedureRoomType procRoomType : procRoomTypeList) {
            ja_.add(getJSONObject(procRoomType));
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

    private JSONObject getJSONObject(ProcedureRoomType procType) {
        JSONObject jo_ = new JSONObject();

        jo_.put("id", procType.getId());
        jo_.put("typeId", procType.getTypeId());
        jo_.put("description", procType.getDescription());

        return jo_;
    }

    @GET
    @Path("filer_list/{searchVal}")
    @Produces(MediaType.APPLICATION_JSON)
    public String findFilteredList(@PathParam("searchVal") String searchVal) {
        JSONArray ja_ = new JSONArray();

        String jpql;
        Map m = new HashMap();
        jpql = "SELECT pr FROM ProcedureRoomType pr WHERE upper(pr.typeId) like :searchVal";

        m.put("searchVal", "%" + searchVal.toUpperCase() + "%");

        List<ProcedureRoomType> procTypeList = super.findByJpql(jpql, m);

        for (ProcedureRoomType procType : procTypeList) {
            ja_.add(getJSONObject(procType));
        }
        return ja_.toString();
    }

    public boolean checkExists(String searchVal) {
        String jpql;
        Map m = new HashMap();
        jpql = "SELECT pr FROM ProcedureRoomType pr WHERE upper(pr.typeId) like :searchVal";

        m.put("searchVal", searchVal.toUpperCase());

        List<ProcedureRoomType> procRoomTypeList = super.findByJpql(jpql, m);

        if (procRoomTypeList.size() > 0) {
            return true;
        }
        return false;
    }
}
