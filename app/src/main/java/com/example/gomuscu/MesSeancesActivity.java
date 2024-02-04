package com.example.gomuscu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;

import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.LinearLayout;

import android.widget.TextView;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.HashMap;
import java.util.List;

public class MesSeancesActivity extends AppCompatActivity {

    private DatabaseAcess db;
    private LinearLayout containerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mes_seances);

        db = DatabaseAcess.getInstance(getApplicationContext());
        db.open();

        containerLayout = findViewById(R.id.containerLayout2); // Utilisez le même ID que dans votre XML

        // Récupérez toutes les séances
        List<SeanceDetails> seancesList = db.getAllSeances();

        // Créez des boutons pour chaque séance
        for (final SeanceDetails seance : seancesList) {
            addButton(seance);
        }

        //db.close();
    }

    private void addButton(final SeanceDetails seance) {
        Button button = new Button(this);
        button.setText("Seance ID: " + seance.getIdSeance() + " | Date: " + seance.getDate());
        //button.setText("Seance : Haut du corps  |  Date: " + seance.getDate());

        // Définissez la taille du texte en sp (scale-independent pixels)
        float textSizeSP = 12; // Choisissez la taille souhaitée en SP
        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSizeSP);

        // Ajoutez du padding au bouton (left, top, right, bottom) en dp
        int paddingInDp = 4; // Choisissez la taille souhaitée en dp
        button.setPadding(paddingInDp, paddingInDp, paddingInDp, paddingInDp);

        // Définissez la hauteur et la largeur du bouton en dp
        int buttonWidthInDp = 300; // Choisissez la largeur souhaitée en dp
        int buttonHeightInDp = 100; // Choisissez la hauteur souhaitée en dp
        setButtonSize(button, buttonWidthInDp, buttonHeightInDp);


        // Ajoutez un écouteur de clic pour gérer l'action lorsqu'un bouton est cliqué
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetDialog(seance);
                // Gérez l'action lorsqu'un bouton est cliqué
                // Vous pouvez lancer une nouvelle activité, afficher des détails, etc.
                // Par exemple, vous pouvez afficher les détails de la séance dans un autre TextView
                // en fonction de l'ID de la séance.
            }
        });

        // Ajoutez le bouton au conteneur
        containerLayout.addView(button);

        // Ajoutez d'autres paramètres de mise en page ou de style selon vos besoins
    }
    private void setButtonSize(Button button, int widthInDp, int heightInDp) {
        // Convertissez la largeur et la hauteur du bouton en pixels
        float scale = getResources().getDisplayMetrics().density;
        int widthInPixels = (int) (widthInDp * scale);
        int heightInPixels = (int) (heightInDp * scale);

        // Créez un nouvel objet LayoutParams avec les dimensions spécifiées
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(widthInPixels, heightInPixels);

        // Appliquez les paramètres de mise en page au bouton
        button.setLayoutParams(layoutParams);
    }
    public void showBottomSheetDialog(SeanceDetails seanceDetails) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_layout, null);

        TextView nomExoTextView = bottomSheetView.findViewById(R.id.nom_exo);

        // Récupérer les détails de l'exercice pour la séance donnée
        ExoDetails exoDetails = db.getExoDetailsForSeance(seanceDetails.getIdSeance());

        // Afficher le nom de l'exercice dans le BottomSheet
        if (exoDetails != null) {
            nomExoTextView.setText(exoDetails.getNomExo());
        }

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }



    public void OnclickLogo(View view) {
        this.finish();
    }
}