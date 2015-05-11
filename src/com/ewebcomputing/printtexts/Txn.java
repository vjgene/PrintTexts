/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ewebcomputing.printtexts;

/**
 *
 * @author owner
 */
public class Txn {

        public static String[] TXNS = new String[] {"PURCHASE","SALE","CREDIT", "DEBIT"};
        public static String[] PAYMENTS = new String[] { "","CASH","CHECK"};

        public static String TXN = "txn";

        public static final int SALE = 1;
        public static final int PURCHASE = 0;
        public static final int CREDIT = 2;
        public static final int DEBIT = 3;

        public static String STR_CREDIT = "CREDIT";
        public static String STR_DEBIT = "DEBIT";
        public static String STR_SALE = "SALE";
        public static String STR_PURCHASE = "PURCHASE";

        public static int CASH = 1;
        public static int CHECK = 2;

        public  String item;
        public String cust;
        public int qty;
        public double amount;
        public int txnType;
        public int paymentType;
}

