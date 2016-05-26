/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service;

import com.ClientStock;
import com.Operation;
import com.Stock;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
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

/**
 *
 * @author Leonardo
 */
@Stateless
@Path("com.operation")
public class OperationFacadeREST extends AbstractFacade<Operation> {

    @PersistenceContext(unitName = "Web-BankPU")
    private EntityManager em;

    public OperationFacadeREST() {
        super(Operation.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Operation entity) {
        if(!entity.getState().equals("waiting"))
        {
            super.create(entity);
            return;
        }
        if (entity.getOperationType())//venda
        {
            ArrayList<ClientStock> l= new ArrayList<>(entity.getFkOwnerId().getClientStockCollection());
            for (int i = 0; i < l.size(); i++) {
                if(l.get(i).getStock().equals(entity.getFkStockId()))
                {
                    if(l.get(i).getQuantity()<entity.getQuantity())
                        return;
                     l.get(i).setQuantity( l.get(i).getQuantity()-entity.getQuantity());
                    getEntityManager().persist(l.get(i));
                    entity.getFkOwnerId().setClientStockCollection(l);
                    getEntityManager().persist(entity.getFkOwnerId());
                    
                    super.create(entity);
                }
            } 
        } 
        else{
            System.out.println(entity.getFkStockId().getQuantity());
            System.out.println(entity);
            entity.setCreationDate(Date.from(Instant.now()));
            if(entity.getFkStockId().getQuantity()>entity.getQuantity())
            {
                
            System.out.println("yes");
                super.create(entity);
            } 
        }
        
        try {
            
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            
            channel.queueDeclare("hello", false, false, false, null);
            String message = entity.getOperationId()+","+entity.getFkOwnerId().getName()+","+entity.getFkStockId().getName()+","+entity.getQuantity();
            channel.basicPublish("", "hello", null, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
            channel.close();
            connection.close();
        } catch (IOException ex) {
            Logger.getLogger(ClientFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TimeoutException ex) {
            Logger.getLogger(ClientFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Operation entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Operation find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Operation> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Operation> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
    @Path("waiting")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Operation> findWaiting() {
        List<Operation> list = super.findAll();
        List<Operation> wait = super.findAll().subList(0, 0);
        for (int i = 0; i < list.size(); i++) {
            if ("waiting".equals(list.get(i).getState())) {
                wait.add(list.get(i));
            }
        }
        return wait;
    }

    @GET
    @Path("{id}/state")
    @Produces({MediaType.TEXT_PLAIN})
    public String getstate(@PathParam("id") Integer id) {
        return super.find(id).getState();
    }

    @GET //  >_> isto devia ser post :\
    @Path("{id}/execute")
    @Produces({MediaType.TEXT_PLAIN})
    public Boolean execute(@PathParam("id") Integer id) {
        Operation op = super.find(id);
        if(op==null)
            return false; 
        if (!"waiting".equals(op.getState())) {
            return false;
        }
        op.setState("executed");
        op.setExecutionDate(Date.from(Instant.now()));
        op.setOperationStockValue(op.getFkStockId().getStockValue());
        getEntityManager().persist(op);
        if (!op.getOperationType()) { //compra
            List<ClientStock> l = new ArrayList<>(op.getFkOwnerId().getClientStockCollection());
            for (int i = 0; i < l.size(); i++) {
                if (l.get(i).getStock().equals(op.getFkStockId())) {
                    l.get(i).setQuantity(l.get(i).getQuantity() + op.getQuantity());
                    getEntityManager().persist(l.get(i));
                    
                    
                    Stock s = op.getFkStockId();
                    s.setQuantity(s.getQuantity() - op.getQuantity());
                    getEntityManager().persist(s);
                    return true;
                }
            }
            ClientStock cs = new ClientStock(op.getFkStockId().getStockId(), op.getFkOwnerId().getClientId());
            cs.setQuantity(op.getQuantity());
            getEntityManager().persist(cs);
            l.add(cs);
            op.getFkOwnerId().setClientStockCollection(l);
            getEntityManager().persist(op.getFkOwnerId());
            //mudar a stock para ter menos

            Stock s = op.getFkStockId();
            s.setQuantity(s.getQuantity() - op.getQuantity());
            getEntityManager().persist(s);
        } else {

            Stock s = op.getFkStockId();
            s.setQuantity(s.getQuantity() + op.getQuantity());
            getEntityManager().persist(s);
        }
        return true;

    }
}
