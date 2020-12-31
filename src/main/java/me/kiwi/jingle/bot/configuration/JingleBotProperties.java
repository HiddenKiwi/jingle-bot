package me.kiwi.jingle.bot.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("jingle-bot")
public class JingleBotProperties {

	private String commandPrefix;

	public String getCommandPrefix() {
		return commandPrefix;
	}

	public void setCommandPrefix(String commandPrefix) {
		this.commandPrefix = commandPrefix;
	}
}
