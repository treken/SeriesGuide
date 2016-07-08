package com.battlelancer.seriesguide.loaders;

import android.content.Context;
import com.battlelancer.seriesguide.tmdbapi.SgTmdb;
import com.battlelancer.seriesguide.util.ServiceUtils;
import com.uwetrottmann.androidutils.GenericSimpleLoader;
import com.uwetrottmann.tmdb2.entities.Person;
import java.io.IOException;
import retrofit2.Response;

/**
 * Loads details of a crew or cast member from TMDb.
 */
public class PersonLoader extends GenericSimpleLoader<Person> {

    private final int mTmdbId;

    public PersonLoader(Context context, int tmdbId) {
        super(context);
        mTmdbId = tmdbId;
    }

    @Override
    public Person loadInBackground() {
        Response<Person> response;
        try {
            response = ServiceUtils.getTmdb(getContext())
                    .personService()
                    .summary(mTmdbId)
                    .execute();
            if (response.isSuccessful()) {
                return response.body();
            } else {
                SgTmdb.trackFailedRequest(getContext(), "get person summary", response);
            }
        } catch (IOException e) {
            SgTmdb.trackFailedRequest(getContext(), "get person summary", e);
        }

        return null;
    }
}
