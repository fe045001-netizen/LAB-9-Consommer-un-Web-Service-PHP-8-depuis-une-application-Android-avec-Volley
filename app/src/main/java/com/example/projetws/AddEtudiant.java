package com.example.projetws;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projetws.beans.Etudiant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AddEtudiant extends AppCompatActivity implements View.OnClickListener {

    private EditText editNom, editPrenom;
    private Spinner spinnerVille;
    private RadioButton radioHomme, radioFemme;
    private Button btnAdd;
    private RequestQueue requestQueue;
    private ProgressDialog progressDialog;

    // Vérifiez que cette URL est correcte
    private static final String INSERT_URL = "http://10.0.2.2/projet/ws/createEtudiant.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_etudiant);

        editNom = findViewById(R.id.nom);
        editPrenom = findViewById(R.id.prenom);
        spinnerVille = findViewById(R.id.ville);
        radioHomme = findViewById(R.id.homme);
        radioFemme = findViewById(R.id.femme);
        btnAdd = findViewById(R.id.add);

        // Initialisation de Volley
        requestQueue = Volley.newRequestQueue(this);
        btnAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add) {
            ajouterEtudiant();
        }
    }

    private void ajouterEtudiant() {
        String nom = editNom.getText().toString().trim();
        String prenom = editPrenom.getText().toString().trim();

        // Validation des champs
        if (nom.isEmpty()) {
            editNom.setError("Nom requis");
            editNom.requestFocus();
            return;
        }

        if (prenom.isEmpty()) {
            editPrenom.setError("Prénom requis");
            editPrenom.requestFocus();
            return;
        }

        if (!radioHomme.isChecked() && !radioFemme.isChecked()) {
            Toast.makeText(this, "Sélectionnez un sexe", Toast.LENGTH_SHORT).show();
            return;
        }

        // Afficher le dialogue de progression
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Ajout en cours...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, INSERT_URL,
                response -> {
                    progressDialog.dismiss();
                    Log.d("VOLLEY_RESPONSE", response);

                    try {
                        Type type = new TypeToken<Collection<Etudiant>>() {}.getType();
                        Collection<Etudiant> etudiants = new Gson().fromJson(response, type);

                        if (etudiants != null && !etudiants.isEmpty()) {
                            Toast.makeText(AddEtudiant.this,
                                    "Étudiant ajouté avec succès!", Toast.LENGTH_LONG).show();
                            finish(); // Retour à l'accueil
                        } else {
                            Toast.makeText(AddEtudiant.this,
                                    "Erreur lors de l'ajout", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e("VOLLEY_PARSE", "Erreur: " + e.getMessage());
                        Toast.makeText(AddEtudiant.this,
                                "Erreur de traitement", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    progressDialog.dismiss();
                    Log.e("VOLLEY_ERROR", "Erreur: " + error.getMessage());

                    String message = "Erreur de connexion";
                    if (error instanceof TimeoutError) {
                        message = "Délai d'attente dépassé. Vérifiez que le serveur est démarré.";
                    } else if (error.networkResponse == null) {
                        message = "Impossible de se connecter au serveur.\n" +
                                "Vérifiez que XAMPP/WAMP est démarré\n" +
                                "et que l'URL est correcte: " + INSERT_URL;
                    }

                    Toast.makeText(AddEtudiant.this, message, Toast.LENGTH_LONG).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                String sexe = radioHomme.isChecked() ? "homme" : "femme";
                Map<String, String> params = new HashMap<>();
                params.put("nom", nom);
                params.put("prenom", prenom);
                params.put("ville", spinnerVille.getSelectedItem().toString());
                params.put("sexe", sexe);
                return params;
            }
        };

        // Configurer les timeouts pour éviter le blocage
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,  // 15 secondes de timeout
                0,      // Pas de réessai
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(stringRequest);
    }
}