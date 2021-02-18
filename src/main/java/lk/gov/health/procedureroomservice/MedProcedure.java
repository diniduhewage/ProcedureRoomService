/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.gov.health.procedureroomservice;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;
import lk.gov.health.procedureservice.enums.ObjectStatus;

/**
 *
 * @author user
 */
@Entity
@XmlRootElement
public class MedProcedure implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String procId;
    private String description;
    @ManyToOne
    private ProcedureType procType;
    @ManyToOne
    private ProcedureRoomType roomType;
    private String comment;
    private ObjectStatus status;
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MedProcedure)) {
            return false;
        }
        MedProcedure other = (MedProcedure) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lk.gov.health.procedureroomservice.MedProcedure[ id=" + id + " ]";
    }

    public String getProcId() {
        return procId;
    }

    public void setProcId(String procId) {
        this.procId = procId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProcedureType getProcType() {
        return procType;
    }

    public void setProcType(ProcedureType procType) {
        this.procType = procType;
    }

    public ProcedureRoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(ProcedureRoomType roomType) {
        this.roomType = roomType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ObjectStatus getStatus() {
        return status;
    }

    public void setStatus(ObjectStatus status) {
        this.status = status;
    }
    
}
