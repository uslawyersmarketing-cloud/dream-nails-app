package com.dreamnails.salon;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.widget.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class SalesActivity extends Activity {
    DatabaseHelper db; LinearLayout list;
    @Override protected void onCreate(Bundle b){super.onCreate(b);db=new DatabaseHelper(this);render();}
    private void render(){
        ScrollView s=new ScrollView(this); LinearLayout r=Ui.root(this);
        r.addView(Ui.title(this,"Sales")); r.addView(Ui.subtitle(this,"RECORD EACH SERVICE"));
        EditText name=Ui.input(this,"Customer name"); EditText phone=Ui.input(this,"Phone number");
        EditText service=Ui.input(this,"Service e.g. Nails, Facial, Massage");
        EditText price=Ui.input(this,"Price charged"); price.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        EditText notes=Ui.input(this,"Notes (optional)");
        r.addView(name);r.addView(phone);r.addView(service);r.addView(price);r.addView(notes);
        Button save=Ui.button(this,"Save Sale"); r.addView(save);
        r.addView(Ui.section(this,"Recent Sales")); list=new LinearLayout(this); list.setOrientation(LinearLayout.VERTICAL); r.addView(list);
        save.setOnClickListener(v->{try{
            if(name.getText().toString().trim().isEmpty()||service.getText().toString().trim().isEmpty()){Toast.makeText(this,"Add customer and service",Toast.LENGTH_SHORT).show();return;}
            double p=Double.parseDouble(price.getText().toString().trim());
            db.addSale(name.getText().toString().trim(),phone.getText().toString().trim(),service.getText().toString().trim(),p,notes.getText().toString().trim());
            Toast.makeText(this,"Sale saved",Toast.LENGTH_SHORT).show();render();
        }catch(Exception e){Toast.makeText(this,"Enter a valid price",Toast.LENGTH_SHORT).show();}});
        load();s.addView(r);setContentView(s);
    }
    private void load(){list.removeAllViews();SimpleDateFormat f=new SimpleDateFormat("dd MMM yyyy",Locale.getDefault());
        try(Cursor c=db.sales()){int n=0;while(c.moveToNext()&&n++<30){
            list.addView(Ui.card(this,c.getString(1)+" • "+c.getString(3)+"\nRs. "+String.format(Locale.US,"%.0f",c.getDouble(4))+" • "+f.format(new Date(c.getLong(5)))));
        }}
    }
}
