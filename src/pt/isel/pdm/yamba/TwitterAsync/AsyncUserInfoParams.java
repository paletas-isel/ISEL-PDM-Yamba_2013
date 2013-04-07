package pt.isel.pdm.yamba.TwitterAsync;

import pt.isel.pdm.yamba.TwitterAsync.listeners.GetUserInfoCompletedListener;

public interface AsyncUserInfoParams {

	public void getUserInfo(GetUserInfoCompletedListener listener);
}
