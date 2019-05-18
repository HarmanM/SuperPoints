package ca.bcit.smpv2;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.Result;

import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    ZXingScannerView scanner;
    final static int PERMISSION_REQUEST_CAMERA = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        scanner = new ZXingScannerView(this);
        setContentView(scanner);
        checkPermission();

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

    private void checkPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check
            if (this.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                builder.setTitle("This app needs to access your camera to read QR codes.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
                    }
                });
                builder.show();
            }
        }
    }

}
