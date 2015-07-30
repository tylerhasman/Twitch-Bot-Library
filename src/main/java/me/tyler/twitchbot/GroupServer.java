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
	
	protected void setup(String username, String authCode, String channel){
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
	}

	/**
	 * Sets the group servers handler. 
	 * <div>Useful for handling whispers</div>
	 * @see {@link GroupServerEventHandler}
	 * @param handler the handler
	 */
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

	private void startBot()
	{
		try {
			bot.startBot();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IrcException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Send a whisper to a chatter
	 * @param user the target
	 * @param message the message
	 */
	public void sendWhisper(String user, String message)
	{
		bot.send().message(channel, "/w " + user + " " + message);
	}

	public void run() {
		startBot();
	}

	public GroupServerEventHandler getHandler() {
		return handler;
	}
	
	public void quit() {
		bot.send().quitServer();
	}

	/**
	 * @see {@link PircBotX#isConnected()}
	 * @return true if the group server is ready to send and recieve whispers
	 */
	public boolean isConnected() {
		return bot.isConnected();
	}

}
