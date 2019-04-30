package ca.bcit.smpv2;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.util.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ImageHandler {

	private static ImageHandler instance;

	private ImageHandler() {
	}

	public static ImageHandler getInstance() {
		if (instance != null) {
			return instance;
		} else {
			instance = new ImageHandler();
			return instance;
		}
	}


	public void uploadFile(Uri fileUri, String name, Context context) {
		BasicAWSCredentials credentials = new BasicAWSCredentials("AKIATGLPVNHQC5LDNPX2", "zx9MVKsBBhBlnaTikDvHYWbXLbTrNgdETnF3eWOX");
		AmazonS3Client s3Client = new AmazonS3Client(credentials);

		if (fileUri != null) {
			final String fileName = name;

			/*
			if (!validateInputFileName(fileName)) {
				return;
			}
			*/

			final File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
					"/" + fileName);

			createFile(context, fileUri, file);

			TransferUtility transferUtility =
					TransferUtility.builder()
							.context(context)
							.awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
							.s3Client(s3Client)
							.build();

			ObjectMetadata meta = new ObjectMetadata();
			meta.setContentType("image/png");

			TransferObserver uploadObserver =
					transferUtility.upload("promo/" + fileName + ".jpg", file, meta);

		}
	}

	private void createFile(Context context, Uri srcUri, File dstFile) {
		try {
			InputStream inputStream = context.getContentResolver().openInputStream(srcUri);
			if (inputStream == null) return;
			OutputStream outputStream = new FileOutputStream(dstFile);
			IOUtils.copy(inputStream, outputStream);
			inputStream.close();
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
