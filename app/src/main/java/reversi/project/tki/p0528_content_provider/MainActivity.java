package reversi.project.tki.p0528_content_provider;

import android.Manifest;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import reversi.project.tki.p0528_content_provider.databinding.ActivityMainBinding;
import reversi.project.tki.p0528_content_provider.util.MyTime;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding b;

    public static final int REQUEST_PHOTO = 3211;
    public static final String PUT_STRING_URI = "put_string_uri";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        b = DataBindingUtil.setContentView(this, R.layout.activity_main);
        b.setActivity(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }


        new MyTime(MainActivity.this);


        ArrayList<String> tttestItems = new ArrayList<>();
        tttestItems.add("first");
        tttestItems.add("second");
        tttestItems.add("333");
        tttestItems.add("forth");

        ArrayAdapter<String>  spinAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tttestItems);
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        b.spin.setAdapter(spinAdapter);
        b.spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Log.d("tttest", "onItemSelected");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("tttest", "onItemSelected");

            }
        });


    }


    public void onClickBtn1(View view) {
        Intent intent = new Intent(MainActivity.this, AlbumActivity.class);


        startActivityForResult(intent, REQUEST_PHOTO);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_PHOTO) {

               /* ArrayList<Photo> arrayList = data.getParcelableArrayListExtra(PUT_STRING_URI);

                b.iv1.setImageURI(arrayList.get(0).uri);
                b.iv2.setImageURI(arrayList.get(1).uri);
                b.iv3.setImageURI(arrayList.get(2).uri);*/

            }
        }


    }
}
