package ca.bcit.smpv2;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {

    TextView pointsSlide;
    TextView superSlide;
    Typeface welcomeFont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        pointsSlide = findViewById(R.id.points_slide);
        superSlide = findViewById(R.id.super_slide);
        welcomeFont = Typeface.createFromAsset(getAssets(), "font/robotregular.ttf");

        pointsSlide.setTypeface(welcomeFont);
        superSlide.setTypeface(welcomeFont);
    }
}
