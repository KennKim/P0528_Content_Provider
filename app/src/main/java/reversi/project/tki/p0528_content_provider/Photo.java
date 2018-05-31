package reversi.project.tki.p0528_content_provider;

import android.net.Uri;

/**
 * Created by Deviation on 2018-05-28.
 */

public class Photo  {

    public Uri uri;
    public Uri thumb;
    public String photoUrl;
    public String folder;
    public String dateAdded;
    public String dateTaken;


    public Photo(Uri uri, String photoUrl) {
        this.uri = uri;
        this.photoUrl = photoUrl;
    }


    public Photo(Uri uri, Uri thumb, String photoUrl, String dateAdded, String dateTaken) {
        this.uri = uri;
        this.thumb = thumb;
        this.photoUrl = photoUrl;
        this.dateAdded = dateAdded;
        this.dateTaken = dateTaken;
    }


    //todo: parcelable

}


