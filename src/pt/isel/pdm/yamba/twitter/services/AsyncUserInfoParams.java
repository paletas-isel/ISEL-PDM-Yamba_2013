package pt.isel.pdm.yamba.twitter.services;

import pt.isel.pdm.yamba.twitter.listeners.GetUserInfoCompletedListener;

public interface AsyncUserInfoParams {

	public void getUserInfo(GetUserInfoCompletedListener listener);
}
