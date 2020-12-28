package me.kiwi.jingle.bot.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.channel.Channel;
import discord4j.core.object.entity.channel.VoiceChannel;
import me.kiwi.jingle.bot.audio.AudioPlayerManagerFacade;

@Component
public class AudioCommandHandler extends AbstractMessageCreateEventSubscriber {

	private static final Logger LOGGER = LoggerFactory.getLogger(AudioCommandHandler.class);
	
	private final AudioPlayerManagerFacade audioPlayerManagerFacade;
	
	public AudioCommandHandler(GatewayDiscordClient gateway, AudioPlayerManagerFacade audioPlayerManagerFacade) {
		super(gateway);
		this.audioPlayerManagerFacade = audioPlayerManagerFacade;
	}
	
	@Override
	protected void execute(MessageCreateEvent event) {
		// Get first voice channel of the guild
		VoiceChannel channel = (VoiceChannel) event.getGuild()
													.block()
													.getChannels()
													.filter(c -> Channel.Type.GUILD_VOICE.equals(c.getType()))
													.blockFirst();
		LOGGER.info("Joining {} channel", channel);
		
		channel.join(spec -> spec.setProvider(this.audioPlayerManagerFacade.getProvider())).block();
		
		this.audioPlayerManagerFacade.load("https://www.youtube.com/watch?v=NS41xDr01mw");
	}

	@Override
	protected String getCommandName() {
		return "/audio";
	}

	@Override
	protected Logger getLogger() {
		return LOGGER;
	}
}
