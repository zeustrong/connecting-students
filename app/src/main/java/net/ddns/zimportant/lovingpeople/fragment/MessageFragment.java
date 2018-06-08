package net.ddns.zimportant.lovingpeople.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import net.ddns.zimportant.lovingpeople.R;
import net.ddns.zimportant.lovingpeople.activity.ListCounselorActivity;
import net.ddns.zimportant.lovingpeople.adapter.ChatRoomsRecyclerAdapter;
import net.ddns.zimportant.lovingpeople.service.common.model.ChatRoom;
import net.ddns.zimportant.lovingpeople.service.common.model.UserChat;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

public class MessageFragment extends BaseFragment {

	@BindView(R.id.tb_message)
	Toolbar toolbar;
	@BindView(R.id.rv_message)
	RecyclerView chatRoomRecyclerView;
	@BindView(R.id.fab_message)
	FloatingActionButton fab;

	Realm realm;
	RecyclerView.LayoutManager layoutManager;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater,
	                         @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_message, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ButterKnife.bind(this, view);
		super.setUpToolbar(toolbar);
		setUpRecyclerView();
		setUpFab();
	}

	private void setUpRecyclerView() {
		RealmResults<ChatRoom> items = setupRealm();
		ChatRoomsRecyclerAdapter chatRoomsRecyclerAdapter = new ChatRoomsRecyclerAdapter(items);

		layoutManager = new LinearLayoutManager(getContext());
		chatRoomRecyclerView.setLayoutManager(layoutManager);
		chatRoomRecyclerView.setAdapter(chatRoomsRecyclerAdapter);
	}

	private RealmResults setupRealm() {
		realm = Realm.getDefaultInstance();

		registerListenUserChat();
		return registerListenChatRoom();
	}

	private void registerListenUserChat() {
		realm.where(UserChat.class)
				.findAllAsync();
	}

	private RealmResults registerListenChatRoom() {
		return realm
				.where(ChatRoom.class)
				.findAllAsync();
	}

	private void setUpFab() {
		fab.setOnClickListener(view -> {
			ListCounselorActivity.open(getContext());
		});
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_message, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		realm.close();
	}
}