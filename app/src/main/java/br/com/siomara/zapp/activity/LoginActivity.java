package br.com.siomara.zapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.util.HashMap;
import java.util.Random;

import br.com.siomara.zapp.R;
import br.com.siomara.zapp.util.Permission;
import br.com.siomara.zapp.util.PreferenceForToken;

public class LoginActivity extends AppCompatActivity {

    private EditText    edtName;
    private EditText    edtCountryCode;
    private EditText    edtAreaCode;
    private EditText    edtTelephone;
    private Button      btnRegister;
    private String[]    neededPermissions = new String[] {
            android.Manifest.permission.SEND_SMS,
            android.Manifest.permission.INTERNET
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Permission.validatePermission(1, this, neededPermissions );

        edtName         = findViewById(R.id.edtName);
        edtCountryCode  = findViewById(R.id.edtCountryCode);
        edtAreaCode     = findViewById(R.id.edtAreaCode);
        edtTelephone    = findViewById(R.id.edtTelephone);
        btnRegister     = findViewById(R.id.btnLogin);

        // Define masks for country code / area code / telephone
        SimpleMaskFormatter simpleMaskCountryCode   = new SimpleMaskFormatter("+NN");
        SimpleMaskFormatter simpleMaskAreaCode      = new SimpleMaskFormatter("NN");
        SimpleMaskFormatter simpleMaskTelephone     = new SimpleMaskFormatter("NNNNN-NNNN");

        MaskTextWatcher maskCountryCode = new MaskTextWatcher(edtCountryCode, simpleMaskCountryCode);
        MaskTextWatcher maskAreaCode    = new MaskTextWatcher(edtAreaCode, simpleMaskAreaCode);
        MaskTextWatcher maskTelephone   = new MaskTextWatcher(edtTelephone, simpleMaskTelephone);

        edtCountryCode.addTextChangedListener(maskCountryCode);
        edtAreaCode.addTextChangedListener(maskAreaCode);
        edtTelephone.addTextChangedListener(maskTelephone);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtName.getText().toString();

                // Remove all mask characters from telephone
                String telephone= edtCountryCode.getText().toString()
                        + edtAreaCode.getText().toString()
                        + edtTelephone.getText().toString();
                telephone = telephone.replace("+", "");
                telephone = telephone.replace("-", "");
                Log.i("TELEPHONE", ": " + telephone);

                // Generate validation code with 4 digits on the client side (just for learning)
                // The token generation process is better if performed on the server side
                // (9999 - 1000) = 8999 - generates number from 0 to 8999
                // + 1000 - guarantees number with 4 digits from 1000 to 9999
                Random random = new Random();
                int randomNumber = random.nextInt(9999 - 1000) + 1000;
                String validationCode = String.valueOf(randomNumber);
                Log.i("VALIDATION_CODE", ": " + validationCode);

                // Save data for validation on the mobile device
                //Preference preference = new Preference(getApplicationContext()); // or line bellow
                PreferenceForToken preference = new PreferenceForToken(LoginActivity.this);
                preference.saveUserPreferences(username, telephone, validationCode);

                // Send validation code SMS
                //telephone = "+5561992826332";
                telephone = "1212";
                String message = "Zapp validation code " + validationCode;
                boolean smsStatus = sendSMS("+" + telephone, message ); // + is mandatory

                if (smsStatus) {
                    Intent intent = new Intent( LoginActivity.this, ValidationActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Problema ao enviar SMS. Tente novamente.", Toast.LENGTH_LONG).show();
                }


                HashMap<String, String> user = preference.getUserPreferences();
                Log.i("USERNAME", ": " + user.get("username"));
                Log.i("TELEPHONE", ": " + user.get("telephone"));
                Log.i("VALIDATION_CODE", ": " + user.get("validation_code"));
                Log.i("USERDATA", ": " + user.get("username") + " / " + user.get("telephone") + " / " + user.get("validation_code"));

            }
        });
    }

    private boolean sendSMS(String telephone, String message) {

        try {
            SmsManager smsManager = SmsManager.getDefault(); // get class instance
            smsManager.sendTextMessage(telephone, null, message, null, null);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    // Verifica se alguma permissão foi negada
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int result : grantResults) {
            if ( result == PackageManager.PERMISSION_DENIED ) {
                alertPermissionValidation();
            }
        }
    }

    // Display mensagem informando que é necessário aceitar
    // determinada permissão para usar o aplicativo
    private void alertPermissionValidation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões Negadas");
        builder.setMessage("Para usar esse app é necessário aceitar as permissões");
        builder.setPositiveButton("Retornar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
