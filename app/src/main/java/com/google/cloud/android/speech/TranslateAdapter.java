package com.google.cloud.android.speech;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;


/**
 * Created by longdo on 18/04/2017.
 */

public class TranslateAdapter extends RecyclerView.Adapter<TranslateAdapter.View_Holder> {

    private List<TranslateModel> translateModelList= Collections.emptyList();
    Context context;

    public TranslateAdapter(List<TranslateModel> translateModelList, Context context) {
        this.translateModelList = translateModelList;
        this.context = context;
    }

    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_item, parent, false);
        View_Holder holder = new View_Holder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(View_Holder holder, int position) {
        holder.title.setText(translateModelList.get(position).getText_from());
        holder.description.setText(translateModelList.get(position).getText_to());

    }

    @Override
    public int getItemCount() {
        return translateModelList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Insert a new item to the RecyclerView on a predefined position
    public void insert(int position, TranslateModel data) {
        translateModelList.add(position, data);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(TranslateModel data) {
        int position = translateModelList.indexOf(data);
        translateModelList.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * View holder class
     * */
    public class View_Holder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView title;
        TextView description;

        View_Holder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cardView);
            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);
        }
    }
}
