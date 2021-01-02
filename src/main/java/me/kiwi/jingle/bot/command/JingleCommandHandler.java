package me.kiwi.jingle.bot.command;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.channel.VoiceChannel;
import me.kiwi.jingle.bot.audio.AudioPlayerManagerFacade;
import me.kiwi.jingle.bot.configuration.JingleBotProperties;
import me.kiwi.jingle.bot.entity.Jingle;
import me.kiwi.jingle.bot.repository.JingleRepository;
import me.kiwi.jingle.bot.util.GuildUtils;

@Component
public class JingleCommandHandler extends AbstractMessageCreateEventSubscriber {

	private static final Logger LOGGER = LoggerFactory.getLogger(JingleCommandHandler.class);
	
	private final AudioPlayerManagerFacade audioPlayerManagerFacade;
	
	private final JingleBotProperties jingleBotProperties;
	
	private final JingleRepository jingleRepository;
	
	public JingleCommandHandler(GatewayDiscordClient gateway, AudioPlayerManagerFacade audioPlayerManagerFacade,
			JingleBotProperties jingleBotProperties, JingleRepository jingleRepository) {
		super(gateway);
		this.audioPlayerManagerFacade = audioPlayerManagerFacade;
		this.jingleBotProperties = jingleBotProperties;
		this.jingleRepository = jingleRepository;
	}

	@Override
	protected void execute(MessageCreateEvent event) {
		LOGGER.debug("Execute jingle command...");
		
		String message = event.getMessage().getContent();
		String[] messageParts = message.split(" ");
		
		// We assume that the second part of the message is the command argument, aka the jingle name
		String jingleName = messageParts[1];
		
		Optional<Jingle> optionalJingle = this.jingleRepository.findByName(jingleName);
		if(optionalJingle.isPresent()) {
			Jingle jingle = optionalJingle.get();
			
			Guild guild = event.getGuild().block();
			VoiceChannel channel = GuildUtils.getFirstVoiceChannel(guild);
			
			LOGGER.info("Joining channel [{}]...", channel);
			channel.join(spec -> spec.setProvider(this.audioPlayerManagerFacade.getProvider())).block();
			
			LOGGER.info("Load the jingle [{}]...", jingleName);
			this.audioPlayerManagerFacade.load(jingle.getUrl());			
		} else {
			LOGGER.warn("The jingle [{}] does not exist.", jingleName);
		}
	}

	@Override
	protected String getCommandName() {
		return this.jingleBotProperties.getCommandPrefix();
	}

	@Override
	protected Logger getLogger() {
		return LOGGER;
	}
}
