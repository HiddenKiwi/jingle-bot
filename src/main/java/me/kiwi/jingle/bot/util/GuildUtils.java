package me.kiwi.jingle.bot.util;

import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.channel.Channel;
import discord4j.core.object.entity.channel.GuildChannel;
import discord4j.core.object.entity.channel.VoiceChannel;

public final class GuildUtils {
	
	private GuildUtils() {
		// No need to instantiate this static class
	}

	public static VoiceChannel getFirstVoiceChannel(Guild guild) {
		return (VoiceChannel) guild.getChannels()
								   .filter(GuildUtils::isVoiceChannel)
								   .blockFirst();
	}
	
	public static boolean isVoiceChannel(GuildChannel channel) {
		return Channel.Type.GUILD_VOICE.equals(channel.getType());
	}
}
