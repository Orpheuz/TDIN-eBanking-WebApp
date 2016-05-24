/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.ClientStock;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Vitor
 */
@Stateless
public class ClientStockFacade extends AbstractFacade<ClientStock> {

    @PersistenceContext(unitName = "Bank-WebAppPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ClientStockFacade() {
        super(ClientStock.class);
    }
    
}
