package com.accounting.costs_benefits.provider;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.accounting.costs_benefits.domain.Transaction;
import com.accounting.costs_benefits.domain.TransactionStorageMrg;
import com.accounting.costs_benefits.parser.TransactionParser;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by carlos on 9/06/13.
 */
public class SmsProvider implements TransactionProvider {

    private final String expectedAddress;

    public enum Column{
        TH_ID("thread_id"),
        ADDRESS("address"),
        PERSON("person"),
        DATE("date"),
        DATE_SENT("date_sent"),
        PROTOCOL("protocol"),
        READ("read"),
        STATUS("status"),
        TYPE("type"),
        REPLY_PATH_PRESENT("reply_path_present"),
        SUBJECT("subject"),
        BODY("body"),
        SERVICE_CENTER("service_center"),
        LOCKED("locked"),
        ERR_CODE("error_code"),
        SEEN("seen");

        final String value;

        private Column(String v){
            value = v;
        }
    }

    public static final String SMS_EXTRA_NAME ="pdus";
    private final TransactionParser parser;

    private final TransactionStorageMrg transactions;
    private final ContentResolver contentResolver;

    private final Set<TransactionListener> listeners;

    public SmsProvider(ContentResolver contentResolver, String expectedAddress, TransactionParser parser){
        this.listeners = new HashSet<TransactionListener>();
        this.contentResolver = contentResolver;
        this.expectedAddress = expectedAddress;
        this.transactions = new TransactionStorageMrg();
        this.parser = parser;
        loadSmsSet();

    }

    @Override
    public synchronized void register(TransactionListener listener){
        if (listeners.add(listener)){
            for (Set<Transaction> trs: transactions.getAll().values()){
                for (Transaction tr : trs){
                    listener.update(tr);
                }
            }
        }

    }

    public void onReceive( Context context, Intent intent )
    {
        // Get the SMS map from Intent
        Bundle extras = intent.getExtras();

        String messages = "";

        if ( extras != null )
        {
            // Get received SMS array
            Object[] smsExtra = (Object[]) extras.get( SMS_EXTRA_NAME );

            // Get ContentResolver object for pushing encrypted SMS to the incoming folder
            ContentResolver contentResolver = context.getContentResolver();

            for ( int i = 0; i < smsExtra.length; ++i )
            {
                SmsMessage sms = SmsMessage.createFromPdu((byte[]) smsExtra[i]);

                String body = sms.getMessageBody().toString();
                String address = sms.getOriginatingAddress();

                receive(address, body);
            }
        }

        notifyNewTransactions();

    }

    private synchronized void loadSmsSet(){
        Cursor cursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null);

        while (cursor.moveToNext()){
            int indexBody = cursor.getColumnIndex(Column.BODY.toString());
            int indexAddress = cursor.getColumnIndex(Column.ADDRESS.toString());

            String body = cursor.getString(indexBody);
            String address = cursor.getString(indexAddress);

            receive(address, body);
        }

        notifyNewTransactions();
    }

    private void receive(String address, String body){
        if (address!=null && address.equals(expectedAddress)){
            try{
                parser.parse(body); // TODO Do we use multiple parsers?
                transactions.create(parser.getTitle(), parser.getTimestamp(), parser.getQuantity());
            }catch(Exception e){ }
        }
    }

    private void notifyNewTransactions(){
        for (TransactionListener l: listeners){
            l.update(transactions.getLastUpdated());
        }
    }

}
