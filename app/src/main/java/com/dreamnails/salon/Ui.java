package com.dreamnails.salon;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public final class Ui {
    public static final int ROSE = Color.rgb(217, 135, 127);
    public static final int ROSE_DARK = Color.rgb(184, 95, 89);
    public static final int CREAM = Color.rgb(255, 249, 247);
    public static final int BLUSH = Color.rgb(247, 231, 228);
    public static final int INK = Color.rgb(34, 29, 29);
    public static final int MUTED = Color.rgb(127, 107, 104);

    public static int dp(Context c, int v) {
        return Math.round(v * c.getResources().getDisplayMetrics().density);
    }

    public static LinearLayout root(Context c) {
        LinearLayout l = new LinearLayout(c);
        l.setOrientation(LinearLayout.VERTICAL);
        l.setPadding(dp(c, 20), dp(c, 20), dp(c, 20), dp(c, 28));
        l.setBackgroundColor(CREAM);
        return l;
    }

    public static TextView title(Context c, String text) {
        TextView t = new TextView(c);
        t.setText(text);
        t.setTextColor(INK);
        t.setTextSize(28);
        t.setTypeface(Typeface.create("serif", Typeface.NORMAL));
        t.setGravity(Gravity.CENTER);
        t.setPadding(0, dp(c, 8), 0, dp(c, 4));
        return t;
    }

    public static TextView subtitle(Context c, String text) {
        TextView t = new TextView(c);
        t.setText(text);
        t.setTextColor(ROSE_DARK);
        t.setTextSize(13);
        t.setGravity(Gravity.CENTER);
        t.setLetterSpacing(0.15f);
        t.setPadding(0, 0, 0, dp(c, 18));
        return t;
    }

    public static TextView section(Context c, String text) {
        TextView t = new TextView(c);
        t.setText(text);
        t.setTextColor(INK);
        t.setTextSize(18);
        t.setTypeface(Typeface.DEFAULT_BOLD);
        t.setPadding(0, dp(c, 14), 0, dp(c, 8));
        return t;
    }

    public static EditText input(Context c, String hint) {
        EditText e = new EditText(c);
        e.setHint(hint);
        e.setTextSize(16);
        e.setSingleLine(true);
        e.setPadding(dp(c, 14), 0, dp(c, 14), 0);
        GradientDrawable g = new GradientDrawable();
        g.setColor(Color.WHITE);
        g.setCornerRadius(dp(c, 14));
        g.setStroke(dp(c, 1), Color.rgb(236, 210, 207));
        e.setBackground(g);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(c, 52));
        p.setMargins(0, 0, 0, dp(c, 10));
        e.setLayoutParams(p);
        return e;
    }

    public static Button button(Context c, String text) {
        Button b = new Button(c);
        b.setText(text);
        b.setTextColor(Color.WHITE);
        b.setTextSize(15);
        b.setAllCaps(false);
        GradientDrawable g = new GradientDrawable();
        g.setColor(ROSE_DARK);
        g.setCornerRadius(dp(c, 16));
        b.setBackground(g);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(c, 52));
        p.setMargins(0, dp(c, 6), 0, dp(c, 6));
        b.setLayoutParams(p);
        return b;
    }

    public static TextView card(Context c, String text) {
        TextView t = new TextView(c);
        t.setText(text);
        t.setTextColor(INK);
        t.setTextSize(16);
        t.setPadding(dp(c, 16), dp(c, 14), dp(c, 16), dp(c, 14));
        GradientDrawable g = new GradientDrawable();
        g.setColor(Color.WHITE);
        g.setCornerRadius(dp(c, 16));
        g.setStroke(dp(c, 1), Color.rgb(242, 220, 217));
        t.setBackground(g);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        p.setMargins(0, 0, 0, dp(c, 10));
        t.setLayoutParams(p);
        return t;
    }
}
