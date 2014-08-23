package pl.tajchert.businesscardwear;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.wearable.view.WatchViewStub;
import android.widget.ImageView;

import java.io.File;

public class MainActivity extends Activity {
    private ImageView imageViewQRCode;
    private String imagePath = Environment.getExternalStorageDirectory() + ValuesCons.WEAR_IMAGE_PATH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                imageViewQRCode = (ImageView) stub.findViewById(R.id.image_qr_code);
                setImageQRCode();
            }
        });
    }

    private void setImageQRCode(){
        File file = new File(imagePath);
        if(file.exists()){
            Bitmap qrCode = readBitmapFromFile();
            if(qrCode != null && imageViewQRCode != null) {
                imageViewQRCode.setImageBitmap(qrCode);
            }
        }
    }


    private Bitmap readBitmapFromFile(){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return BitmapFactory.decodeFile(imagePath, options);
    }
}
