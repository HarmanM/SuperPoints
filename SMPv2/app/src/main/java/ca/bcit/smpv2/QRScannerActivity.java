package ca.bcit.smpv2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.Result;

import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    ZXingScannerView scanner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        scanner = new ZXingScannerView(this);
        setContentView(scanner);

    }

    @Override
    public void handleResult(Result result) {
        if(result != null) {
            String[] results = result.getText().split(" ");
            if(results.length == 2)
            {
                PromotionUsage o = new PromotionUsage(0, Integer.parseInt(results[1]), Integer.parseInt(results[0]));
                new DatabaseObj(QRScannerActivity.this).setPromotionUsage(o, (ArrayList<Object> objects)->{
                    if(objects.size() != 0)
                    {
                        onBackPressed();
                        Toast.makeText(this, "Purchase recorded!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "This QR code is not recognized.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        scanner.stopCamera();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        scanner.setResultHandler(this);
        scanner.startCamera();
    }

}
