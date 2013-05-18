package pt.isel.pdm.yamba.TwitterAsync.services;

import pt.isel.pdm.yamba.TwitterAsync.listeners.GetUserInfoCompletedListener;

public interface AsyncUserInfoParams {

	public void getUserInfo(GetUserInfoCompletedListener listener);
}
