package me.tyler.twitchbot;

import org.pircbotx.Configuration;
import org.pircbotx.Configuration.Builder;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.Listener;

public class TwitchBot extends PircBotX {

	private static final String SERVER = "irc.twitch.tv";
	private static final int PORT = 6667;
	
	private boolean useTwitchCapabilities;
	
	private GroupServer groupServer;
	
	public TwitchBot(String username, String authcode){	
		this(getConfig(username, authcode));
	}
	
	public TwitchBot(Configuration config){
		super(config);
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
	
	private static Configuration getConfig(String username, String authcode){
		Builder builder = new Configuration.Builder();
		
		builder.addServer(SERVER, PORT);
		builder.setName(username);
		builder.setServerPassword(authcode);
		builder.addListener(new DefaultListener());
		
		return builder.buildConfiguration();
	}

}
