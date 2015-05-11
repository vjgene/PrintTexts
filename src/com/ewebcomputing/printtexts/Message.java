/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ewebcomputing.printtexts;

import java.io.Serializable;

/**
 *
 * @author owner
 */
public class Message implements Serializable {
    private String sender;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public String toXml()   {
        StringBuffer xml = new StringBuffer("<message><sender>");
        xml.append(getSender());
        xml.append("</sender>");
        xml.append("<date>");
        xml.append(getDate());
        xml.append("</date>");
        xml.append("<body>");
        xml.append(getBody());
        xml.append("</body></message>");
        return xml.toString();
    }

    public String toPlainText()   {
        StringBuffer xml = new StringBuffer("");
        xml.append(getSender());
        xml.append("(;)");
        xml.append(getDate());
        xml.append("(;)");
        xml.append(getBody());
        xml.append("[;]");
        return xml.toString();
    }
    private String date;
    private String snippet;
    private String body;
}