package me.tyler.twitchbot;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.pircbotx.Configuration;
import org.pircbotx.Configuration.Builder;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;
import org.pircbotx.hooks.Listener;
import org.pircbotx.hooks.ListenerAdapter;

/**
 * Twitch bot used for connecting to twitch.tv stream chats
 * @author Tyler
 *
 */
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
		logger = Logger.getLogger(config.getName());
		
		Formatter format = new Formatter(){

			@Override
			public String format(LogRecord record) {
				
				Date date = new Date(record.getMillis());
				
				return date.toString()+" ["+record.getLevel().getName()+"] "+record.getMessage();
			}
			
		};
		
		logger.setUseParentHandlers(false);
		
		logger.addHandler(new ConsoleHandler(){
			{
				setFormatter(format);
			}
		});
		
		logger.setLevel(Level.INFO);
		
		groupServer = new GroupServer();
	}
	
	/**
	 * @see Listener
	 * @param listener The listener to add
	 */
	public void addListener(Listener listener){
		getConfiguration().getListenerManager().addListener(listener);
	}
	
	/**
	 * <div>Enable or disable twitch capabilities,
	 * this must be set before the bot is started for it to take effect
	 * </div>
	 * @param useTwitchCapabilities if set to true capabilities will be enabled once the bot has connected
	 * @see <a href=https://github.com/justintv/Twitch-API/blob/master/IRC.md>Twitch IRC Documentation</a>
	 */
	public void setUseTwitchCapabilities(boolean useTwitchCapabilities) {
		this.useTwitchCapabilities = useTwitchCapabilities;
	}
	
	/**
	 * 
	 * @return true if capabilities are enabled
	 */
	public boolean isUsingTwitchCapabilities(){
		return useTwitchCapabilities;
	}
	
	protected void setGroupServer(GroupServer gs){
		groupServer = gs;
	}
	
	/**
	 * The group server is used to send whispers
	 * @return the group server
	 */
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

	/**
	 * <div>Starts the main bot and the {@link GroupServer}</div>
	 * 
	 */
	@Override
	public void startBot() throws IOException, IrcException {
		groupServer.setup(getConfiguration().getName(), getConfiguration().getServerPassword(), "#"+getConfiguration().getName());
		Thread gsThread = new Thread(groupServer);
		
		gsThread.setDaemon(true);
		
		gsThread.start();
		super.startBot();
	}
	
}
