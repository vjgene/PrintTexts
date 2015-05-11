/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ewebcomputing.printtexts;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TwoLineListItem;

/**
 *
 * @author owner
 */
public class PrintTextsListItem extends TwoLineListItem {

    private Message msg;

    public Message getMsg() {
        return msg;
    }

    public void setMsg(Message msg) {
        this.msg = msg;
    }
    public PrintTextsListItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public PrintTextsListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PrintTextsListItem(Context context) {
        super(context);
    }
}
