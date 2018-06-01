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
import java.util.LinkedHashMap;

import reversi.project.tki.p0528_content_provider.databinding.ActivityAlbumBinding;
import reversi.project.tki.p0528_content_provider.util.MyTime;

public class AlbumActivity extends AppCompatActivity {

    private ActivityAlbumBinding b;

    private AlbumAdapter mAdapter;
    private ArrayList<Photo> itemsAll = new ArrayList<>();
    private ArrayList<Photo> items = new ArrayList<>();
    private LinkedHashMap<String, ArrayList<Photo>> mapFolder = new LinkedHashMap<>();
    private ArrayList<String> listFolder = new ArrayList<>();
    private int spinIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_album);
        b.setActivity(this);

        b.progress.setVisibility(View.VISIBLE);
        fetchAllImages();
        modifyFolderName();

        initView();

        b.spin.setSelection(spinIndex);
//        mAdapter.notifyDataSetChanged();
        b.progress.setVisibility(View.INVISIBLE);

    }

    private void initView() {
        mAdapter = new AlbumAdapter(this, items,
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

        ArrayAdapter<String> spinAdapter = new ArrayAdapter<>(AlbumActivity.this, android.R.layout.simple_spinner_item, listFolder);
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        b.spin.setAdapter(spinAdapter);
        b.spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Log.d("tttest", "onItemSelected");
//                mAdapter.notifyDataSetChanged();
//                items = mapFolder.values().toArray()[position];
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
            itemsAll = new ArrayList<>(cursor.getCount());
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

                    //todo: photo pojo 정리하고, folder 별로 spinner 해결할 것. 그리고 GLIDE uri로 load 하는거 찾아볼 것. 꼭 file로만 해야 하는건지?

                    String taken = cursor.getString(dataDateTaken);
                    String takenDate = MyTime.getStringFormat(taken, MyTime.DATE_FORMAT);


                    Photo photo = new Photo(imageUri, filePath, id, taken, size, orientation);
//                    itemsAll.add(photo);

                    addListFolder(photo);

                } while (cursor.moveToNext());

            } else {
                // Cursor가 비었습니다.
            }
        }
        cursor.close();
    }

    private void addListFolder(Photo photo) {
        String[] a = photo.photoPath.split("/");
        String folder = a[a.length - 2];

        if (folder == null) {
            return;
        }

        if (listFolder.contains(folder)) {    // folder가 있을때 case1. photo를 추가해야 함.
            ArrayList<Photo> list = mapFolder.get(folder);
            list.add(photo);
            mapFolder.put(folder, list);

        } else {                              // folder가 없을때 case1.처음 case2.다음 folder
            listFolder.add(folder);
            ArrayList<Photo> list = new ArrayList<>();
            list.add(photo);
            mapFolder.put(folder, list);

        }

    }

    private void modifyFolderName() {
        listFolder.clear();
        for (String f : mapFolder.keySet()) {
            int size = mapFolder.get(f).size();
            listFolder.add(f + " (" + size + ")");

        }
    }


}
