package me.kiwi.jingle.bot.discord;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Provide the properties about Discord.
 */
@Configuration
@ConfigurationProperties("discord")
public class DiscordProperties {

	/**
	 * The bot token used for authentication.
	 */
    private String token;

    /**
     * @return the bot token used for authentication
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token bot token used for authentication
     */
    public void setToken(String token) {
        this.token = token;
    }
}