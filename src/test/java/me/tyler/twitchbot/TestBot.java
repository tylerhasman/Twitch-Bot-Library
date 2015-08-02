package me.tyler.twitchbot;

import java.io.IOException;

import me.tyler.twitchbot.teespring.Teespring;

import org.junit.Assert;
import org.junit.Test;
import org.pircbotx.exception.IrcException;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ConnectEvent;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

public class TestBot {
	
	@Test
	public void testConnectToServer() throws IOException, IrcException{
		TwitchBot bot = new TwitchBot("maventestaccount", "oauth:yysqvbli5e982cx8slkrbvqgqfjsqp", "#maventestaccount");
		
		bot.getGroupServer().setHandler(new GroupServerEventHandler() {
			
			@Override
			public void onPrivateMessage(PrivateMessageEvent event) {
				
			}
			
			@Override
			public void onConnect(ConnectEvent event) {
				
			}
		});
		
		bot.addListener(new ListenerAdapter() {
			
			@Override
			public void onJoin(JoinEvent event) throws Exception {
				TwitchBot bot = event.getBot();
				
				event.respond("Hello World");
				
				Assert.assertTrue(bot.getGroupServer().isConnected());
				Assert.assertTrue(bot.getGroupServer().getHandler() != null);
				
				Teespring tspring = new Teespring("KittyPlaysTwitchCon", (n, ts) -> System.out.println());
				
				event.respond("Teespring info: "+tspring.getId()+" "+tspring.getName()+" "+tspring.getShirtsSold());
				
				bot.send().quitServer();
			}
			
		});
		
		
		
		
		
		bot.startBot();
		
		bot.close();
	}
	
}
