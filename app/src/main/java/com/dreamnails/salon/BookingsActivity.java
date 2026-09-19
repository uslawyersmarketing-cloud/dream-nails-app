package com.dreamnails.salon;

import android.app.*;
import android.content.*;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.widget.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class BookingsActivity extends Activity {
    DatabaseHelper db; long selectedTime=System.currentTimeMillis()+86400000L; TextView when; LinearLayout list;
    @Override protected void onCreate(Bundle b){super.onCreate(b);db=new DatabaseHelper(this);render();}
    private void render(){
        ScrollView s=new ScrollView(this); LinearLayout r=Ui.root(this);
        r.addView(Ui.title(this,"Bookings")); r.addView(Ui.subtitle(this,"PLAN BEAUTIFUL DAYS"));
        EditText name=Ui.input(this,"Customer name"); EditText phone=Ui.input(this,"Phone number");
        EditText service=Ui.input(this,"Service"); EditText price=Ui.input(this,"Price"); price.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        r.addView(name);r.addView(phone);r.addView(service);r.addView(price);
        when=Ui.card(this,format(selectedTime)); r.addView(when);
        Button choose=Ui.button(this,"Choose Date & Time");r.addView(choose);
        Button save=Ui.button(this,"Save Booking + 24h Reminder");r.addView(save);
        r.addView(Ui.section(this,"Upcoming Appointments"));list=new LinearLayout(this);list.setOrientation(LinearLayout.VERTICAL);r.addView(list);
        choose.setOnClickListener(v->pickDate());
        save.setOnClickListener(v->{try{
            if(name.getText().toString().trim().isEmpty()||service.getText().toString().trim().isEmpty()){Toast.makeText(this,"Add customer and service",Toast.LENGTH_SHORT).show();return;}
            double p=price.getText().toString().trim().isEmpty()?0:Double.parseDouble(price.getText().toString().trim());
            long id=db.addBooking(name.getText().toString().trim(),phone.getText().toString().trim(),service.getText().toString().trim(),p,selectedTime,"");
            scheduleReminder((int)id,name.getText().toString().trim(),service.getText().toString().trim(),selectedTime);
            Toast.makeText(this,"Booking saved",Toast.LENGTH_SHORT).show();render();
        }catch(Exception e){Toast.makeText(this,"Check the booking details",Toast.LENGTH_SHORT).show();}});
        load();s.addView(r);setContentView(s);
    }
    private void pickDate(){
        Calendar c=Calendar.getInstance();c.setTimeInMillis(selectedTime);
        new DatePickerDialog(this,(v,y,m,d)->{
            Calendar x=Calendar.getInstance();x.setTimeInMillis(selectedTime);x.set(y,m,d);
            new TimePickerDialog(this,(tv,h,min)->{x.set(Calendar.HOUR_OF_DAY,h);x.set(Calendar.MINUTE,min);x.set(Calendar.SECOND,0);selectedTime=x.getTimeInMillis();when.setText(format(selectedTime));},x.get(Calendar.HOUR_OF_DAY),x.get(Calendar.MINUTE),false).show();
        },c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH)).show();
    }
    private void scheduleReminder(int id,String customer,String service,long appt){
        long trigger=appt-86400000L;if(trigger<=System.currentTimeMillis())return;
        Intent i=new Intent(this,ReminderReceiver.class);i.putExtra("customer",customer);i.putExtra("service",service);i.putExtra("time",appt);
        PendingIntent p=PendingIntent.getBroadcast(this,id,i,PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);
        ((AlarmManager)getSystemService(ALARM_SERVICE)).setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,trigger,p);
    }
    private void load(){list.removeAllViews();try(Cursor c=db.bookings()){int n=0;while(c.moveToNext()&&n++<40){list.addView(Ui.card(this,format(c.getLong(5))+"\n"+c.getString(1)+" • "+c.getString(3)+"\n"+c.getString(6)));}}}
    private String format(long t){return new SimpleDateFormat("EEE, dd MMM yyyy • h:mm a",Locale.getDefault()).format(new Date(t));}
}
