package reversi.project.tki.p0528_content_provider;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import reversi.project.tki.p0528_content_provider.databinding.ViewholderAlbumBinding;
import reversi.project.tki.p0528_content_provider.util.ImageActivity;

/**
 * Created by conscious on 2017-12-19.
 */

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewholderAlbum> {

    interface OnSelectListener {
        void onSelected(Photo photo);

        void onSelectedArray(ArrayList<Photo> arrayUrl);
    }

    private Context mContext;
    private ArrayList<Photo> items;
    private OnSelectListener mListener;

    AlbumAdapter(Context mContext, ArrayList<Photo> items, OnSelectListener mListener) {
        this.mContext = mContext;
        this.items = items;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public ViewholderAlbum onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewholderAlbumBinding b = ViewholderAlbumBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewholderAlbum(b.getRoot());
    }

    @Override
    public void onBindViewHolder(ViewholderAlbum h, int p) {
//        Uri uri = AlbumActivity.items.get(p);
//        h.b.setImageUri(uri);

        final Photo photo = items.get(p);
        if (photo.thumbUri == null) {
            photo.thumbUri = getThumbUri(photo.photoId);
        }

        h.b.setPhoto(photo);
        h.b.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, ImageActivity.class);
                intent.putExtra(ImageActivity.PUT_IMG_PATH, photo.photoPath);
                mContext.startActivity(intent);

//                mListener.onSelected(photo);

            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @BindingAdapter({"android:src"})
    public static void setImage(final ImageView iv, final Uri uri) {
        iv.setImageURI(uri);
       /* iv.post(new Runnable() {
            @Override
            public void run() {
                Glide.with(iv.getContext())
                        .load(uri)
                        .crossFade()
                        .error(android.R.drawable.arrow_down_float)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(iv);
            }
        });*/
    }

    private Uri getThumbUri(String imageId) {
        String[] projection = {MediaStore.Images.Thumbnails.DATA};

        Cursor cursor = mContext.getContentResolver().query(
                MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, // 썸네일 컨텐트 테이블
                projection, // DATA를 출력
                MediaStore.Images.Thumbnails.IMAGE_ID + "=?", // IMAGE_ID는 원본 이미지의 _ID를 나타냅니다.
                new String[]{imageId},
                null);


        if (cursor != null && cursor.moveToFirst()) {
            int thumbnailColumnIndex = cursor.getColumnIndex(projection[0]);
            String thumbnailPath = cursor.getString(thumbnailColumnIndex);
            cursor.close();
            return Uri.parse(thumbnailPath);
        } else {
            // thumbnailCursor가 비었습니다.
            // 썸네일이 존재하지 않을 때에는 아래와 같이 썸네일을 생성하도록 요청합니다
            MediaStore.Images.Thumbnails.getThumbnail(mContext.getContentResolver(),
                    Long.parseLong(imageId),
                    MediaStore.Images.Thumbnails.MINI_KIND,
                    null);
            cursor.close();
            return getThumbUri(imageId);
        }
    }


    private Uri getTtestThumbUri(String id) {
        Uri thumbUri = null;

        String[] projection = {MediaStore.Images.Thumbnails.DATA};

        Cursor cursor = MediaStore.Images.Thumbnails.queryMiniThumbnail(
                mContext.getContentResolver(),
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

    class ViewholderAlbum extends RecyclerView.ViewHolder {
        private ViewholderAlbumBinding b;

        public ViewholderAlbum(View itemView) {
            super(itemView);
            b = DataBindingUtil.bind(itemView);
        }
    }
}
