package reversi.project.tki.p0528_content_provider.util;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.OnViewTapListener;

import reversi.project.tki.p0528_content_provider.R;
import reversi.project.tki.p0528_content_provider.databinding.ActivityImageBinding;

public class ImageActivity extends AppCompatActivity {

    private ActivityImageBinding b;
    public static final String PUT_IMG_PATH = "put_img_path";
    private boolean on;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_image);
        b.setActivity(this);

        String imgPath = getIntent().getStringExtra(PUT_IMG_PATH);
        b.setImgPath(imgPath);


//        mAdapter = new ImageAdapter(getFragmentManager());
//        b.viewpager.setAdapter(mAdapter);

        b.iv.setOnViewTapListener(new OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {

            }
        });

    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            showSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);

    }

    // Shows the system bars by removing all the flags
// except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }



    public void onClickImage(View view) {
        if (on) {
            showSystemUI();
            on=false;
        } else {
            hideSystemUI();
            on=true;
        }
    }

    @BindingAdapter({"android:src_image"})
    public static void src_image(final ImageView iv, final String imgPath) {
        iv.setImageURI(Uri.parse(imgPath));
        /*
        iv.post(new Runnable() {
            @Override
            public void run() {
                Glide.with(iv.getContext())
                        .load(Uri.parse(imgPath))
                        .crossFade()
                        .error(android.R.drawable.arrow_up_float)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(iv);
            }
        });*/
    }

}
