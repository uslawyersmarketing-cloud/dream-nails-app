package com.dreamnails.salon;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class CustomersActivity extends Activity {
    DatabaseHelper db; LinearLayout list;
    @Override protected void onCreate(Bundle b){super.onCreate(b);db=new DatabaseHelper(this);render();}
    private void render(){
        ScrollView s=new ScrollView(this); LinearLayout r=Ui.root(this);
        r.addView(Ui.title(this,"Customers")); r.addView(Ui.subtitle(this,"KEEP EVERY CLIENT CLOSE"));
        EditText name=Ui.input(this,"Customer name"); EditText phone=Ui.input(this,"Phone number"); EditText notes=Ui.input(this,"Notes (optional)");
        r.addView(name);r.addView(phone);r.addView(notes);
        Button save=Ui.button(this,"Save Customer"); r.addView(save);
        r.addView(Ui.section(this,"Customer Records")); list=new LinearLayout(this); list.setOrientation(LinearLayout.VERTICAL); r.addView(list);
        save.setOnClickListener(v->{ if(name.getText().toString().trim().isEmpty()){Toast.makeText(this,"Enter customer name",Toast.LENGTH_SHORT).show();return;}
            db.addCustomer(name.getText().toString().trim(),phone.getText().toString().trim(),notes.getText().toString().trim());
            Toast.makeText(this,"Customer saved",Toast.LENGTH_SHORT).show(); render();});
        load(); s.addView(r);setContentView(s);
    }
    private void load(){ list.removeAllViews(); SimpleDateFormat f=new SimpleDateFormat("dd MMM yyyy",Locale.getDefault());
        try(Cursor c=db.customers()){while(c.moveToNext()){
            String x=c.getString(1)+"\n"+safe(c.getString(2))+"\nAdded "+f.format(new Date(c.getLong(4)));
            if(c.getString(3)!=null&&!c.getString(3).isEmpty()) x+="\n"+c.getString(3);
            list.addView(Ui.card(this,x));
        }}
    }
    private String safe(String s){return s==null?"":s;}
}
