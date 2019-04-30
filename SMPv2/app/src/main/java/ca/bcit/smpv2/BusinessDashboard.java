package ca.bcit.smpv2;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Consumer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BusinessDashboard extends AppCompatActivity
{
    public static Business business;
    int defaultPromotionPoints = 0;
    Promotions selectedPromotion;
    Button addBtn;
    Button editBtn;
    Button dltBtn;
    ArrayList<Promotions> usersPromotions;
    PromotionsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        new DatabaseObj(this).getBusinesses("businessID=" + LoginActivity.user.getBusinessID(), (ArrayList<Object> businessObj) -> {
            business = (Business)businessObj.get(0);

            setContentView(R.layout.activity_business_dashboard);

            // Find the toolbar view inside the activity layout
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

            usersPromotions = new ArrayList<Promotions>();
            adapter = new PromotionsAdapter(this, usersPromotions);

            ListView listView = (ListView) findViewById(R.id.lvBusinessPromotions);
            listView.setAdapter(adapter);

            int businessID = LoginActivity.user.getBusinessID();
                new DatabaseObj (BusinessDashboard.this).getPromotions("businessID=" + businessID, (ArrayList<Object> objects)-> {
                    for(Object o : objects) {
                        adapter.add((Promotions) o);
                    }
                    listView.setAdapter(adapter);
            });


            setSupportActionBar(toolbar);
            toolbar.setOverflowIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.baseline_person_black_18dp));

            listView.setOnItemClickListener((parent, view, position, id) ->  {
                    selectedPromotion = usersPromotions.get(position);
            });

            addBtn = findViewById(R.id.addBtn);
            editBtn =  findViewById(R.id.editBtn);
            dltBtn = findViewById(R.id.deleteBtn);

            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAddDialog();
                }
            });

            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showUpdateDialog(selectedPromotion);
                }
            });

            dltBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDeleteDialog(selectedPromotion);
                }
            });
        });


    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_business, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if(item.getItemId() != R.id.dashboard)
            finish();
        switch (item.getItemId()) {
            case R.id.dashboard:
//                Intent i = new Intent(getBaseContext(), BusinessDashboard.class);
//                startActivity(i);
                return true;
            case R.id.analytics:
                Intent j = new Intent(getBaseContext(), Analytics.class);
                startActivity(j);
                return true;
            case R.id.settings:
                Intent i = new Intent(getBaseContext(), BusinessSettingsActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showAddDialog() {
        showUpdateDialog(null);
    }

    private void showDeleteDialog(Promotions promoToDelete) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.delete_promotion_dialogue, null);
        final Button buttonDeletePromotion = dialogView.findViewById(R.id.buttonDeletePromotion);
        final Button buttonCancelDeletePromotion = dialogView.findViewById(R.id.buttonCancelDeletePromotion);

        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("Delete Promotion");

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();


        buttonDeletePromotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO need delete operation in PHP
                new DatabaseObj(BusinessDashboard.this).deletePromotion(promoToDelete.getPromotionID(), null);
                usersPromotions.remove(promoToDelete);
                alertDialog.dismiss();
                ListView listView = (ListView) findViewById(R.id.lvBusinessPromotions);
                listView.setAdapter(adapter);
            }
        });
        buttonCancelDeletePromotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

            }
        });
    }

    private void showUpdateDialog(Promotions updatedPromo) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.add_promotion_dialogue, null);
        //final EditText editTextPromotionPoints = dialogView.findViewById(R.id.editTextPromotionPoints);
        final EditText editTextPromotionDetail = dialogView.findViewById(R.id.editTextPromotionDetails);
        final Button buttonAddPromotion = dialogView.findViewById(R.id.buttonAddPromotion);
        ImageView promoImageView = dialogView.findViewById(R.id.promoImageView);
        Spinner spinner = (Spinner) dialogView.findViewById(R.id.minTier);

        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle((updatedPromo == null) ? "Add Promotion" : "Edit Promotion");

        final AlertDialog alertDialog = dialogBuilder.create();

        new DatabaseObj(this).getTiers("", (ArrayList<Object> objects) -> {
            Collections.sort((ArrayList<PointTiers>)(ArrayList<?>)objects, Comparator.comparingInt(PointTiers::getMinPoints));
            final List<PointTiers> tiers = new ArrayList<>();
            for(Object o : objects)
                tiers.add((PointTiers)o);

            final ArrayAdapter<PointTiers> spinnerArrayAdapter = new ArrayAdapter<>(
                    this, R.layout.spinner_item, tiers);

            spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
            spinner.setAdapter(spinnerArrayAdapter);

            if(updatedPromo != null)
            {
                editTextPromotionDetail.setText(updatedPromo.getDetails());

                for (int i = 0; i < spinnerArrayAdapter.getCount(); i++)
                    if((spinnerArrayAdapter.getItem(i)).getTierID() == updatedPromo.getMinTier().getTierID())
                        spinner.setSelection(i);
            }

            alertDialog.show();
        });


        buttonAddPromotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PointTiers promotionPoints = (PointTiers)spinner.getSelectedItem();
                String promotionDetails = editTextPromotionDetail.getText().toString();
                if (TextUtils.isEmpty(promotionDetails)) {
                    editTextPromotionDetail.setError("Your customer should know what it is you are offering!");
                }

                if(updatedPromo == null)
                {
                    int promoID = -1;
                    int businessID = LoginActivity.user.getBusinessID();
                    Promotions promo = new Promotions(promoID, businessID, promotionPoints, promotionDetails, 0, business.getBusinessName());
                    new DatabaseObj(BusinessDashboard.this).setPromotion(promo, (ArrayList<Object> objects)->{
                        promo.setPromotionID((int)objects.get(0));
                        usersPromotions.add(promo);
                        ListView listView = (ListView) findViewById(R.id.lvBusinessPromotions);
                        listView.setAdapter(adapter);
                    });
                }
                else
                {
                    updatedPromo.setDetails(promotionDetails);
                    updatedPromo.setMinTier(promotionPoints);
                    new DatabaseObj(BusinessDashboard.this).setPromotion(updatedPromo);
                    new DatabaseObj(BusinessDashboard.this).setPromotion(updatedPromo, (ArrayList<Object> objects)->{
                        usersPromotions.add((Promotions)objects.get(0));
                        ListView listView = (ListView) findViewById(R.id.lvBusinessPromotions);
                        listView.setAdapter(adapter);
                    });
                }
                alertDialog.dismiss();
            }
        });
    }
}

