/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.gov.health.procedureservice.enums;

/**
 *
 * @author user
 */
public enum ProcPerClientStates {
    CREATED("Created"),
    INPROGRESS("In-Progress"),
    DONE("Done"),
    HOLD("Hold"),
    REJECTED("Rejected"),
    RECONSIDER("Request Reconsider");

    private final String label;

    private ProcPerClientStates(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
