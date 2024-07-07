package com.inha.endgame.core.io;

public enum RequestType {
	TEST(false),
	CREATE_ROOM(true),
	LEAVE_ROOM(true),
	ROOM_LIST(false),
	ADD_USER(true),
	CHECK_USER(false),
	START_ROOM(true),
	SETTING_ROOM(true),
	UPDATE_USER(false),
	PING(true),
	NETWORK_DELAY(false),
	AIM(true),
	STUN(true),
	SHOT(true),
	PLAY_MISSION(true),
	USE_ITEM(true),
	ASSASSIN_KILL(true),
	REPORT_USER(true),
	CHAT(false),
	RECONNECT(true),
	GAME_OVER_CONFIRM(true);

	// 이미 이전에 같은 유저 대상으로 해당 Request 처리 중이라면 중복으로 인지하고 true인 경우 요청 제거, 아닌 경우 스핀락으로 해결(유저당 한 쓰레드만 사용하기 위해)
	private boolean checkDuplicate;

	RequestType(boolean checkDuplicate) {
		this.checkDuplicate = checkDuplicate;
	}

	public boolean checkDuplicate() {
		return checkDuplicate;
	}
}
