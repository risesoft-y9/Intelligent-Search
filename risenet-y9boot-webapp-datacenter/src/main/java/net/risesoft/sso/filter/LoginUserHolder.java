package net.risesoft.sso.filter;


import net.risesoft.sso.model.oauth2.UserInfo;

public abstract class LoginUserHolder {
	
	private static final ThreadLocal<UserInfo> userInfoHolder = new ThreadLocal<>();

	public static void setUserInfo(UserInfo userInfo) {
		userInfoHolder.set(userInfo);
	}

	public static UserInfo getUserInfo() {
		return userInfoHolder.get();
	}

	public static void clear() {
		userInfoHolder.remove();
	}

}
