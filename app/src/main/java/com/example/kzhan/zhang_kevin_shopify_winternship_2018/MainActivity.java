package com.example.kzhan.zhang_kevin_shopify_winternship_2018;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Context mContext = this;
    private EditText mUserField;
    private EditText mItemField;
    private Button mButton;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUserField = (EditText) findViewById(R.id.user_edit_text);
        mItemField = (EditText) findViewById(R.id.item_edit_text);
        mButton = (Button) findViewById(R.id.submit_button);
        mProgressBar = (ProgressBar) findViewById(R.id.main_progress_bar);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isValid()) {
                    Toast.makeText(mContext, getString(R.string.error_no_input), Toast.LENGTH_SHORT).show();
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    /**
     * @return True if some input has been typed for either edit text, false otherwise
     */
    private boolean isValid() {
        return mUserField.getText().toString().length() > 0 || mItemField.getText().toString().length() > 0;
    }
}
