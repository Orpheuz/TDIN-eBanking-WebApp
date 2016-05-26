/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSRuntimeException;
import javax.jms.Queue;


/**
 *
 * @author Leonardo
 */
@Named(value = "senderBean")
@ApplicationScoped
public class SenderBean {

    @Resource(mappedName = "jms/myQueue")
    private Queue myQueue;

    @Inject
    @JMSConnectionFactory("jms/myQueueFactory")
    private JMSContext context;

    private String messageText;

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
    /**
     * Creates a new instance of SenderBean
     */
    public SenderBean() {
    }

    public void sendJMSMessageToMyQueue() {
        
         try {
            String text = "Message from producer: " + messageText;
            context.createProducer().send(myQueue, text);
 
            FacesMessage facesMessage =
                    new FacesMessage("Sent message: " + text);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } catch (JMSRuntimeException t) {
             System.out.println(t.toString());
        }
    }

    
}
