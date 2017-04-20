package com.google.cloud.android.speech.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.cloud.android.speech.R;
import com.google.cloud.android.speech.models.Translate;

import java.util.Collections;
import java.util.List;


/**
 * Created by longdo on 18/04/2017.
 */

public class TranslateAdapter extends RecyclerView.Adapter<TranslateAdapter.View_Holder> {

    private List<Translate> translateList = Collections.emptyList();
    Context context;

    public TranslateAdapter(List<Translate> translateList, Context context) {
        this.translateList = translateList;
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
        holder.title.setText(translateList.get(position).getText_from());
        holder.description.setText(translateList.get(position).getText_to());
        holder.imageView.setImageResource(R.drawable.speaker);

    }

    @Override
    public int getItemCount() {
        if (translateList!=null){
            return translateList.size();
        }
        else return 0;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Insert a new item to the RecyclerView on a predefined position
    public void insert(int position, Translate data) {
        translateList.add(position, data);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(Translate data) {
        int position = translateList.indexOf(data);
        translateList.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * View holder class
     * */
    public class View_Holder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView title;
        TextView description;
        ImageView imageView;

        View_Holder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cardView);
            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }
}
