package com.dreamnails.salon;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import androidx.core.content.FileProvider;
import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReportsActivity extends Activity {
    private DatabaseHelper db;

    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        db = new DatabaseHelper(this);

        ScrollView s = new ScrollView(this);
        LinearLayout r = Ui.root(this);
        r.addView(Ui.title(this, "Reports & Export"));
        r.addView(Ui.subtitle(this, "YOUR BUSINESS, ORGANIZED"));
        r.addView(Ui.card(this, "This Month Sales\nRs. " + String.format(Locale.US, "%.0f", db.salesMonth())));
        r.addView(Ui.card(this, "Total Customers\n" + db.customerCount()));

        Button sales = Ui.button(this, "Download Sales CSV");
        Button customers = Ui.button(this, "Download Customers CSV");
        r.addView(sales);
        r.addView(customers);

        sales.setOnClickListener(v -> exportSales());
        customers.setOnClickListener(v -> exportCustomers());

        r.addView(Ui.card(this, "After export, choose Files, email, WhatsApp, or another app to save or send the report."));
        s.addView(r);
        setContentView(s);
    }

    private void exportSales() {
        try {
            File f = new File(getExternalFilesDir(null), "dream-nails-sales.csv");
            try (PrintWriter w = new PrintWriter(f)) {
                w.println("Customer,Phone,Service,Price,Date,Notes");
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                try (Cursor c = db.sales()) {
                    while (c.moveToNext()) {
                        w.println(csv(c.getString(1)) + "," + csv(c.getString(2)) + "," + csv(c.getString(3)) + "," +
                                c.getDouble(4) + "," + df.format(new Date(c.getLong(5))) + "," + csv(c.getString(6)));
                    }
                }
            }
            share(f, "text/csv");
        } catch (Exception e) {
            Toast.makeText(this, "Could not export sales", Toast.LENGTH_SHORT).show();
        }
    }

    private void exportCustomers() {
        try {
            File f = new File(getExternalFilesDir(null), "dream-nails-customers.csv");
            try (PrintWriter w = new PrintWriter(f)) {
                w.println("Name,Phone,Notes");
                try (Cursor c = db.customers()) {
                    while (c.moveToNext()) {
                        w.println(csv(c.getString(1)) + "," + csv(c.getString(2)) + "," + csv(c.getString(3)));
                    }
                }
            }
            share(f, "text/csv");
        } catch (Exception e) {
            Toast.makeText(this, "Could not export customers", Toast.LENGTH_SHORT).show();
        }
    }

    private String csv(String s) {
        if (s == null) s = "";
        return "\"" + s.replace("\"", "\"\"") + "\"";
    }

    private void share(File f, String type) {
        Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", f);
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType(type);
        i.putExtra(Intent.EXTRA_STREAM, uri);
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(i, "Save or share report"));
    }
}
