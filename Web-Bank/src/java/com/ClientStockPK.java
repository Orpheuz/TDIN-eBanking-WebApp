/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Leonardo
 */
@Embeddable
public class ClientStockPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "pk_fk_stock_id")
    private int pkFkStockId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "pk_fk_client_id")
    private int pkFkClientId;

    public ClientStockPK() {
    }

    public ClientStockPK(int pkFkStockId, int pkFkClientId) {
        this.pkFkStockId = pkFkStockId;
        this.pkFkClientId = pkFkClientId;
    }

    public int getPkFkStockId() {
        return pkFkStockId;
    }

    public void setPkFkStockId(int pkFkStockId) {
        this.pkFkStockId = pkFkStockId;
    }

    public int getPkFkClientId() {
        return pkFkClientId;
    }

    public void setPkFkClientId(int pkFkClientId) {
        this.pkFkClientId = pkFkClientId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) pkFkStockId;
        hash += (int) pkFkClientId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ClientStockPK)) {
            return false;
        }
        ClientStockPK other = (ClientStockPK) object;
        if (this.pkFkStockId != other.pkFkStockId) {
            return false;
        }
        if (this.pkFkClientId != other.pkFkClientId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ClientStockPK[ pkFkStockId=" + pkFkStockId + ", pkFkClientId=" + pkFkClientId + " ]";
    }
    
}
