/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ewebcomputing.printtexts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author owner
 */
public class Conversation  implements Serializable  {
    private String snippet;
    private ArrayList messages = new ArrayList();
    String id;
    String sender;

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public ArrayList getMessages() {
        return messages;
    }

    public void setMessages(ArrayList messages) {
        this.messages = messages;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public String getSender() {
       // if(messages.size() > 0)
         //   return ((Message)messages.get(0)).getSender();
        //return "";
        return sender;
    }

    public String getDate() {
        if(messages.size() > 0)
            return ((Message)messages.get(0)).getDate();
        return "";
    }

    public String toXml()   {
        StringBuffer xml = new StringBuffer();   
         for(Iterator i=messages.iterator(); i.hasNext();)   {
            xml.append(((Message)i.next()).toXml());
        }
        return xml.toString();
    }

     public String toPlainText()   {
        StringBuffer xml = new StringBuffer();

        for(Iterator i=messages.iterator(); i.hasNext();)   {
            xml.append(((Message)i.next()).toPlainText());
        }
        return xml.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Conversation other = (Conversation) obj;
        if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }
     

}