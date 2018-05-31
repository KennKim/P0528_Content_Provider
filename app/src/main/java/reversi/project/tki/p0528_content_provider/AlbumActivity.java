package reversi.project.tki.p0528_content_provider;

import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import reversi.project.tki.p0528_content_provider.databinding.ActivityAlbumBinding;

public class AlbumActivity extends AppCompatActivity {

    private ActivityAlbumBinding b;

    private AlbumAdapter mAdapter;
    private ArrayList<Photo> itemsAll = new ArrayList<>();
    //    public static ArrayList<Photo> items = new ArrayList<>();
    public static ArrayList<Uri> items = new ArrayList<>();

    //    private ArrayAdapter<String> spinAdapter;
    private ArrayList<String> listFolder = new ArrayList<>();

    public static String folder;
    private int folderIndex = 0;


    private ArrayList<Photo> itemsFolder = new ArrayList<>();
    private ArrayList<PhotoFolder> listPhotoFolder = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        b = DataBindingUtil.setContentView(this, R.layout.activity_album);
        b.setActivity(this);

        b.progress.setVisibility(View.VISIBLE);
//        getThumbAll();
        fetchAllImages();


        mAdapter = new AlbumAdapter(this, itemsAll,
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

        /*ArrayAdapter<String> spinAdapter = new ArrayAdapter<>(AlbumActivity.this, android.R.layout.simple_spinner_item, listFolder);
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        b.spin.setAdapter(spinAdapter);
        b.spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Log.d("tttest", "onItemSelected");
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("tttest", "onNothingSelected");

            }
        });
*/


        mAdapter.notifyDataSetChanged();
//        b.iv.setImageURI(itemsAll.get(10).thumb);
        b.progress.setVisibility(View.INVISIBLE);

    }

    private void fetchAllImages() {


        // DATA는 이미지 파일의 스트림 데이터 경로를 나타냅니다.
        String[] projection = {MediaStore.Images.Media.DATA,
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media.DATE_TAKEN,
                MediaStore.Images.Thumbnails._ID,
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
            int dataDateAdded = cursor.getColumnIndex(projection[2]);
            int dataDateTaken = cursor.getColumnIndex(projection[3]);
            int dataThumbId = cursor.getColumnIndex(projection[4]);


            if (cursor.moveToFirst()) {
                do {
                    String filePath = cursor.getString(dataColumnIndex);
                    String id = cursor.getString(dataId);

                    String thumbId = cursor.getString(dataThumbId);
                    Uri imageUri = Uri.parse(filePath);
//                    Uri thumbUri = getTtestThumbUri(id);
//                    Uri thumbUri = getThumbUri(thumbId);


                    String added = cursor.getString(dataDateAdded);
                    String taken = cursor.getString(dataDateTaken);


                    Photo photo = new Photo(imageUri, null, filePath, id, added, taken);
                    itemsAll.add(photo);

//                    addListFolder(photo);


                    /*

                    String filePath = cursor.getString(dataColumnIndex);
                    Uri imageUri = Uri.parse(filePath);
                    items.add(imageUri);*/

                } while (cursor.moveToNext());

            } else {
                // imageCursor가 비었습니다.
            }
        }
        cursor.close();
    }

    //    private String currentFolder = null;
    private ArrayList<Photo> listTemp = new ArrayList<>();

    private void addListFolder(Photo photo) {

        String[] a = photo.photoUrl.split("/");
        String folder = a[a.length - 2];


        if (folder == null) {
            return;
        }

        if (listFolder.contains(folder)) {    // folder가 있을때 case1. photo를 추가해야 함.

            listTemp.add(photo);

        } else {                              // folder가 없을때 case1.처음 case2.다음 folder

//            currentFolder = folder;
            listFolder.add(folder);


            if (!listTemp.isEmpty()) {
                int size = listTemp.size();
                String modifiedFolderName = folder + " (" + size + ")";
                listPhotoFolder.add(new PhotoFolder(modifiedFolderName, listTemp));

                listTemp.clear();
                listTemp.add(photo);
            }
            listTemp.add(photo);


        }
        /*
        if (listFolder.contains(folder)) {    // folder가 있을때 case1. photo를 추가해야 함.

            listTemp.add(photo);

        } else {                              // folder가 없을때 case1.처음 case2.다음 folder

//            currentFolder = folder;
            listFolder.add(folder);

            if (!listTemp.isEmpty()) {
                int size = listTemp.size();
                String modifiedFolderName = folder + " (" + size + ")";
                listPhotoFolder.add(new PhotoFolder(modifiedFolderName, listTemp));

                listTemp.clear();
                listTemp.add(photo);
            }
            listTemp.add(photo);


        }*/
    }

