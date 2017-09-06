package com.example.kzhan.zhang_kevin_shopify_winternship_2018;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.NumberFormat;

/**
 * Created by kzhan on 2017-09-06.
 */

public class SearchResultActivity extends AppCompatActivity {
    private TextView mPersonHeader;
    private TextView mItemHeader;
    private TextView mPersonResult;
    private TextView mItemResult;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        mPersonHeader = (TextView) findViewById(R.id.user_result_header);
        mItemHeader = (TextView) findViewById(R.id.item_result_header);
        mPersonResult = (TextView) findViewById(R.id.user_result_text);
        mItemResult = (TextView) findViewById(R.id.item_result_text);
        mButton = (Button) findViewById(R.id.back_button);

        Intent intent = getIntent();
        String personName = intent.getStringExtra(MainActivity.PERSON_NAME);
        String itemName = intent.getStringExtra(MainActivity.ITEM_NAME);
        float personTotal = intent.getFloatExtra(MainActivity.PERSON_SEARCH, 0);
        int itemQuantity = intent.getIntExtra(MainActivity.ITEM_SEARCH, 0);

        if (personName.length() == 0) {
            mPersonHeader.setText(getString(R.string.no_text));
        } else {
            mPersonHeader.setText(personName);
            NumberFormat formatter = NumberFormat.getCurrencyInstance();
            String resultString = "Total spent: " + formatter.format(personTotal);
            mPersonResult.setText(resultString);
        }

        if (itemName.length() == 0) {
            mItemHeader.setText(getString(R.string.no_text));
        } else {
            mItemHeader.setText(itemName);
            String resultString = "Total purchased: " + String.valueOf(itemQuantity);
            mItemResult.setText(resultString);
        }

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}

