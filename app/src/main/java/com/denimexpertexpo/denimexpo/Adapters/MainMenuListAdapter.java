package com.denimexpertexpo.denimexpo.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.denimexpertexpo.denimexpo.R;

/**
 * Created by Ra2l on 23/06/2015.
 */
public class MainMenuListAdapter extends BaseAdapter {

    private String[] listItemTitle;
    private String[] listSubtitleItems;
    private Context context;

    private LayoutInflater viewInflater;


    /**
     * Simple constructor
     */
    public MainMenuListAdapter(String[] titleItems, String[] subTitleItems, Context context) {
        this.listItemTitle = titleItems;
        this.listSubtitleItems = subTitleItems;

        //save the layout inflater for the later use
        this.viewInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return listItemTitle.length;
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return position;
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View newRow = convertView;
        if (newRow == null) {
            newRow = this.viewInflater.inflate(R.layout.menu_list_row, null);
        }

        String title = listItemTitle[position];
        String subTitle = listSubtitleItems[position];

        ImageView rowImage = (ImageView) newRow.findViewById(R.id.menu_list_image);
        TextView titleTextView = (TextView) newRow.findViewById(R.id.menu_list_title);
        TextView subtitleTextView = (TextView) newRow.findViewById(R.id.menu_list_sub_title);

        if (title.isEmpty()) {
            //these are blank rows
            rowImage.setVisibility(View.INVISIBLE);
            newRow.setBackgroundColor(Color.parseColor("#00ffffff"));
        } else {
            //if reused
            rowImage.setVisibility(View.VISIBLE);

            if (position % 2 == 0) {
                newRow.setBackgroundResource(R.drawable.main_main_list_item_1_selector);
            } else {
                newRow.setBackgroundResource(R.drawable.main_main_list_item_2_selector);
            }
        }

        titleTextView.setText(title);
        subtitleTextView.setText(subTitle);

        return newRow;
    }
}
