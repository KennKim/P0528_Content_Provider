package reversi.project.tki.p0528_content_provider;

import android.net.Uri;

/**
 * Created by Deviation on 2018-05-28.
 */

public class Photo  {

    public Uri uri;
    public Uri thumbUri;
    public String photoPath;
    public String photoId;
    public String dateTaken;
    public String size;
    public String orientation;

    public String folder;


    public Photo(Uri uri, String photoPath, String photoId,  String dateTaken,  String size,  String orientation) {
        this.uri = uri;
        this.thumbUri = null;
        this.photoPath = photoPath;
        this.photoId = photoId;
        this.dateTaken = dateTaken;
        this.size = size;
        this.orientation = orientation;
    }


    //todo: parcelable

}


