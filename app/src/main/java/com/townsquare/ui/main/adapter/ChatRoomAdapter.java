package com.townsquare.ui.main.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.townsquare.R;
import com.townsquare.ui.main.models.Message;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ChatRoomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public ChatRoomAdapter(Context context, List<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == OTHERS) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_other, parent, false);
            return new ChatRoomAdapter.OtherViewHolder(view);
        } else if (viewType == OWNER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_owner, parent, false);
            return new ChatRoomAdapter.OwnerViewHolder(view);
        }
        throw new RuntimeException("There is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);

        if (holder != null) {
            if (holder instanceof ChatRoomAdapter.OwnerViewHolder) {
                ChatRoomAdapter.OwnerViewHolder.bindMessageOwnerInfo((OwnerViewHolder) holder, message, context, this, position);
            } else {
                ChatRoomAdapter.OtherViewHolder.bindMessageOtherUserInfo((OtherViewHolder) holder, message, context, this, position);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).isOwner()) {
            return OWNER;
        }
        return OTHERS;
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class OtherViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.message)
        TextView message;

        @BindView(R.id.date)
        TextView date;

        OtherViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        static void bindMessageOtherUserInfo(ChatRoomAdapter.OtherViewHolder holder, Message messages, final Context context, ChatRoomAdapter adapter, int position) {
            holder.date.setText(messages.getWhen());
            holder.message.setText(messages.getContent());
            holder.name.setText(messages.getSender());
        }

    }


    static class OwnerViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.message)
        TextView message;

        @BindView(R.id.date)
        TextView date;

        OwnerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        static void bindMessageOwnerInfo(ChatRoomAdapter.OwnerViewHolder holder, Message messages, final Context context, ChatRoomAdapter adapter, int position) {
            holder.date.setText(messages.getWhen());
            holder.message.setText(messages.getContent());
        }
    }

    private static final String TAG = ChatRoomAdapter.class.getCanonicalName();
    private List<Message> messages;
    private Context context;

    private static int OTHERS = 1;
    private static int OWNER = 0;
}
