/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ewebcomputing.printtexts;

import java.util.Iterator;
import java.util.List;

/**
 *
 * @author owner
 */
public class PrintService {
    public static void printConversations(List l)   {
        StringBuffer str = new StringBuffer("<messages>");
        for(Iterator i=l.iterator(); i.hasNext();)  {
            Object obj = i.next();
            if(obj instanceof Conversation)
                str.append(((Conversation)obj).toXml());
            else
                str.append(((Message)obj).toXml());
        }
        str.append("</messages>");
        //System.out.println(str);
    }

    public static String  toString(List l)    {
        StringBuffer str = new StringBuffer("<messages>");
        for(Iterator i=l.iterator(); i.hasNext();)  {
            Object obj = i.next();
            if(obj instanceof Conversation)
                str.append(((Conversation)obj).toXml());
            else
                str.append(((Message)obj).toXml());
        }
        str.append("</messages>");
        return str.toString();

    }

    public static String  toPlainText(List l)    {
        StringBuffer str = new StringBuffer("");
        for(Iterator i=l.iterator(); i.hasNext();)  {
            Object obj = i.next();
            if(obj instanceof Conversation)
                str.append(((Conversation)obj).toPlainText());
            else
                str.append(((Message)obj).toPlainText());
        }
        str.append("");
        return str.toString();

    }
}
