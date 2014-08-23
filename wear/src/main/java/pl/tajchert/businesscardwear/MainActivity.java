package pl.tajchert.businesscardwear;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends Activity {
    private ImageView imageViewQRCode;
    private ImageView refreshCircle;

    private Animation refreshAnim;
    private String imagePath = Environment.getExternalStorageDirectory() + ValuesCons.WEAR_IMAGE_PATH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        refreshCircle = (ImageView) findViewById(R.id.refreshCircle);
        refreshAnim = AnimationUtils.loadAnimation(this, R.anim.refresh_animation);


        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                imageViewQRCode = (ImageView) stub.findViewById(R.id.image_qr_code);
                if(refreshAnim != null){
                    refreshAnimation(refreshAnim);
                }
            }
        });
    }

    private void refreshAnimation(final Animation animation){
        refreshCircle.setVisibility(View.VISIBLE);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        if (imageViewQRCode != null) {
                            setImageQRCode();
                        }
                    }
                }, (animation.getDuration() + 300));
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                refreshCircle.setVisibility(View.GONE);
            }
        });
        refreshCircle.startAnimation(animation);
    }

    private void setImageQRCode() {
        Bitmap qrCode = readBitmapFromFile();
        if (qrCode != null && imageViewQRCode != null) {
            imageViewQRCode.setImageBitmap(qrCode);
        }
    }


    private Bitmap readBitmapFromFile(){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return BitmapFactory.decodeFile(imagePath, options);
    }
}
