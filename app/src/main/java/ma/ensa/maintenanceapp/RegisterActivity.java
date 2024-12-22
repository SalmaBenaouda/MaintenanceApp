package ma.ensa.maintenanceapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText fullNameInput, emailInput, passwordInput, phoneInput;
    Spinner specialitySpinner;
    Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fullNameInput = findViewById(R.id.fullNameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        phoneInput = findViewById(R.id.phoneInput);
        specialitySpinner = findViewById(R.id.specialitySpinner);
        registerButton = findViewById(R.id.registerButton);

        DatabaseHelper dbHelper = new DatabaseHelper(this);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = fullNameInput.getText().toString();
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();
                String phone = phoneInput.getText().toString();
                String speciality = specialitySpinner.getSelectedItem().toString();

                if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                } else {
                    // Insertion dans la table Professionnel
                    boolean isInserted = dbHelper.insertProfessionnel(fullName, email, password, phone, speciality);
                    if (isInserted) {
                        Toast.makeText(RegisterActivity.this, "Inscription réussie !", Toast.LENGTH_SHORT).show();
                        finish(); // Retour à l'activité précédente
                    } else {
                        Toast.makeText(RegisterActivity.this, "Erreur : Cet email existe déjà.", Toast.LENGTH_SHORT).show();
                        Log.e("DB_ERROR", "Échec de l'insertion dans la base de données.");
                    }
                }
            }
        });
    }
}
