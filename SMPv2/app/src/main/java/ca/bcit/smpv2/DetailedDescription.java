package ca.bcit.smpv2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailedDescription extends AppCompatActivity {

    ImageView promoImage;
    TextView promoTitle;
    TextView detailedDescription;
    Bitmap b;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_description);
        toolbar = (Toolbar) findViewById
                (R.id.toolbar);
        setSupportActionBar(toolbar);

        String title= getIntent().getStringExtra("title");
        String details= getIntent().getStringExtra("details");
        String promoID = getIntent().getStringExtra("promoID");

        promoTitle = findViewById(R.id.promotionTitle);
        detailedDescription = findViewById(R.id.promotionDetailedDescription);
        promoImage = findViewById(R.id.imageView);

        promoTitle.setText(title);
        detailedDescription.setText(details);
        Picasso.get().load("https://s3.amazonaws.com/superpoints-userfiles-mobilehub-467637819/promo/"
                + promoID + ".jpg").into(promoImage);
    }
}
