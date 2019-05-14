package ca.bcit.smpv2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Consumer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    SearchView searchView;
    ListView listView;
    PromotionsAdapter newAdapter;
    ImageView promoImage;
    TextView promoTitle;
    TextView promoDetails;
    private ArrayList<Pair<Promotions, Boolean>> usersPromotions;
    private ArrayList<Pair<Promotions, Boolean>> newusersPromotions;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        searchView = findViewById(R.id.searchView);
        // Find the toolbar view inside the activity layout
        toolbar = (Toolbar) findViewById
                (R.id.toolbar);
        setSupportActionBar(toolbar);

        // Construct the data source, maybe construct arraylist beforehand
        usersPromotions = new ArrayList<Pair<Promotions, Boolean>>();
        //final PromotionsAdapter preferredAdapter = new PromotionsAdapter(this, usersPromotions);
        final PromotionsAdapter adapter = new PromotionsAdapter(this, usersPromotions);
        listView = (ListView) findViewById(R.id.lvPromotions);
        new DatabaseObj (DashboardActivity.this).getApplicablePromotions(LoginActivity.user.getUserID(), (ArrayList<Object> promotions)-> {
            ArrayList<Business> preferredBusinesses = new ArrayList<>();
            new DatabaseObj(DashboardActivity.this)
                    .getPreferredBusinesses("businessID IN (SELECT businessID FROM superpoints.PreferredBusinesses WHERE userID = "
                            + DatabaseObj.SQLSafe(LoginActivity.user.getUserID()) + " )",(ArrayList<Object> preferredBusineses)->{
                for(Object preferredBusiness: preferredBusineses)
                    preferredBusinesses.add((Business) preferredBusiness);
                Collections.sort((ArrayList<Promotions>) (ArrayList<?>) promotions,
                        (Promotions p1, Promotions p2) -> p1.getBusinessName().compareToIgnoreCase(p2.getBusinessName()));
                //Array to hold all promotions that are preferred to be deleted from original list later to append other promotions
                ArrayList<Promotions> preferredPromotions = new ArrayList<>();
                for(Object promotion : promotions)
                {
                    Promotions currentPromotion = (Promotions) promotion;
                    for(int i = 0; i < preferredBusinesses.size(); ++i)
                    {
                        //TODO optimize?
                        if(currentPromotion.getBusinessID() == preferredBusinesses.get(i).getBusinessID())
                        {
                            adapter.add(new Pair<Promotions, Boolean>(currentPromotion, true));
                            preferredPromotions.add(currentPromotion);
                            break;
                        }
                    }
                }
                //promotions.add(new Promotions)
                //All promotions left will not be preferred so add them all
                promotions.removeAll(preferredPromotions);
                for(Object promotion : promotions)
                {
                    adapter.add(new Pair<Promotions, Boolean>((Promotions) promotion, false));
                }
            });
            //Sort promotions by business name
            listView.setAdapter(adapter);
        });

        //TODO get applicable promos, cross businessid with return from get applicable promos, maybe make where condition do it for me
        //TODO insert blank promo with id -1 to indicate separator from preferred to unpreferred

        listView.refreshDrawableState();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(DashboardActivity.this, DetailedDescription.class);

                Promotions promo = usersPromotions.get(position).first;
                promoImage = findViewById(R.id.iconImageView);
                promoTitle = findViewById(R.id.promotionBusinessName);
                promoDetails = findViewById(R.id.promotionDetailedDescription);
                String promoID = String.valueOf(promo.getPromotionID());

                i.putExtra("promoID", promoID);
                i.putExtra("title", promoTitle.getText());
                i.putExtra("details", promo.getDetails());
                startActivity(i);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                String userInput = s.toLowerCase();
                newusersPromotions = new ArrayList<>();
                for (int i = 0; i < usersPromotions.size(); i++) {
                    Pair<Promotions, Boolean> p = usersPromotions.get(i);
                    if (p.first.getBusinessName().toLowerCase().contains(userInput.trim())
                            || p.first.getShortDescription().toLowerCase().contains(userInput.trim())
                            || p.first.getMinTier().getName().toLowerCase().contains(userInput.trim())) {
                        newusersPromotions.add(p);
                    }
                }
                newAdapter = new PromotionsAdapter(DashboardActivity.this, newusersPromotions);
                listView.setAdapter(newAdapter);
                return true;
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() != R.id.dashboard)
            finish();
        switch (item.getItemId()) {
            case R.id.home:
                return true;
            case R.id.dashboard:
                //Intent i = new Intent(getBaseContext(), DashboardActivity.class);
                //startActivity(i);
                return true;
            case R.id.settings:
                Intent k = new Intent(getBaseContext(), SettingsActivity.class);
                startActivity(k);
                return true;
            /*case R.id.profile:
                Intent p = new Intent(getBaseContext(), ProfileActivity.class);
                startActivity(p);
                return true;*/
            case R.id.viewPoints:
                Intent j = new Intent(getBaseContext(), UserPointsActivity.class);
                startActivity(j);
                return true;
            case R.id.logOut:
                Intent m = new Intent(this, MapsActivity.class);
                m.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(m);
                Intent l = new Intent(getBaseContext(), LandingActivity.class);
                l.putExtra("logout", true);
                startActivity(l);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
