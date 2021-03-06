package ca.bcit.smpv2;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import me.originqiu.library.EditTag;


public class BusinessDashboard extends AppCompatActivity {
    public static Business business;
    int defaultPromotionPoints = 0;
    Promotions selectedPromotion;
    Button addBtn;
    Button editBtn;
    Button dltBtn;
    EditTag editTag;
    EditText tagEditText;
    ArrayList<Pair<Promotions, Boolean>> usersPromotions;
    PromotionsAdapter adapter;
    private static final int RESULT_LOAD_IMAGE = 1;
    ImageView promoImageView;
    ListView listView;
    Uri selectedImage;
    ImageView iconImageView;
    static List<PointTiers> tiers = new ArrayList<>();
    private static final int PERMISSION_REQUEST_EXTERNAL_STORAGE = 1;
    Toolbar toolbar;
    FloatingActionButton scanBtn;
    List<String> tagList = new ArrayList<>();
    TreeMap<String, Tag> dbTagList = new TreeMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        promoImageView = findViewById(R.id.promoImageView);
        iconImageView = findViewById(R.id.iconImageView);


        super.onCreate(savedInstanceState);
        checkPermission();

        new DatabaseObj(this).getBusinesses("businessID=" + DatabaseObj.SQLSafe(LoginActivity.user.getBusinessID()), (ArrayList<Object> businessObj) -> {
            business = (Business) businessObj.get(0);
            new DatabaseObj(this).getBusinessSettings("businessID=" + DatabaseObj.SQLSafe(business.getBusinessID()), (ArrayList<Object> settings) -> {
                new DatabaseObj(this).getTags("businessID=" + DatabaseObj.SQLSafe(LoginActivity.user.getBusinessID()), (ArrayList<Object> tagObjects) -> {
                    for (Object x : tagObjects)
                        dbTagList.put(((Tag) x).getTag(), (Tag)x);

                        for (Object setting : settings)
                            business.addSetting((BusinessSetting) setting);

                        setContentView(R.layout.activity_business_dashboard);
                        toolbar = (Toolbar) findViewById(R.id.toolbar);
                        setSupportActionBar(toolbar);

                        // Find the toolbar view inside the activity layout

                        usersPromotions = new ArrayList<>();

                        listView = (ListView) findViewById(R.id.lvBusinessPromotions);
                        listView.setAdapter(adapter);

                        int businessID = LoginActivity.user.getBusinessID();
                        new DatabaseObj(BusinessDashboard.this).getPromotions("businessID=" + DatabaseObj.SQLSafe(businessID), (ArrayList<Object> objects) -> {
                            for (Object o : objects) {
                                usersPromotions.add(new Pair<Promotions, Boolean>((Promotions) o, false));
                            }
                            adapter = new PromotionsAdapter(this, usersPromotions);
                            listView.setAdapter(adapter);
                        });

                        listView.refreshDrawableState();

                        listView.setOnItemClickListener((parent, view, position, id) -> {
                            selectedPromotion = usersPromotions.get(position).first;

                        });

                        addBtn = findViewById(R.id.addBtn);
                        editBtn = findViewById(R.id.editBtn);
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
                                if (selectedPromotion != null)
                                    showUpdateDialog(selectedPromotion);
                                else
                                    Toast.makeText(BusinessDashboard.this, "Select a promotion", Toast.LENGTH_SHORT).show();
                            }
                        });

                        dltBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (selectedPromotion != null)
                                    showDeleteDialog(selectedPromotion);
                                else
                                    Toast.makeText(BusinessDashboard.this, "Select a promotion", Toast.LENGTH_SHORT).show();
                            }
                        });
                });
            });
        });

        tiers.clear();
        new DatabaseObj(this).getTiers("", (ArrayList<Object> objects) -> {
            Collections.sort((ArrayList<PointTiers>) (ArrayList<?>) objects, Comparator.comparingInt(PointTiers::getMinPoints));
            for (Object o : objects)
                tiers.add((PointTiers) o);
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
        if (item.getItemId() != R.id.dashboard)
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
            case R.id.logOut:
                Intent l = new Intent(getBaseContext(), LandingActivity.class);
                l.putExtra("logout", true);
                startActivity(l);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showAddDialog() {
        showUpdateDialog(null);
    }

    private void checkPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check
            if (this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    && this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                builder.setTitle("This app needs to access your storage");
                builder.setMessage("Please grant storage access so this app can use images from your storage for promotions");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_EXTERNAL_STORAGE);
                    }
                });
                builder.show();
            }
        }
    }

    private void showDeleteDialog(Promotions promoToDelete) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.delete_promotion_dialogue, null);
        final Button buttonDeletePromotion = dialogView.findViewById(R.id.buttonDeletePromotion);
        final Button buttonCancelDeletePromotion = dialogView.findViewById(R.id.buttonCancelDeletePromotion);

        dialogBuilder.setView(dialogView);
        TextView deleteHeader = findViewById(R.id.deleteHeaderTextView);
        dialogBuilder.setCustomTitle(deleteHeader);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();


        buttonDeletePromotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO need delete operation in PHP
                new DatabaseObj(BusinessDashboard.this).deletePromotion(promoToDelete.getPromotionID(), (ArrayList<Object> objects) -> {
                    Delete instance = new Delete();
                    instance.execute(String.valueOf(promoToDelete.getPromotionID()));
                });
                usersPromotions.remove(new Pair<>(promoToDelete, false));
                alertDialog.dismiss();
                listView = (ListView) findViewById(R.id.lvBusinessPromotions);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                listView.refreshDrawableState();
            }
        });
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listView.refreshDrawableState();
        buttonCancelDeletePromotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

            }
        });
    }

    public void uploadImage(View v) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            selectedImage = data.getData();
            promoImageView.setImageURI(selectedImage);
        }
    }


    private void showUpdateDialog(Promotions updatedPromo) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.add_promotion_dialogue, null);
        final EditText editTextPromotionDetail = dialogView.findViewById(R.id.editTextPromotionDetails);
        final Button buttonAddPromotion = dialogView.findViewById(R.id.buttonAddPromotion);
        final EditText editTextShortDescription = dialogView.findViewById(R.id.editTextShortDescription);
        promoImageView = dialogView.findViewById(R.id.promoImageView);
        Spinner spinner = (Spinner) dialogView.findViewById(R.id.minTier);

        LayoutInflater factory = getLayoutInflater();
        EditTag editTag = (EditTag) dialogView.findViewById(R.id.edit_tag_view);
        List<String> tagList = new ArrayList<String>();


        dialogBuilder.setView(dialogView);
        TextView titleHeader = findViewById(R.id.titleHeader);
        dialogBuilder.setCustomTitle(titleHeader);

        final AlertDialog alertDialog = dialogBuilder.create();


        final ArrayAdapter<PointTiers> spinnerArrayAdapter = new ArrayAdapter<>(
                this, R.layout.spinner_item, tiers);

        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(spinnerArrayAdapter);

        if (updatedPromo != null) {
            editTextPromotionDetail.setText(updatedPromo.getDetails());
            if(updatedPromo.getPromotionTags().size() != 0)
            {
                for (Tag x : updatedPromo.getPromotionTags()) {
                    editTag.addTag(x.getTag());
                }
            }
            editTextPromotionDetail.setText(updatedPromo.getDetails());
            editTextShortDescription.setText(updatedPromo.getShortDescription());
            Picasso.get().load("https://s3.amazonaws.com/superpoints-userfiles-mobilehub-467637819/promo/" + updatedPromo.getPromotionID() + ".jpg")
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(promoImageView);
            for (int i = 0; i < spinnerArrayAdapter.getCount(); i++)
                if ((spinnerArrayAdapter.getItem(i)).getTierID() == updatedPromo.getMinTier().getTierID())
                    spinner.setSelection(i);
            alertDialog.show();
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            listView.refreshDrawableState();
        }

        alertDialog.show();


        buttonAddPromotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkPermission();
                iconImageView = findViewById(R.id.iconImageView);
                PointTiers promotionPoints = (PointTiers) spinner.getSelectedItem();
                String promotionDetails = editTextPromotionDetail.getText().toString();
                String shortDescription = editTextShortDescription.getText().toString();
                if (TextUtils.isEmpty(promotionDetails)) {
                    editTextPromotionDetail.setError("Your customer should know what it is you are offering!");
                }
                if (TextUtils.isEmpty(promotionDetails)) {
                    editTextShortDescription.setError("Your customer should know what it is you are offering!");
                }

                if (updatedPromo == null) {
                    int promoID = -1;
                    int businessID = LoginActivity.user.getBusinessID();
                    Promotions promo = new Promotions(promoID, businessID, promotionPoints, promotionDetails, 0, business.getBusinessName(), shortDescription);
                    if (selectedImage == null) {
                        selectedImage = Uri.parse("android.resource://ca.bcit.smpv2/drawable/not_available");
                    }
                    List<String> tagList = editTag.getTagList();
                    ArrayList<Tag> newTagList = new ArrayList<>();
                    for (int i = 0; i < tagList.size(); i++)
                    {
                        Tag newTag = new Tag(-1, LoginActivity.user.getBusinessID(), tagList.get(i));
                        if(dbTagList.get(newTag.getTag()) == null)
                        {
                            newTagList.add(newTag);
                        }
                    }
                    new DatabaseObj(BusinessDashboard.this).setTag(newTagList, (ArrayList<Object> objects2) ->
                    {
                        new DatabaseObj(BusinessDashboard.this).getTags("businessID=" + DatabaseObj.SQLSafe(LoginActivity.user.getBusinessID()), (ArrayList<Object> tagObjects) -> {
                            for (Object x : tagObjects) {
                                dbTagList.put(((Tag) x).getTag(), (Tag) x);
                            }
                            for (int i = 0; i < tagList.size(); i++)
                            {
                                promo.addPromotionTag(dbTagList.get(tagList.get(i)));
                            }
                            new DatabaseObj(BusinessDashboard.this).setPromotion(promo, (ArrayList<Object> objects) -> {
                                promo.setPromotionID(Integer.parseInt(objects.get(0).toString()));
                                ImageHandler.getInstance().uploadFile(selectedImage, String.valueOf(promo.getPromotionID()), getApplicationContext(),
                                        () -> {
                                            ListView listView = (ListView) findViewById(R.id.lvBusinessPromotions);
                                            listView.setAdapter(adapter);
                                            selectedImage = null;
                                        });
                                usersPromotions.add(new Pair<>(promo, false));
                            });
                        });
                    });
                } else {
                    List<String> tagList = editTag.getTagList();
                    ArrayList<Tag> newTagList = new ArrayList<>();
                    for (int i = 0; i < tagList.size(); i++)
                    {
                        Tag newTag = new Tag(-1, LoginActivity.user.getBusinessID(), tagList.get(i));
                        if(dbTagList.get(newTag.getTag()) == null)
                        {
                            newTagList.add(newTag);
                        }
                    }
                    new DatabaseObj(BusinessDashboard.this).setTag(newTagList, (ArrayList<Object> objects2) ->
                    {
                        new DatabaseObj(BusinessDashboard.this).getTags("businessID=" + DatabaseObj.SQLSafe(LoginActivity.user.getBusinessID()), (ArrayList<Object> tagObjects) -> {
                            for (Object x : tagObjects) {
                                dbTagList.put(((Tag) x).getTag(), (Tag) x);
                            }
                            updatedPromo.getPromotionTags().clear();
                            for (int i = 0; i < tagList.size(); i++) {
                                updatedPromo.addPromotionTag(dbTagList.get(tagList.get(i)));
                            }
                            updatedPromo.setDetails(promotionDetails);
                            updatedPromo.setShortDescription(shortDescription);
                            updatedPromo.setMinTier(promotionPoints);
                            Picasso.get().load("https://s3.amazonaws.com/superpoints-userfiles-mobilehub-467637819/promo/" + updatedPromo.getPromotionID() + ".jpg")
                                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).networkPolicy(NetworkPolicy.NO_CACHE)
                                    .into(promoImageView);
                            new DatabaseObj(BusinessDashboard.this).setPromotion(updatedPromo, (ArrayList<Object> objects) -> {
                                ImageHandler.getInstance().uploadFile(selectedImage, String.valueOf(updatedPromo.getPromotionID()), getApplicationContext(),
                                        () -> {
                                            ListView listView = (ListView) findViewById(R.id.lvBusinessPromotions);
                                            listView.setAdapter(adapter);
                                            selectedImage = null;

                                        });
                            });
                        });
                    });

                }
                alertDialog.dismiss();
            }
        });
    }

    private class Delete extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            ImageHandler.getInstance().deleteImage(strings[0], getApplicationContext());
            return true;
        }
    }

    public void openQRScanner(View view) {
        Intent i = new Intent(BusinessDashboard.this, QRScannerActivity.class);
        startActivity(i);
    }


    public void addTag(View view) {
        editTag.addTag("hello");
    }
}

