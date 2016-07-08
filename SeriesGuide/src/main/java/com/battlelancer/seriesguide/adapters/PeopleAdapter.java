package com.battlelancer.seriesguide.adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.battlelancer.seriesguide.R;
import com.battlelancer.seriesguide.util.PeopleListHelper;
import com.battlelancer.seriesguide.util.ServiceUtils;
import com.battlelancer.seriesguide.util.TmdbTools;
import java.util.List;

/**
 * Shows a list of people in rows with headshots, name and description.
 */
public class PeopleAdapter extends ArrayAdapter<PeopleListHelper.Person> {

    private static int LAYOUT = R.layout.item_person;

    private LayoutInflater mInflater;

    public PeopleAdapter(Context context) {
        super(context, LAYOUT);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(LAYOUT, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.name = (TextView) convertView.findViewById(R.id.textViewPerson);
            viewHolder.description = (TextView) convertView.findViewById(
                    R.id.textViewPersonDescription);
            viewHolder.headshot = (ImageView) convertView.findViewById(R.id.imageViewPerson);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        PeopleListHelper.Person person = getItem(position);

        // name and description
        viewHolder.name.setText(person.name);
        viewHolder.description.setText(person.description);

        // load headshot
        ServiceUtils.loadWithPicasso(getContext(),
                TmdbTools.buildProfileImageUrl(getContext(), person.profilePath,
                        TmdbTools.ProfileImageSize.W185))
                .resizeDimen(R.dimen.person_headshot_size, R.dimen.person_headshot_size)
                .centerCrop()
                .error(R.color.protection_dark)
                .into(viewHolder.headshot);

        // set unique transition names
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            viewHolder.headshot.setTransitionName("peopleAdapterPoster_" + position);
        }

        return convertView;
    }

    /**
     * Replace the data in this {@link android.widget.ArrayAdapter} with the given list.
     */
    public void setData(List<PeopleListHelper.Person> data) {
        clear();
        if (data != null) {
            addAll(data);
        }
    }

    public static class ViewHolder {
        public TextView name;
        public TextView description;
        public ImageView headshot;
    }
}
