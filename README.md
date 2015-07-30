# Twitch Chat Bot Library

Twitch Chat Bot Library is a wrapper that extends off the features of [PircBotX](https://github.com/thelq/pircbotx)

Twitch Chat Bot Library makes it easier to create Twitch Bots in java


### Version
0.0.1

### Installation
Download Twitch Chat Bot Library and compile it with maven
Add the library to your classpath.

### Usage

```
TwitchBot bot = new TwitchBot("[username]", "[oauth token]", "[default channel]");

bot.addListener(new CoolListener());

bot.startBot();
```