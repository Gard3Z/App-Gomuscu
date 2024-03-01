package com.example.gomuscu;

import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.LinearLayout;

import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

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
        // Récupérer les détails de tous les exercices pour la séance donnée
        List<ExoDetails> exoDetailsList = db.getExoDetailsForSeance(seance.getIdSeance());

        // Déterminer le type de séance en fonction du type_exo du premier exercice
        String typeSeance;
        if (!exoDetailsList.isEmpty()) {
            int typeSeanceInt = exoDetailsList.get(0).getTypeExo();
            typeSeance = (typeSeanceInt == 1) ? "Haut du corps" : ((typeSeanceInt == 0) ? "Bas du corps" : "Type inconnu");
        } else {
            // Ajustez le texte de l'en-tête si la liste d'exercices est vide
            typeSeance = "Type inconnu";
        }

        // Utilisez le type de séance dans le texte du bouton
        button.setText("Type de séance : " + typeSeance + " \n Date : " + seance.getDate());

        // Ajouter un espacement entre les lignes (par exemple, 8 pixels)
        float lineSpacingExtra = 16; // Choisissez la valeur d'espacement souhaitée en pixels
        button.setLineSpacing(lineSpacingExtra, 1); // 1.0f pour le facteur de multiplication (pas de changement)

        // Définissez la taille du texte en sp (scale-independent pixels)
        float textSizeSP = 16; // Choisissez la taille souhaitée en SP
        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSizeSP);

        // Changer la couleur du texte
        int textColor = ContextCompat.getColor(this, R.color.secondary); // Remplacez "votre_couleur" par la couleur souhaitée
        button.setTextColor(textColor);

        // Ajoutez du padding au bouton (left, top, right, bottom) en dp
        int paddingInDp = 4; // Choisissez la taille souhaitée en dp
        button.setPadding(paddingInDp, paddingInDp, paddingInDp, paddingInDp);

        // Définir la largeur et la hauteur du bouton en dp
        int buttonWidthInDp = 300; // Choisissez la largeur souhaitée en dp
        int buttonHeightInDp = 100; // Choisissez la hauteur souhaitée en dp

        // Convertir dp en pixels
        int buttonWidthInPixels = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                buttonWidthInDp,
                getResources().getDisplayMetrics()
        );

        int buttonHeightInPixels = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                buttonHeightInDp,
                getResources().getDisplayMetrics()
        );

        // Définir les paramètres de mise en page du bouton
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                buttonWidthInPixels,
                buttonHeightInPixels
        );

        // Définir la marge supérieure en dp
        int marginTopInDp = 16; // Choisissez la marge supérieure souhaitée en dp

        // Convertir dp en pixels
        int marginTopInPixels = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                marginTopInDp,
                getResources().getDisplayMetrics()
        );

        layoutParams.setMargins(0, marginTopInPixels, 0, 0); // Marges (left, top, right, bottom)

        // Appliquer les paramètres de mise en page au bouton
        button.setLayoutParams(layoutParams);

        // Définir l'arrière-plan personnalisé
        // Remplacez R.drawable.votre_bouton_drawable par la référence appropriée de votre arrière-plan
        button.setBackgroundResource(R.drawable.button_custom);

        button.setBackgroundResource(R.drawable.button_custom);





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
    public void showBottomSheetDialog(SeanceDetails seance) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_layout, null);

        TableLayout tableLayout = bottomSheetView.findViewById(R.id.tableLayout);

        // En-tête de la séance
        TextView seanceHeader = bottomSheetView.findViewById(R.id.seanceHeader);

        // Récupérer les détails de tous les exercices pour la séance donnée
        List<ExoDetails> exoDetailsList = db.getExoDetailsForSeance(seance.getIdSeance());

        // Couleur du texte pour les données dynamiques
        int textColor = ContextCompat.getColor(this, R.color.secondary);

        // Afficher les noms, poids et séries des exercices dans le BottomSheet
        for (ExoDetails exoDetails : exoDetailsList) {
            TableRow tableRow = new TableRow(this);

            // Exercice
            TextView exerciceTextView = createTextView(exoDetails.getNomExo(), Gravity.START);
            exerciceTextView.setTextColor(textColor);
            tableRow.addView(exerciceTextView);

            // Poids
            TextView poidsTextView = createTextView(String.valueOf(exoDetails.getPoids()+ " Kg"), Gravity.CENTER);
            poidsTextView.setTextColor(textColor);
            tableRow.addView(poidsTextView);

            // Série
            TextView serieTextView = createTextView(String.valueOf(exoDetails.getSerie()), Gravity.CENTER);
            serieTextView.setTextColor(textColor);
            tableRow.addView(serieTextView);

            // Ajouter la ligne au tableau
            tableLayout.addView(tableRow);
        }
        int typeSeanceInt = exoDetailsList.get(0).getTypeExo();
        String typeSeance;

        if (typeSeanceInt == 1) {
            typeSeance = "Haut du corps";
        } else if (typeSeanceInt == 0) {
            typeSeance = "Bas du corps";
        } else {
            // Si la valeur n'est ni 0 ni 1, vous pouvez définir un autre type ou une valeur par défaut
            typeSeance = "Type inconnu";
        }

        seanceHeader.setText("Type de séance : " + typeSeance + " | Date : " + seance.getDate());

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    private TextView createTextView(String text, int gravity) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setPadding(8, 8, 8, 8);

        // Définir la gravité du texte dans la colonne
        textView.setGravity(gravity);

        return textView;
    }


    public void OnclickLogo(View view) {
        Utilitaire.performVibration(this);
        this.finish();
    }
}