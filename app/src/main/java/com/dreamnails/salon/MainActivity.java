package com.dreamnails.salon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.util.Locale;

public class MainActivity extends Activity {
    private DatabaseHelper db;
    private LinearLayout root;

    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        db = new DatabaseHelper(this);
        build();
    }

    @Override protected void onResume() {
        super.onResume();
        if (root != null) build();
    }

    private void build() {
        ScrollView s = new ScrollView(this);
        root = Ui.root(this);
        root.addView(Ui.title(this, "DREAM NAILS"));
        root.addView(Ui.subtitle(this, "DREAM IT. NAIL IT."));
        TextView hello = Ui.section(this, "Good Morning, Salon Owner ♡");
        root.addView(hello);
        root.addView(Ui.card(this, "Today's Bookings\n" + db.bookingCountToday()));
        root.addView(Ui.card(this, "Today's Sales\nRs. " + String.format(Locale.US, "%.0f", db.salesToday())));
        root.addView(Ui.card(this, "Total Customers\n" + db.customerCount()));
        root.addView(Ui.card(this, "This Month\nRs. " + String.format(Locale.US, "%.0f", db.salesMonth())));
        root.addView(Ui.section(this, "Quick Actions"));

        var b1=Ui.button(this,"+ New Sale"); b1.setOnClickListener(v->startActivity(new Intent(this, SalesActivity.class))); root.addView(b1);
        var b2=Ui.button(this,"+ New Booking"); b2.setOnClickListener(v->startActivity(new Intent(this, BookingsActivity.class))); root.addView(b2);
        var b3=Ui.button(this,"Customers"); b3.setOnClickListener(v->startActivity(new Intent(this, CustomersActivity.class))); root.addView(b3);
        var b4=Ui.button(this,"Reports & Export"); b4.setOnClickListener(v->startActivity(new Intent(this, ReportsActivity.class))); root.addView(b4);
        root.addView(Ui.subtitle(this, "Beautiful clients. Brighter days. ♡"));
        s.addView(root); setContentView(s);
    }
}
