package me.tyler.twitchbot;

import java.io.IOException;
import java.util.Arrays;

import org.pircbotx.Configuration;
import org.pircbotx.Configuration.Builder;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;
import org.pircbotx.hooks.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TwitchBot extends PircBotX {

	private static final String SERVER = "irc.twitch.tv";
	private static final int PORT = 6667;
	
	private boolean useTwitchCapabilities;
	
	private GroupServer groupServer;
	
	private Logger logger;
	
	public TwitchBot(String username, String authcode, String... defaultChannels){	
		this(getConfig(username, authcode, defaultChannels));
	}
	
	public TwitchBot(Configuration config){
		super(config);
		logger = LoggerFactory.getLogger("BOT");
		groupServer = new GroupServer();
	}
	
	public void addListener(Listener listener){
		getConfiguration().getListenerManager().addListener(listener);
	}
	
	public void setUseTwitchCapabilities(boolean useTwitchCapabilities) {
		this.useTwitchCapabilities = useTwitchCapabilities;
	}
	
	public boolean isUsingTwitchCapabilities(){
		return useTwitchCapabilities;
	}
	
	protected void setGroupServer(GroupServer gs){
		groupServer = gs;
	}
	
	public GroupServer getGroupServer() {
		return groupServer;
	}
	
	public Logger getLogger() {
		return logger;
	}
	
	private static Configuration getConfig(String username, String authcode, String... defaultChannels){
		Builder builder = new Configuration.Builder();
		
		builder.addServer(SERVER, PORT);
		builder.setName(username);
		builder.setServerPassword(authcode);
		builder.setMessageDelay(0);
		builder.addListener(new DefaultListener());
		builder.setAutoReconnect(false);
		builder.setAutoSplitMessage(false);
	
		Arrays.asList(defaultChannels).stream().forEach(s -> builder.addAutoJoinChannel(s));
		
		return builder.buildConfiguration();
	}

	@Override
	public void startBot() throws IOException, IrcException {
		groupServer.setup(getConfiguration().getName(), getConfiguration().getServerPassword(), "#"+getConfiguration().getName());
		Thread gsThread = new Thread(groupServer);
		
		gsThread.setDaemon(true);
		
		gsThread.start();
		super.startBot();
	}
	
}
