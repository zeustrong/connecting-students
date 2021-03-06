package net.ddns.zimportant.lovingpeople.fragmentintro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.ddns.zimportant.lovingpeople.R;

import agency.tango.materialintroscreen.SlideFragment;

public class SecondSlide extends SlideFragment {
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_introduce2, container, false);
	}

	@Override
	public int backgroundColor() {
		return R.color.intro2;
	}

	@Override
	public int buttonsColor() {
		return R.color.blue_normal;
	}

	@Override
	public boolean canMoveFurther() {
		return true;
	}
}
