package com.example.android.movies;

/**
 * Created by Joseph Costlow on 20-Mar-17.
 */

public class Trailer {

    public String mKey;
    public String mSite;
    public String mType;
    public String mTrailerUrl;
    public String mThumbUrl;

    public Trailer(String key, String site, String type, String trailerUrl, String thumbUrl) {

        this.mKey = key;
        this.mSite = site;
        this.mType = type;
        this.mTrailerUrl = trailerUrl;
        this.mThumbUrl = thumbUrl;
    }
}
