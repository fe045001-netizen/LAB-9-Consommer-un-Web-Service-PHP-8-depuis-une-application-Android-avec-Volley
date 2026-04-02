
package com.example.projetws;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnAjouter, btnListe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAjouter = findViewById(R.id.btnAjouter);
        btnListe = findViewById(R.id.btnListe);

        btnAjouter.setOnClickListener(this);
        btnListe.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnAjouter) {
            // Redirection vers la page d'ajout
            Intent intent = new Intent(this, AddEtudiant.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btnListe) {
            // Redirection vers la page de liste
            Intent intent = new Intent(this, ListEtudiant.class);
            startActivity(intent);
        }
    }
}