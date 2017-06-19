package com.example.yzhou.customview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private YJMoneyView moneyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        moneyView = (YJMoneyView) findViewById(R.id.special_items_money2);

        moneyView.setText(123.34d);
    }
}
