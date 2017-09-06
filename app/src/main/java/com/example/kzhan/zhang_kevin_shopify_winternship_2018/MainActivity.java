package com.example.kzhan.zhang_kevin_shopify_winternship_2018;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    public static final String PERSON_NAME = "PERSON_NAME";
    public static final String ITEM_NAME = "ITEM_NAME";
    public static final String PERSON_SEARCH = "USER_SEARCH";
    public static final String ITEM_SEARCH = "ITEM_SEARCH";

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

        mUserField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return mItemField.requestFocus();
            }
        });

        mItemField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mItemField.getWindowToken(), 0);
                return findViewById(R.id.main_activity_layout).requestFocus();
            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isValid()) {
                    cancelLoading();
                } else {
                    final String person = mUserField.getText().toString();
                    final String item = mItemField.getText().toString();
                    mProgressBar.setVisibility(View.VISIBLE);
                    ConnectionClient.getInstance().getData(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                            cancelLoading();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException{
                            float userTotal = 0f;
                            int itemQuantity = 0;
                            try {
                                JSONObject serverResponse = new JSONObject(response.body().string());
                                JSONArray orders = serverResponse.getJSONArray("orders");
                                // iterate through each order
                                for (int i = 0; i<orders.length(); i++) {
                                    JSONObject order = orders.getJSONObject(i);

                                    // Check for search by person
                                    if (person.length() > 0 && isOrderedBy(order, person)) {
                                        String sOrderTotal = order.getString("total_price");
                                        if (sOrderTotal != null) {
                                            float fOrderTotal = Float.parseFloat(sOrderTotal);
                                            userTotal += fOrderTotal;
                                        }
                                    }

                                    // Check for search by item
                                    if (item.length() > 0) {
                                        itemQuantity += numItems(order, item);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                cancelLoading();
                            }
                            Intent intent = new Intent(mContext, SearchResultActivity.class);
                            intent.putExtra(PERSON_SEARCH, userTotal);
                            intent.putExtra(ITEM_SEARCH, itemQuantity);
                            intent.putExtra(PERSON_NAME, person);
                            intent.putExtra(ITEM_NAME, item);
                            startActivity(intent);
                        }
                    });
                }
            }
        });
    }

    /**
     * Hide loading screen when returning to this activity
     */
    @Override
    protected void onResume() {
        super.onResume();
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    /**
     * Function to hide loading animation and display toast error message
     */
    private void cancelLoading() {
        ConnectionClient.getInstance().cancel();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(mContext, getString(R.string.error_no_input), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * @return True if some input has been typed for either edit text, false otherwise
     */
    private boolean isValid() {
        return mUserField.getText().toString().length() > 0 || mItemField.getText().toString().length() > 0;
    }

    /**
     * Looks through order object to identify if it is ordered by the correct person
     * @param order - Order JSONObject
     * @param person - Person's name to filter by (Must be full name)
     * @return - true if match, false otherwise
     * @throws JSONException
     */
    private boolean isOrderedBy(JSONObject order, String person) throws JSONException{
        if (order.has("billing_address")) {
            JSONObject billing = order.getJSONObject("billing_address");
            if (billing.has("name")) {
                String name = billing.getString("name");
                if (name.equalsIgnoreCase(person)) {
                    return true;
                }
            }
        }
        return false;
    }

    private int numItems(JSONObject order, String item) throws JSONException{
        int numTotal = 0;
        JSONArray items = order.getJSONArray("line_items");
        if (items != null) {
            for (int i = 0; i<items.length(); i++ ){
                JSONObject object = items.getJSONObject(i);
                if (object != null && object.has("title") && object.has("quantity")) {
                    if (object.getString("title").equalsIgnoreCase(item)) {
                        numTotal += object.getInt("quantity");
                    }
                }
            }
        }

        return numTotal;
    }
}
