package com.example.christopher.fchat.Recyclerviews;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.christopher.fchat.Models.ChatMessage;
import com.example.christopher.fchat.R;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;
import static com.example.christopher.fchat.Utils.Constants.UserID;
import static com.example.christopher.fchat.Utils.Constants.UserInformation;
import static com.example.christopher.fchat.Utils.Constants.no_id;


public class ChatRoom extends RecyclerView.Adapter<ChatRoom.ChatViewHolder> {
    private ArrayList<ChatMessage> mExampleList;
    private OnItemClickListener mListener;
    private Context context;


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public int getItemViewType(int position) {


        SharedPreferences prefs = context.getSharedPreferences(UserInformation, MODE_PRIVATE);
        String CurrentUserUID = prefs.getString(UserID, no_id);

        String AuthorUID = mExampleList.get(position).getAuthorUID();

        if (CurrentUserUID.equals(AuthorUID)) {
            position = 2;
            return position;

        } else {

            position = 0;
            return position;
        }

    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        //  public ImageView mImageView;
        public TextView mTextView1 = itemView.findViewById(R.id.message_textview);
        public TextView mTextView2 = itemView.findViewById(R.id.message_author);

        public ChatViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            //  mImageView = itemView.findViewById(R.id.imageView);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public ChatRoom(ArrayList<ChatMessage> exampleList, Context context) {
        mExampleList = exampleList;
        this.context = context;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Log.e(TAG, "onCreateViewHolder: int viewType: " + viewType );
        if (viewType == 0) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_recived, parent, false);
            ChatViewHolder evh = new ChatViewHolder(v, mListener);
            return evh;
        } else {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_send, parent, false);
            ChatViewHolder evh = new ChatViewHolder(v, mListener);
            return evh;
        }


    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        ChatMessage currentItem = mExampleList.get(position);

        //  holder.mImageView.setImageResource(currentItem.getImageResource());
        Log.e(TAG, "onBindViewHolder:" + currentItem.getMessage());
        holder.mTextView1.setText(currentItem.getMessage());
        holder.mTextView2.setText(currentItem.getTimestamp());
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
}
