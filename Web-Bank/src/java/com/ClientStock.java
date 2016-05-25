/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Leonardo
 */
@Entity
@Table(name = "client_stock")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ClientStock.findAll", query = "SELECT c FROM ClientStock c"),
    @NamedQuery(name = "ClientStock.findByPkFkStockId", query = "SELECT c FROM ClientStock c WHERE c.clientStockPK.pkFkStockId = :pkFkStockId"),
    @NamedQuery(name = "ClientStock.findByPkFkClientId", query = "SELECT c FROM ClientStock c WHERE c.clientStockPK.pkFkClientId = :pkFkClientId"),
    @NamedQuery(name = "ClientStock.findByQuantity", query = "SELECT c FROM ClientStock c WHERE c.quantity = :quantity")})
public class ClientStock implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ClientStockPK clientStockPK;
    @Column(name = "quantity")
    private Integer quantity;
    @JoinColumn(name = "pk_fk_stock_id", referencedColumnName = "stock_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Stock stock;
    @JoinColumn(name = "pk_fk_client_id", referencedColumnName = "client_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Client client;

    public ClientStock() {
    }

    public ClientStock(ClientStockPK clientStockPK) {
        this.clientStockPK = clientStockPK;
    }

    public ClientStock(int pkFkStockId, int pkFkClientId) {
        this.clientStockPK = new ClientStockPK(pkFkStockId, pkFkClientId);
    }

    public ClientStockPK getClientStockPK() {
        return clientStockPK;
    }

    public void setClientStockPK(ClientStockPK clientStockPK) {
        this.clientStockPK = clientStockPK;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (clientStockPK != null ? clientStockPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ClientStock)) {
            return false;
        }
        ClientStock other = (ClientStock) object;
        if ((this.clientStockPK == null && other.clientStockPK != null) || (this.clientStockPK != null && !this.clientStockPK.equals(other.clientStockPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ClientStock[ clientStockPK=" + clientStockPK + " ]";
    }
    
}
