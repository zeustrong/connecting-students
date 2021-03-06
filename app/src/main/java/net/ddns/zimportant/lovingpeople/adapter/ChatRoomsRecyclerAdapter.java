package net.ddns.zimportant.lovingpeople.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.ddns.zimportant.lovingpeople.R;
import net.ddns.zimportant.lovingpeople.service.common.model.ChatRoom;
import net.ddns.zimportant.lovingpeople.service.common.model.UserChat;
import net.ddns.zimportant.lovingpeople.service.helper.UserViewLoader;
import net.ddns.zimportant.lovingpeople.service.interfaces.OnCreateConversation;
import net.ddns.zimportant.lovingpeople.service.utils.AppUtils;
import net.ddns.zimportant.lovingpeople.service.utils.FormatUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

public class ChatRoomsRecyclerAdapter
		extends RealmRecyclerViewAdapter<ChatRoom, ChatRoomsRecyclerAdapter.ChatRoomViewHolder> {

	public ChatRoomsRecyclerAdapter(@Nullable OrderedRealmCollection<ChatRoom> data) {
		super(data, true);
	}

	@NonNull
	@Override
	public ChatRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_chatroom, parent, false);
		return new ChatRoomViewHolder(v, parent.getContext());
	}

	@Override
	public void onBindViewHolder(@NonNull ChatRoomViewHolder holder, int position) {
		holder.setItem(getItem(position));
	}

	class ChatRoomViewHolder extends RecyclerView.ViewHolder {
		@BindView(R.id.dialogAvatar)
		CircleImageView imageView;
		@BindView(R.id.onlineIndicator)
		ImageView onlineIndicatorView;
		@BindView(R.id.dialogName)
		TextView chatRoomName;
		@BindView(R.id.dialogLastMessage)
		TextView chatRoomLastMessage;
		@BindView(R.id.dialogDate)
		TextView chatRoomLastDate;
		boolean isSetUpAutoUpdateUser = false;

		View itemView;
		ChatRoom chatRoom;
		OnCreateConversation listener;

		ChatRoomViewHolder(View itemView, Context context) {
			super(itemView);
			ButterKnife.bind(this, itemView);
			this.itemView = itemView;
			this.listener = (OnCreateConversation) context;
		}

		void setItem(ChatRoom chatRoom) {
			this.chatRoom = chatRoom;
			setUpLayout();
		}

		private void setUpLayout() {
			if (!isSetUpAutoUpdateUser) {
				isSetUpAutoUpdateUser = true;
				setUpUser();
				updateOnClick();
			}
			setUpChatRoom();
		}

		private void setUpUser() {
			RealmResults<UserChat> userRealmResults = chatRoom
					.getRealm()
					.where(UserChat.class)
					.equalTo("id", chatRoom.getUserId())
					.findAllAsync();
			UserViewLoader userViewLoader = new UserViewLoader.Builder(userRealmResults)
					.setAvatarView(imageView)
					.setStatusView(onlineIndicatorView)
					.setNameView(chatRoomName)
					.build();
			userViewLoader.startListening();
		}

		private void setUpChatRoom() {
			if (chatRoom.getMessages().size() != 0) {
				updateChatRoomLastMessage();
				updateChatRoomLastDate();
			}
		}

		private void updateChatRoomLastMessage() {
			chatRoomLastMessage.setText(
					chatRoom.getMessages().last().getBody()
			);
		}

		private void updateChatRoomLastDate() {
			chatRoomLastDate.setText(getFormatLastDate());
		}

		private String getFormatLastDate() {
			return FormatUtils.getDurationString(
					chatRoom.getMessages().last().getCreatedAt()
			);
		}

		private void updateOnClick() {
			itemView.setOnClickListener(v -> {
				listener.onOpenConversation(
						chatRoom.getStorytellerId(),
						chatRoom.getCounselorId()
				);
			});
		}
	}
}