    private void modifyFolderName() {
        listFolder.clear();
        for (PhotoFolder pf : listPhotoFolder) {
            listFolder.add(pf.folder);
        }

       /*     int size = pf.items.size();

            int i =0;
            for (String folder : listFolder){
                if (folder.equals(pf.folder)) {
                    String folderAddedCount = folder + " (" + size + ")";
                    listFolder.set(i, folderAddedCount);
                }
                i++;
            }
        }*/
    }

    private Uri getThumbUri(String imageId) {
        // DATA는 이미지 파일의 스트림 데이터 경로를 나타냅니다.
        String[] projection = {MediaStore.Images.Thumbnails.DATA};

        // 원본 이미지의 _ID가 매개변수 imageId인 썸네일을 출력
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, // 썸네일 컨텐트 테이블
                projection, // DATA를 출력
                MediaStore.Images.Thumbnails.IMAGE_ID + "=?", // IMAGE_ID는 원본 이미지의 _ID를 나타냅니다.
                new String[]{imageId},
                null);

        //todo: 이미지 id만 던져주고 id로 thumbnail 받아와서 뿌리는 걸로?


        if (cursor != null && cursor.moveToFirst()) {

            int thumbnailColumnIndex = cursor.getColumnIndex(projection[0]);
            String thumbnailPath = cursor.getString(thumbnailColumnIndex);
            cursor.close();
            return Uri.parse(thumbnailPath);
        } else {
            // thumbnailCursor가 비었습니다.
            // 이는 이미지 파일이 있더라도 썸네일이 존재하지 않을 수 있기 때문입니다.
            // 보통 이미지가 생성된 지 얼마 되지 않았을 때 그렇습니다.
            // 썸네일이 존재하지 않을 때에는 아래와 같이 썸네일을 생성하도록 요청합니다
            MediaStore.Images.Thumbnails.getThumbnail(getContentResolver(),
                    Long.parseLong(imageId),
                    MediaStore.Images.Thumbnails.MINI_KIND,
                    null);
            cursor.close();
            return getThumbUri(imageId);
/*

Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(getContentResolver(),
	 selectedImageUri,
	MediaStore.Images.Thumbnails.MINI_KIND,
	 null );
*/

        }
    }

    private Uri getTtestThumbUri(String id) {
        Uri thumbUri = null;

        String[] projection = {MediaStore.Images.Thumbnails.DATA};

        Cursor cursor = MediaStore.Images.Thumbnails.queryMiniThumbnail(
                getContentResolver(),
                Long.parseLong(id),
                MediaStore.Images.Thumbnails.MINI_KIND,
                null);
        if (cursor != null && cursor.moveToFirst()) {

            int thumbnailColumnIndex = cursor.getColumnIndex(projection[0]);
            String thumbnailPath = cursor.getString(thumbnailColumnIndex);
            thumbUri = Uri.parse(thumbnailPath);
        }

        return thumbUri;
    }


    private void getThumbAll() {
        // DATA는 이미지 파일의 스트림 데이터 경로를 나타냅니다.
        String[] projection = {MediaStore.Images.Thumbnails.DATA};

        // 원본 이미지의 _ID가 매개변수 imageId인 썸네일을 출력
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, // 썸네일 컨텐트 테이블
                projection, // DATA를 출력
                null, // IMAGE_ID는 원본 이미지의 _ID를 나타냅니다.
                null,
                null);


        if (cursor == null) {

            Toast.makeText(AlbumActivity.this, "null", Toast.LENGTH_SHORT).show();

        } else {

            int thumbnailColumnIndex = cursor.getColumnIndex(projection[0]);

            if (cursor.moveToFirst()) {
                do {
                    String thumbnailPath = cursor.getString(thumbnailColumnIndex);
                    Uri thumbnailUri = Uri.parse(thumbnailPath);

                    items.add(thumbnailUri);

                } while (cursor.moveToNext());

            }
        }
        cursor.close();

    }

}
