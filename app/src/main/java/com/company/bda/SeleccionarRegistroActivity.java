package com.company.bda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SeleccionarRegistroActivity extends AppCompatActivity {

    private Button botonDonante, botonReceptor;
    private TextView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_registro);

        botonDonante = findViewById(R.id.donorButton);
        botonReceptor = findViewById(R.id.recipientButton);
        backButton = findViewById(R.id.backButton);


        botonDonante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SeleccionarRegistroActivity.this, RegistroDonanteActivity.class);
                startActivity(intent);
            }
        });

        botonReceptor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SeleccionarRegistroActivity.this, RegistroReceptorActivity.class);
                startActivity(intent);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SeleccionarRegistroActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }
}