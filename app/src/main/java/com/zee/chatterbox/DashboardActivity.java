package com.zee.chatterbox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class DashboardActivity extends AppCompatActivity implements ValueEventListener {

    Firebase myFirebaseRef;

    EditText edMsg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Firebase.setAndroidContext(this);

        myFirebaseRef = new Firebase("https://telepathy.firebaseio.com/"); // Change this to your app url
        myFirebaseRef.child("message").addValueEventListener(this);

        initViews();
    }

    private void initViews() {

        edMsg = (EditText) findViewById(R.id.edMsg);
        edMsg.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    sendMessage();
                    handled = true;
                }
                return handled;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void onPushPressed(View v)
    {
        sendMessage();
    }

    public void onPullPressed(View v)
    {

        //To pull the message on button press call "onDataChange" method here
    }

    private void sendMessage()
    {

        myFirebaseRef.child("message").setValue(edMsg.getText().toString());
        edMsg.setText("");
        Toast.makeText(this, "Pushed messaged!", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        System.out.println(dataSnapshot.getValue());  //prints "Do you have data? You'll love Firebase."
        String snapshotStr =  dataSnapshot.toString();
        String msg = (String) dataSnapshot.getValue();
        //Gson gson = new Gson();
        //Message msg = gson.fromJson(dataSnapshot.getValue().toString(), Message.class);
        Toast.makeText(DashboardActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {

    }
}
