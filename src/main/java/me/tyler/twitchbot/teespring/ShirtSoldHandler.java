package me.tyler.twitchbot.teespring;

public interface ShirtSoldHandler {

	/**
	 * Called when a amount of shirts are sold
	 * @param amount the amount sold since the last check
	 * @param ts the teespring object this event is related to
	 */
	public void onShirtSold(int amount, Teespring ts);
	
}
