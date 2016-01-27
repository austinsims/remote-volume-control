package name.austinsims.remotevolumecontrol;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    static final int PORT = 8001;
    static final String PREFS_NAME = "remote-volume-control";
    static final String KEY_IP_ADDRESS = "ip";

    private TextView ipAddressField;
    private Button volumeUpButton;
    private Button volumeDownButton;
    private Button toggleMuteButton;
    private SharedPreferences prefs;

    private String getHostName() {
        return ipAddressField.getText().toString();
    }

    private class CommandTask extends AsyncTask<Character, Void, Void> {
        protected Void doInBackground(Character... chars) {
            try (
                    Socket socket = new Socket(getHostName(), PORT);
                    PrintWriter out =
                            new PrintWriter(socket.getOutputStream(), true);
            ) {
                out.print(chars[0]);
                out.flush();
            } catch (Exception e) {
                toast(e);
            }
            return null;
        }
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ipAddressField = (TextView) findViewById(R.id.ipAddressField);
        volumeUpButton = (Button) findViewById(R.id.volumeUpButton);
        volumeDownButton = (Button) findViewById(R.id.volumeDownButton);
        toggleMuteButton = (Button) findViewById(R.id.toggleMuteButton);

        prefs = getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String savedIpAddressOrEmptyString = prefs.getString(KEY_IP_ADDRESS, "");
        ipAddressField.setText(savedIpAddressOrEmptyString);

        volumeUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveIpAddress();
                new CommandTask().execute(Character.valueOf('u'));
            }
        });

        volumeDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveIpAddress();
                new CommandTask().execute(Character.valueOf('d'));
            }
        });

        toggleMuteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveIpAddress();
                new CommandTask().execute(Character.valueOf('m'));
            }
        });
    }

    private void  saveIpAddress() {
        String ipAddressString = ipAddressField.getText().toString();
        prefs.edit()
                .putString(KEY_IP_ADDRESS, ipAddressString)
                .apply();
    }

    private void toast(Exception ex) {
        String exName = ex.getClass().getName();
        String exMessage = ex.getMessage();
        String toastMessage = exName + (exMessage == null ? "" : ": " + exMessage);

        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, toastMessage, Toast.LENGTH_LONG);
        toast.show();
    }
}

