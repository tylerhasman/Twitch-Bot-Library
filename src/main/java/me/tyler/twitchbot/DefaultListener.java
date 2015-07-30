package me.tyler.twitchbot;

import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ConnectEvent;
import org.pircbotx.hooks.events.QuitEvent;

/**
 * Default listener always added to TwitchBot
 * @author Tyler
 *
 */
public class DefaultListener extends ListenerAdapter {
	
	@Override
	public void onConnect(ConnectEvent event) throws Exception {
		
		TwitchBot bot = event.getBot();
		
		if(bot.isUsingTwitchCapabilities()){
			bot.sendRaw().rawLineNow("CAP REQ :twitch.tv/commands");
			bot.sendRaw().rawLineNow("CAP REQ :twitch.tv/tags");
			bot.sendRaw().rawLineNow("CAP REQ :twitch.tv/membership");
		}

	}
	
	@Override
	public void onQuit(QuitEvent event) throws Exception {
		TwitchBot bot = event.getBot();
		
		bot.getGroupServer().quit();
		
	}
	
}
