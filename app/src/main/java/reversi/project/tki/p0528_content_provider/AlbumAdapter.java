package reversi.project.tki.p0528_content_provider;


import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import reversi.project.tki.p0528_content_provider.databinding.ViewholderAlbumBinding;

/**
 * Created by conscious on 2017-12-19.
 */

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewholderAlbum> {

    interface OnSelectListener {
        void onSelected(Photo photo);

        void onSelectedArray(ArrayList<Photo> arrayUrl);
    }

    private OnSelectListener mListener;

//    private ArrayList<Photo> items;

    AlbumAdapter( OnSelectListener mListener) {
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
        Uri uri = AlbumActivity.items.get(p);
        h.b.setImageUri(uri);
//
//        final Photo photo = AlbumActivity.items.get(p);
//        h.b.setPhoto(photo);
//        h.b.cardview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
///*
//                    ArrayList<Photo> arrayList = new ArrayList<>();
//                    arrayList.add(new Photo(items.get(0).uri, items.get(0).photoUrl));
//                    arrayList.add(new Photo(items.get(1).uri, items.get(1).photoUrl));
//                    arrayList.add(new Photo(items.get(2).uri, items.get(2).photoUrl));
//                    mListener.onSelectedArray(arrayList);*/
//
//                mListener.onSelected(photo);
//
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return AlbumActivity.items.size();
    }

    @BindingAdapter({"android:src"})
    public static void setImage(ImageView iv, Uri uri) {
        iv.setImageURI(uri);
    }


    class ViewholderAlbum extends RecyclerView.ViewHolder {
        private ViewholderAlbumBinding b;

        public ViewholderAlbum(View itemView) {
            super(itemView);
            b = DataBindingUtil.bind(itemView);
        }
    }
}
