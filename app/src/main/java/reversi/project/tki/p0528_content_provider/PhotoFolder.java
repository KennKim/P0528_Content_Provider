package reversi.project.tki.p0528_content_provider;

import java.util.ArrayList;

/**
 * Created by Deviation on 2018-05-29.
 */

public class PhotoFolder {

    public String folder;
    public ArrayList<Photo> items;


    public PhotoFolder(String folder, ArrayList<Photo> itemsFolder) {
        this.folder = folder;
        this.items = itemsFolder;
    }
}
