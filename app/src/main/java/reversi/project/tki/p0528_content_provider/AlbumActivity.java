package reversi.project.tki.p0528_content_provider;

import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import reversi.project.tki.p0528_content_provider.databinding.ActivityAlbumBinding;
import reversi.project.tki.p0528_content_provider.util.MyTime;

public class AlbumActivity extends AppCompatActivity {

    private ActivityAlbumBinding b;

    private AlbumAdapter mAdapter;
    public static ArrayList<Photo> items;
    public static String currentFolder;

    private ArrayList<String> listFolder = new ArrayList<>();
    private ArrayList<String> listFolderName = new ArrayList<>();
    private ArrayList<ArrayList<Photo>> listOflist = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_album);
        b.setActivity(this);

        b.progress.setVisibility(View.VISIBLE);
        items = new ArrayList<>();
        fetchAllImages();
        modifyFolderName();

        initView();

        b.spin.setSelection(0);
        b.progress.setVisibility(View.INVISIBLE);
    }

    private void initView() {
        mAdapter = new AlbumAdapter(this,
                new AlbumAdapter.OnSelectListener() {
                    @Override
                    public void onSelected(Photo photo) {

                    }

                    @Override
                    public void onSelectedArray(ArrayList<Photo> arrayUrl) {
                           /* Intent intent = new Intent();
                            intent.putParcelableArrayListExtra(MainActivity.PUT_STRING_URI, listUrl);
                            setResult(RESULT_OK, intent);

                            finish();*/
                    }
                });
        b.rv.setAdapter(mAdapter);

        ArrayAdapter<String> spinAdapter = new ArrayAdapter<>(AlbumActivity.this, android.R.layout.simple_spinner_item, listFolderName);
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        b.spin.setAdapter(spinAdapter);
        b.spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                currentFolder = listFolder.get(position);

                items = listOflist.get(position);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("tttest", "onNothingSelected");
            }
        });
    }

    private void fetchAllImages() {
        // DATA는 이미지 파일의 스트림 데이터 경로를 나타냅니다.
        String[] projection = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATE_TAKEN,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.ORIENTATION,
        };

        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // 이미지 컨텐트 테이블
                projection, // DATA를 출력
                null,       // 모든 개체 출력
                null,
                MediaStore.MediaColumns.DATE_ADDED + " DESC");      // 정렬

        if (cursor == null) {
            // Error 발생
            // 적절하게 handling 해주세요
//            Toast.makeText(getApplicationContext(), "cursor == null", Toast.LENGTH_SHORT).show();
            Log.d("tttest", "cursor null");
            return;
        } else {
            int dataColumnIndex = cursor.getColumnIndex(projection[0]);
            int dataId = cursor.getColumnIndex(projection[1]);
            int dataDateTaken = cursor.getColumnIndex(projection[2]);
            int datasize = cursor.getColumnIndex(projection[3]);
            int dataorientation = cursor.getColumnIndex(projection[4]);


            if (cursor.moveToFirst()) {
                do {
                    String filePath = cursor.getString(dataColumnIndex);
                    String id = cursor.getString(dataId);
                    String size = cursor.getString(datasize);
                    String orientation = cursor.getString(dataorientation);
                    Uri imageUri = Uri.parse(filePath);


                    String taken = cursor.getString(dataDateTaken);
                    String takenDate = MyTime.getStringFormat(taken, MyTime.DATE_FORMAT);

                    String[] a = filePath.split("/");  //folder별 정리.
                    String folder = a[a.length - 2];


                    Photo photo = new Photo(imageUri, filePath, id, taken, size, orientation, folder);
                    addListFolder(photo);

                } while (cursor.moveToNext());

            } else {
                // Cursor가 비었습니다.
            }
        }
        cursor.close();
    }



    private void addListFolder(Photo photo) {

        if (photo.folder == null) {
            return;
        }

        if (listFolder.contains(photo.folder)) {    // folder가 있을때

            int i = 0;
            for (String f : listFolder) {
                if (f.equals(photo.folder)) {
                    listOflist.get(i).add(photo);
                }
                i++;
            }

        } else {                              // folder가 없을때
            listFolder.add(photo.folder);
            ArrayList<Photo> list = new ArrayList<>();
            list.add(photo);
            listOflist.add(list);

        }

    }

    private void modifyFolderName() {
        // folder안의 image count를 spinner에 넣기위해.
        int i = 0;
        for (String f : listFolder) {
            int size = listOflist.get(i).size();
            f = f + " (" + size + ")";
            listFolderName.add(f);
            i++;
        }
    }


}
