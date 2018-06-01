package reversi.project.tki.p0528_content_provider;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
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
    //    private ArrayList<Photo> items;
    private OnSelectListener mListener;


    public AlbumAdapter(Context mContext, OnSelectListener mListener) {
        this.mContext = mContext;
//        this.items = items;
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

        Photo photo = AlbumActivity.items.get(p);


        h.bind(photo);
/*
        h.b.setPhoto(photo);
        h.b.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, ImageActivity.class);
                intent.putExtra(ImageActivity.PUT_IMG_PATH, photo.photoPath);
                mContext.startActivity(intent);

//                mListener.onSelected(photo);

            }
        });*/

    }

    @Override
    public int getItemCount() {
        return AlbumActivity.items.size();
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


    public class ViewholderAlbum extends RecyclerView.ViewHolder {
        private ViewholderAlbumBinding b;
        private Photo photo;

        public ViewholderAlbum(View itemView) {
            super(itemView);
            b = DataBindingUtil.bind(itemView);
            b.setHolder(this);
        }

        private void bind(Photo photo) {
            this.photo = photo;
            if (photo.thumbUri == null) {
//                photo.thumbUri = getThumbUri(photo.photoId);
                new MyAsync((AlbumActivity) mContext, b).execute(photo);
                return;
            }
            b.setPhoto(photo);
        }

        public void onClickIv(View view) {
            Intent intent = new Intent(mContext, ImageActivity.class);
            intent.putExtra(ImageActivity.PUT_IMG_PATH, photo.photoPath);
            mContext.startActivity(intent);
        }


    }

    public static class MyAsync extends AsyncTask<Photo, Integer, Uri> {

        private WeakReference<AlbumActivity> mActivity;
        private Photo photo;
        private ViewholderAlbumBinding b;

        private MyAsync(AlbumActivity activity, ViewholderAlbumBinding b) {
            mActivity = new WeakReference<>(activity);
            this.b = b;
        }


        @Override
        protected Uri doInBackground(Photo... photos) {
            this.photo = photos[0];
            return getThumbUri(photo.photoId, 1);
        }

        @Override
        protected void onPostExecute(Uri uri) {
            if (AlbumActivity.currentFolder.equals(photo.folder)) {
                photo.thumbUri = uri;
                b.setPhoto(photo);
            }else{
                cancel(true);
            }
            super.onPostExecute(uri);
        }


        private Uri getThumbUri(String imageId, int count) {
            String[] projection = {MediaStore.Images.Thumbnails.DATA};

            Cursor cursor = mActivity.get().getContentResolver().query(
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
                if (count > 2) {
                    return null; // 요청은 두번만 하고 그래도 썸네일 안 만들어지면 그냥 null
                }
                count++;

                MediaStore.Images.Thumbnails.getThumbnail(mActivity.get().getContentResolver(),
                        Long.parseLong(imageId),
                        MediaStore.Images.Thumbnails.MINI_KIND,
                        null);
                cursor.close();
                return getThumbUri(imageId, count);
            }
        }
    }

}
