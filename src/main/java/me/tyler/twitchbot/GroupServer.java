package me.tyler.twitchbot;

import java.io.IOException;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ConnectEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

public class GroupServer extends ListenerAdapter implements Runnable
{

	private static final String IP = "199.9.253.119";

	private static final int PORT = 6667;

	private PircBotX bot;

	private String channel;
	
	private GroupServerEventHandler handler;
	
	protected GroupServer(String username, String authCode, String channel)
	{

		Configuration config = new Configuration.Builder()
		.setName(username)
		.addServer(IP, PORT)
		.setServerPassword(authCode)
		.setMessageDelay(0)
		.addAutoJoinChannel(channel)
		.addListener(this)
		.setAutoSplitMessage(false)
		.buildConfiguration();

		this.channel = channel;
		
		bot = new PircBotX(config);
		
		handler = null;
	}

	public void setHandler(GroupServerEventHandler handler){
		this.handler = handler;
	}
	
	@Override
	public void onConnect(ConnectEvent event) throws Exception
	{
		bot.sendRaw().rawLineNow("CAP REQ :twitch.tv/commands");
		
		if(handler != null)
			handler.onConnect(event);
	}

	@Override
	public void onPrivateMessage(PrivateMessageEvent event) throws Exception
	{
		if(handler != null)
			handler.onPrivateMessage(event);
	}

	public void startBot()
	{
		try {
			bot.startBot();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IrcException e) {
			e.printStackTrace();
		}
	}

	public void close()
	{
		bot.close();
	}

	public void sendWhisper(String user, String message)
	{
		bot.send().message(channel, "/w " + user + " " + message);
	}

	public void run() {
		startBot();
	}

}
