package com.example.projetws;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projetws.beans.Etudiant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListEtudiant extends AppCompatActivity {

    private ListView listViewEtudiants;
    private RequestQueue requestQueue;
    private ProgressDialog progressDialog;
    private List<String> etudiantsList;
    private ArrayAdapter<String> adapter;

    private static final String LIST_URL = "http://10.0.2.2/projet/ws/loadEtudiant.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_etudiant);

        listViewEtudiants = findViewById(R.id.listViewEtudiants);
        etudiantsList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, etudiantsList);
        listViewEtudiants.setAdapter(adapter);

        requestQueue = Volley.newRequestQueue(this);
        chargerListeEtudiants();
    }

    private void chargerListeEtudiants() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Chargement...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, LIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.d("LIST_RESPONSE", response);

                        try {
                            Type type = new TypeToken<Collection<Etudiant>>() {}.getType();
                            Collection<Etudiant> etudiants = new Gson().fromJson(response, type);

                            if (etudiants != null && !etudiants.isEmpty()) {
                                etudiantsList.clear();
                                for (Etudiant e : etudiants) {
                                    String affichage = e.getId() + " - " + e.getNom() + " " + e.getPrenom() +
                                            " (" + e.getVille() + ") - " + e.getSexe();
                                    etudiantsList.add(affichage);
                                }
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(ListEtudiant.this,
                                        "Aucun étudiant", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.e("LIST_PARSE", "Erreur: " + e.getMessage());
                            Toast.makeText(ListEtudiant.this,
                                    "Erreur de traitement", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Log.e("LIST_ERROR", "Erreur: " + error.getMessage());
                        Toast.makeText(ListEtudiant.this,
                                "Erreur de connexion", Toast.LENGTH_LONG).show();
                    }
                });

        requestQueue.add(stringRequest);
    }
}