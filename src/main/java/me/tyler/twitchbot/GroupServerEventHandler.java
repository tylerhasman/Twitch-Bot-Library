package me.tyler.twitchbot;

import org.pircbotx.hooks.events.ConnectEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

public interface GroupServerEventHandler {

	public void onConnect(ConnectEvent event);
	public void onPrivateMessage(PrivateMessageEvent event);
	
}
