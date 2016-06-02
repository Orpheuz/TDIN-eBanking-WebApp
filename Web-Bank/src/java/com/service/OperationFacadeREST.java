/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service;

import com.Client;
import com.ClientStock;
import com.Operation;
import com.Stock;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.sun.mail.smtp.SMTPTransport;
import java.io.IOException;
import java.security.Security;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
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
            ArrayList<ClientStock> l= new ArrayList<>(getEntityManager().find(Client.class, entity.getFkOwnerId().getClientId()).getClientStockCollection());
            Boolean fail=false;
            for (int i = 0; i < l.size(); i++) { 
                if(l.get(i).getStock().equals(entity.getFkStockId()))
                {
                    if(l.get(i).getQuantity()<entity.getQuantity())
                        return;
                    l.get(i).setQuantity( l.get(i).getQuantity()-entity.getQuantity());
                    getEntityManager().persist(l.get(i));
                    entity.getFkOwnerId().setClientStockCollection(l);
                    
                    entity.setCreationDate(Date.from(Instant.now()));
                    super.create(entity);
                }
            } 
            if(fail)
                return;
        } 
        else{
            entity.setCreationDate(Date.from(Instant.now()));
            if(entity.getFkStockId().getQuantity()>entity.getQuantity())
            {
                
            System.out.println("yes");
                super.create(entity);
            } 
            else
            {
                return;
            }
        }
        
        try {
            
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            
            channel.queueDeclare("hello", false, false, false, null);
            System.out.println(super.findAll().get(super.findAll().size()-1).getOperationId()+","+entity.getFkOwnerId().getName()+","+entity.getFkStockId().getName()+","+entity.getQuantity());
            
            String message = super.findAll().get(super.findAll().size()-1).getOperationId()+","+entity.getFkOwnerId().getName()+","+entity.getFkStockId().getName()+","+entity.getQuantity();
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
        
        try {
            Send("ei12072", "Feup1res", op.getFkOwnerId().getEmail(), "", "Operation Executed", "Your Operation with Id "+ op.getOperationId() + " has been executed!");
        } catch (MessagingException ex) {
            Logger.getLogger(OperationFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;

    }
    
    public static void Send(final String username, final String password, String recipientEmail, String ccEmail, String title, String message) throws AddressException, MessagingException {
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

        // Get a Properties object
        Properties props = System.getProperties();
        props.setProperty("mail.smtps.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "465");
        props.setProperty("mail.smtps.auth", "true");

        /*
        If set to false, the QUIT command is sent and the connection is immediately closed. If set 
        to true (the default), causes the transport to wait for the response to the QUIT command.

        ref :   http://java.sun.com/products/javamail/javadocs/com/sun/mail/smtp/package-summary.html
                http://forum.java.sun.com/thread.jspa?threadID=5205249
                smtpsend.java - demo program from javamail
        */
        props.put("mail.smtps.quitwait", "false");

        Session session = Session.getInstance(props, null);

        // -- Create a new message --
        final MimeMessage msg = new MimeMessage(session);

        // -- Set the FROM and TO fields --
        msg.setFrom(new InternetAddress(username + "@fe.up.pt"));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail, false));

        if (ccEmail.length() > 0) {
            msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(ccEmail, false));
        }

        msg.setSubject(title);
        msg.setText(message, "utf-8");
        msg.setSentDate(new Date());

        SMTPTransport t = (SMTPTransport)session.getTransport("smtps");

        t.connect("smtp.fe.up.pt", username, password);
        t.sendMessage(msg, msg.getAllRecipients());      
        t.close();
    }
}
