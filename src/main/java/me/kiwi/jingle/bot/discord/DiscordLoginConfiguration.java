package me.kiwi.jingle.bot.discord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;

/**
 * Provides the {@link GatewayDiscordClient} bean after logging to Discord by using 
 * the {@link DiscordProperties#getToken()}.
 *
 */
@Configuration
public class DiscordLoginConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(DiscordLoginConfiguration.class);
	
	private final DiscordProperties discordProperties;
	
	/**
	 * Constructor for {@link DiscordLoginConfiguration}.
	 * @param discordProperties the external properties about Discord
	 */
	public DiscordLoginConfiguration(DiscordProperties discordProperties) {
		this.discordProperties = discordProperties;
	}
	
	/**
	 * Provides the bean of type {@link GatewayDiscordClient} required to interact with
	 * the Discord guild (e.g. subscribe to events).
	 * @return the {@link GatewayDiscordClient} bean instance
	 */
	@Bean
	public GatewayDiscordClient getGatewayDiscordClient() {
		LOGGER.info("Logging to Discord...");
		return DiscordClient.create(discordProperties.getToken()).login().block();
	}
}
