package me.tyler.twitchbot;

import org.pircbotx.hooks.events.ConnectEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

public interface GroupServerEventHandler {

	/**
	 * Called when the group server connects
	 * @param event Information about the connection
	 */
	public void onConnect(ConnectEvent event);
	
	
	/**
	 * Called when the group server recieves a whisper
	 * @param event Information about the whisper
	 */
	public void onPrivateMessage(PrivateMessageEvent event);
	
}
