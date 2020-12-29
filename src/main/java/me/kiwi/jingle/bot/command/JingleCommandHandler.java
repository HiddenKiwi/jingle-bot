package me.kiwi.jingle.bot.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.channel.VoiceChannel;
import me.kiwi.jingle.bot.audio.AudioPlayerManagerFacade;
import me.kiwi.jingle.bot.util.GuildUtils;

@Component
public class JingleCommandHandler extends AbstractMessageCreateEventSubscriber {

	private static final Logger LOGGER = LoggerFactory.getLogger(JingleCommandHandler.class);
	
	private final AudioPlayerManagerFacade audioPlayerManagerFacade;
	
	private final Environment environment;
	
	public JingleCommandHandler(GatewayDiscordClient gateway, AudioPlayerManagerFacade audioPlayerManagerFacade,
			Environment environment) {
		super(gateway);
		this.audioPlayerManagerFacade = audioPlayerManagerFacade;
		this.environment = environment;
	}

	@Override
	protected void execute(MessageCreateEvent event) {
		LOGGER.debug("Execute jingle command...");
		
		String message = event.getMessage().getContent();
		String[] messageParts = message.split(" ");
		
		// We assume that the second part of the message is the command argument, aka the jingle name
		String jingleName = messageParts[1];
		
		if(environment.containsProperty(jingleName)) {
			Guild guild = event.getGuild().block();
			VoiceChannel channel = GuildUtils.getFirstVoiceChannel(guild);
			
			LOGGER.info("Joining channel [{}]...", channel);
			channel.join(spec -> spec.setProvider(this.audioPlayerManagerFacade.getProvider())).block();
			
			LOGGER.info("Load the jingle [{}]...", jingleName);
			String jingleUrl = environment.getProperty(jingleName);
			this.audioPlayerManagerFacade.load(jingleUrl);
		} else {
			LOGGER.warn("The jingle [{}] does not exist.", jingleName);
		}
	}

	@Override
	protected String getCommandName() {
		return "/jingle";
	}

	@Override
	protected Logger getLogger() {
		return LOGGER;
	}
}
