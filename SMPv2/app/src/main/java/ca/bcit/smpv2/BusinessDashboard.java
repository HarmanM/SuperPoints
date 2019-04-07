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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;

import java.util.ArrayList;

public class BusinessDashboard extends AppCompatActivity {

    NumberPicker NBPromotionTier;
    int minimumUserTier = 1;
    int maxmimumUserTier = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_dashboard);

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        ArrayList<Promotions> usersPromotions = new ArrayList<Promotions>();
        PromotionsAdapter adapter = new PromotionsAdapter(this, usersPromotions);

        ListView listView = (ListView) findViewById(R.id.lvAnalytics);
        listView.setAdapter(adapter);

        setSupportActionBar(toolbar);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(getApplicationContext(),R.drawable.baseline_person_black_18dp));

        NBPromotionTier = (NumberPicker) findViewById(R.id.numberPickerPromotionTier);
        NBPromotionTier.setMaxValue(maxmimumUserTier);
        NBPromotionTier.setMinValue(minimumUserTier);

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
        switch (item.getItemId()) {
            case R.id.dashboard:
                Intent i = new Intent(getBaseContext(), BusinessDashboard.class);
                startActivity(i);
                return true;
            case R.id.analytics:
                Intent j = new Intent(getBaseContext(), Analytics.class);
                startActivity(j);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showAddDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.add_promotion_dialogue, null);
        final EditText editTextPromotionName = dialogView.findViewById(R.id.editTextPromotionName);
        final NumberPicker numberPickerPromotionTier = dialogView.findViewById(R.id.numberPickerPromotionTier);
        final EditText editTextPromotionDetail = dialogView.findViewById(R.id.editTextPromotionDetails);
        final Button buttonAddPromotion = dialogView.findViewById(R.id.buttonAddPromotion);

        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("Add Promotion");

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        buttonAddPromotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String promotionName = editTextPromotionName.getText().toString().trim();
                int promotionTier = numberPickerPromotionTier.getValue();
                String promotionDetails = editTextPromotionDetail.getText().toString();

                if (TextUtils.isEmpty(promotionName))
                {
                    editTextPromotionName.setError("A promotion should have a name!");
                    return;
                }
                if (promotionTier == 0)
                {
                    promotionTier = minimumUserTier;
                }
                if (TextUtils.isEmpty(promotionDetails)) {
                    editTextPromotionDetail.setError("Your customer should know what it is you are offering!");
                }

                //TODO java initializes null ints to 0, PHP script checks if "" is given for ID then triggers insert, need to adjust somehow
                int promoID = 0;
                int businessID = LoginActivity.user.getBusinessID();
                int tierID = numberPickerPromotionTier.getValue();
                String details = editTextPromotionDetail.getText().toString();
                int clicks = 0;
                //TODO should we attach business name to user or handle at db level? probably db level
                String businessName = "";
                Promotions promo = new Promotions(promoID, businessID, tierID, details, clicks, businessName);

                //TODO calls setPromotion which we decided would handle creation of row by if given object has no ID
                new DatabaseObj (BusinessDashboard.this).setPromotion(promo, null);

                //TODO call method to update promotions listview
                /*helper.updateToDo(db ,new ToDo(newTask, newWho, newDueDate, newDone), toDoId);
                lvToDo = findViewById(R.id.lvToDo);
                final ArrayList<ToDo> toDoList = getToDos();
                ToDoListAdapter adapter = new ToDoListAdapter(MainActivity.this, toDoList);
                lvToDo.setAdapter(adapter);*/

                alertDialog.dismiss();
            }
        });
}
