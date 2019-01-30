package br.com.siomara.zapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.util.HashMap;

import br.com.siomara.zapp.R;
import br.com.siomara.zapp.util.PreferenceForToken;

// Class to validate the telephone informed by the user
public class ValidationActivity extends AppCompatActivity {

    private EditText    edtValidationCode;
    private Button      btnValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation);

        edtValidationCode =  findViewById(R.id.edtValidationCode);
        btnValidation = findViewById(R.id.btnValidation);

        // Define masks for validation code - only number
        SimpleMaskFormatter simpleMaskValidationCode = new SimpleMaskFormatter("NNNNN");
        MaskTextWatcher maskValidationCode   = new MaskTextWatcher(edtValidationCode, simpleMaskValidationCode);
        edtValidationCode.addTextChangedListener(maskValidationCode);

        btnValidation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Recupera dados das preferencias do usuario
                PreferenceForToken preference = new PreferenceForToken(ValidationActivity.this);

                HashMap<String, String> usuario = preference.getUserPreferences();
                String createdToken = usuario.get("validation_code");
                String typedToken = edtValidationCode.getText().toString();

                if (typedToken.equals(createdToken)) {
                    Toast.makeText(ValidationActivity.this, "Token Válido!!!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ValidationActivity.this, "Token Inválido", Toast.LENGTH_SHORT).show();

                }

            }
        });


    }
}
