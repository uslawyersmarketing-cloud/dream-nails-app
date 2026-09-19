package com.dreamnails.salon;

import android.app.Activity;
import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class ReportsActivity extends Activity {
    DatabaseHelper db;
    @Override protected void onCreate(Bundle b){super.onCreate(b);db=new DatabaseHelper(this);
        ScrollView s=new ScrollView(this);LinearLayout r=Ui.root(this);
        r.addView(Ui.title(this,"Reports & Export"));r.addView(Ui.subtitle(this,"YOUR BUSINESS, ORGANIZED"));
        r.addView(Ui.card(this,"This Month Sales\nRs. "+String.format(Locale.US,"%.0f",db.salesMonth())));
        r.addView(Ui.card(this,"Total Customers\n"+db.customerCount()));
        Button sales=Ui.button(this,"Download Sales CSV");Button customers=Ui.button(this,"Download Customers CSV");
        r.addView(sales);r.addView(customers);
        sales.setOnClickListener(v->exportSales());customers.setOnClickListener(v->exportCustomers());
        r.addView(Ui.card(this,"Exports open in your phone's share menu. You can save them to Files, email them, or send them through WhatsApp."));
        s.addView(r);setContentView(s);
    }
    private void exportSales(){try{
        File f=new File(getExternalFilesDir(null),"dream-nails-sales.csv");try(PrintWriter w=new PrintWriter(f)){
            w.println("Customer,Phone,Service,Price,Date,Notes");SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd",Locale.US);
            try(Cursor c=db.sales()){while(c.moveToNext())w.println(q(c.getString(1))+","+q(c.getString(2))+","+q(c.getString(3))+","+c.getDouble(4)+","+df.format(new Date(c.getLong(5)))+","+q(c.getString(6)));}}
        }share(f,"text/csv");}catch(Exception e){Toast.makeText(this,"Could not export",Toast.LENGTH_SHORT).show();}}
    private void exportCustomers(){try{
        File f=new File(getExternalFilesDir(null),"dream-nails-customers.csv");try(PrintWriter w=new PrintWriter(f)){
            w.println("Name,Phone,Notes");try(Cursor c=db.customers()){while(c.moveToNext())w.println(q(c.getString(1))+","+q(c.getString(2))+","+q(c.getString(3)));}}
        }share(f,"text/csv");}catch(Exception e){Toast.makeText(this,"Could not export",Toast.LENGTH_SHORT).show();}}
    private String q(String s){if(s==null)s="";return """+s.replace(""","""")+""";}
    private void share(File f){Uri uri=androidx.core.content.FileProvider.getUriForFile(this,getPackageName()+".provider",f);}
    private void share(File f,String type){
        Intent i=new Intent(Intent.ACTION_SEND);i.setType(type);
        Uri u=Uri.parse("content://"+getPackageName()+".files/"+f.getName());
        i.putExtra(Intent.EXTRA_STREAM,u);
        Toast.makeText(this,"CSV saved to: "+f.getAbsolutePath(),Toast.LENGTH_LONG).show();
    }
}
