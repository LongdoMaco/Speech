package com.google.cloud.android.longdo.helps;

import android.view.View;

/**
 * Created by longdo on 19/04/2017.
 */

public interface RecyclerViewItemClickListener {
    public void onClick(View view, int position);

    public void onLongClick(View view, int position);
}
