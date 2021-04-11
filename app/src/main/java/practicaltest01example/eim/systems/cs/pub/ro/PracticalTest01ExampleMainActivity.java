package practicaltest01example.eim.systems.cs.pub.ro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PracticalTest01ExampleMainActivity extends AppCompatActivity {

    Button navigateToSecondaryActivityButton;
    EditText leftEditText, rightEditText;
    Button leftButton, rightButton;
    public static final int SERVICE_STARTED = 1;
    public static final int SERVICE_STOPPED = 0;
    int serviceStatus = SERVICE_STOPPED;

    public static final int SECONDARY_ACTIVITY_REQUEST_CODE = 17;
    public static final int MAX_NUMBER_OF_CLICKS_THRESHOLD = 10;

    private MessageBroadcastReceiver messageBroadcastReceiver = new MessageBroadcastReceiver();
    private class MessageBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("Broadcast Receiver", intent.getStringExtra("BR extra"));
        }
    }

    private IntentFilter intentFilter = new IntentFilter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_example_main);

        intentFilter.addAction("Action 1");
        intentFilter.addAction("Action 2");
        intentFilter.addAction("Action 3");

        navigateToSecondaryActivityButton = findViewById(R.id.navigate_to_secondary_activity_button);
        navigateToSecondaryActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int leftClicks = Integer.parseInt(leftEditText.getText().toString());
                int rightClicks = Integer.parseInt(rightEditText.getText().toString());

                Intent navigateToSecondaryActivityIntent = new Intent(PracticalTest01ExampleMainActivity.this, PracticalTest01ExampleSecondaryActivity.class);
                navigateToSecondaryActivityIntent.putExtra("Total clicks", leftClicks + rightClicks);
                startActivityForResult(navigateToSecondaryActivityIntent, SECONDARY_ACTIVITY_REQUEST_CODE);
            }
        });
        leftEditText = findViewById(R.id.left_edit_text);
        rightEditText = findViewById(R.id.right_edit_text);
        leftButton = findViewById(R.id.left_button);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leftEditText.setText(String.valueOf(Integer.parseInt(leftEditText.getText().toString()) + 1));

                int leftClicks = Integer.parseInt(leftEditText.getText().toString());
                int rightClicks = Integer.parseInt(rightEditText.getText().toString());

                if (leftClicks + rightClicks >= MAX_NUMBER_OF_CLICKS_THRESHOLD && serviceStatus == SERVICE_STOPPED) {
                    Intent intent = new Intent(PracticalTest01ExampleMainActivity.this, PracticalTest01ExampleService.class);
                    intent.putExtra("First number", leftClicks);
                    intent.putExtra("Second number", rightClicks);
                    startService(intent);
                    serviceStatus = SERVICE_STARTED;
                }
            }
        });

        rightButton = findViewById(R.id.right_button);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rightEditText.setText(String.valueOf(Integer.parseInt(rightEditText.getText().toString()) + 1));

                int leftClicks = Integer.parseInt(leftEditText.getText().toString());
                int rightClicks = Integer.parseInt(rightEditText.getText().toString());

                if (leftClicks + rightClicks >= MAX_NUMBER_OF_CLICKS_THRESHOLD && serviceStatus == SERVICE_STOPPED) {
                    Intent intent = new Intent(PracticalTest01ExampleMainActivity.this, PracticalTest01ExampleService.class);
                    intent.putExtra("First number", leftClicks);
                    intent.putExtra("Second number", rightClicks);
                    startService(intent);
                    serviceStatus = SERVICE_STARTED;
                }
            }
        });

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("Left clicks")) {
                leftEditText.setText(String.valueOf(savedInstanceState.getInt("Left clicks")));
            } else {
                leftEditText.setText(String.valueOf(0));
            }

            if (savedInstanceState.containsKey("Right clicks")) {
                rightEditText.setText(String.valueOf(savedInstanceState.getInt("Right clicks")));
            } else {
                rightEditText.setText(String.valueOf(0));
            }
        } else {
            leftEditText.setText(String.valueOf(0));
            rightEditText.setText(String.valueOf(0));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(messageBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(messageBroadcastReceiver);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("Left clicks", Integer.parseInt(leftEditText.getText().toString()));
        outState.putInt("Right clicks", Integer.parseInt(rightEditText.getText().toString()));
    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);

        if (savedInstanceState.containsKey("Left clicks")) {
            leftEditText.setText(String.valueOf(savedInstanceState.getInt("Left clicks")));
        } else {
            leftEditText.setText(String.valueOf(0));
        }

        if (savedInstanceState.containsKey("Right clicks")) {
            rightEditText.setText(String.valueOf(savedInstanceState.getInt("Right clicks")));
        } else {
            rightEditText.setText(String.valueOf(0));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SECONDARY_ACTIVITY_REQUEST_CODE) {
            Toast.makeText(PracticalTest01ExampleMainActivity.this, "" + resultCode, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent(PracticalTest01ExampleMainActivity.this, PracticalTest01ExampleService.class);
        stopService(intent);
        super.onDestroy();
    }
}