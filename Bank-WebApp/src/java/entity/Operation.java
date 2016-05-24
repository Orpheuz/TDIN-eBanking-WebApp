/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Vitor
 */
@Entity
@Table(name = "operation")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Operation.findAll", query = "SELECT o FROM Operation o"),
    @NamedQuery(name = "Operation.findByOperationId", query = "SELECT o FROM Operation o WHERE o.operationId = :operationId"),
    @NamedQuery(name = "Operation.findByCreationDate", query = "SELECT o FROM Operation o WHERE o.creationDate = :creationDate"),
    @NamedQuery(name = "Operation.findByExecutionDate", query = "SELECT o FROM Operation o WHERE o.executionDate = :executionDate"),
    @NamedQuery(name = "Operation.findByState", query = "SELECT o FROM Operation o WHERE o.state = :state"),
    @NamedQuery(name = "Operation.findByOperationStockValue", query = "SELECT o FROM Operation o WHERE o.operationStockValue = :operationStockValue"),
    @NamedQuery(name = "Operation.findByQuantity", query = "SELECT o FROM Operation o WHERE o.quantity = :quantity"),
    @NamedQuery(name = "Operation.findByOperationType", query = "SELECT o FROM Operation o WHERE o.operationType = :operationType")})
public class Operation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "operation_id")
    private Integer operationId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "creation_date")
    @Temporal(TemporalType.DATE)
    private Date creationDate;
    @Column(name = "execution_date")
    @Temporal(TemporalType.DATE)
    private Date executionDate;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "state")
    private String state;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "operation_stock_value")
    private Double operationStockValue;
    @Basic(optional = false)
    @NotNull
    @Column(name = "quantity")
    private int quantity;
    @Basic(optional = false)
    @NotNull
    @Column(name = "operation_type")
    private boolean operationType;
    @JoinColumn(name = "fk_owner_id", referencedColumnName = "client_id")
    @ManyToOne
    private Client fkOwnerId;
    @JoinColumn(name = "fk_stock_id", referencedColumnName = "stock_id")
    @ManyToOne
    private Stock fkStockId;

    public Operation() {
    }

    public Operation(Integer operationId) {
        this.operationId = operationId;
    }

    public Operation(Integer operationId, Date creationDate, String state, int quantity, boolean operationType) {
        this.operationId = operationId;
        this.creationDate = creationDate;
        this.state = state;
        this.quantity = quantity;
        this.operationType = operationType;
    }

    public Integer getOperationId() {
        return operationId;
    }

    public void setOperationId(Integer operationId) {
        this.operationId = operationId;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(Date executionDate) {
        this.executionDate = executionDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Double getOperationStockValue() {
        return operationStockValue;
    }

    public void setOperationStockValue(Double operationStockValue) {
        this.operationStockValue = operationStockValue;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean getOperationType() {
        return operationType;
    }

    public void setOperationType(boolean operationType) {
        this.operationType = operationType;
    }

    public Client getFkOwnerId() {
        return fkOwnerId;
    }

    public void setFkOwnerId(Client fkOwnerId) {
        this.fkOwnerId = fkOwnerId;
    }

    public Stock getFkStockId() {
        return fkStockId;
    }

    public void setFkStockId(Stock fkStockId) {
        this.fkStockId = fkStockId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (operationId != null ? operationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Operation)) {
            return false;
        }
        Operation other = (Operation) object;
        if ((this.operationId == null && other.operationId != null) || (this.operationId != null && !this.operationId.equals(other.operationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Operation[ operationId=" + operationId + " ]";
    }
    
}
