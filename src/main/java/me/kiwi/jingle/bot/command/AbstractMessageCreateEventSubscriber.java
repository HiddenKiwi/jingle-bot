package me.kiwi.jingle.bot.command;

import java.util.Optional;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Member;

/**
 * Abstract class which subscribe to {@link MessageCreateEvent} and provides method to
 * handle it processing.
 * 
 */
public abstract class AbstractMessageCreateEventSubscriber {

	private final GatewayDiscordClient gateway;
	
	/**
	 * Get the {@link GatewayDiscordClient} from the subclass.
	 * @param gateway the client to interact with the Discord gateway
	 */
	public AbstractMessageCreateEventSubscriber(GatewayDiscordClient gateway) {
		this.gateway = gateway;
	}
	
	/**
	 * Subscribe to {@link MessageCreateEvent} with the implementation of 
	 * {@link AbstractMessageCreateEventSubscriber#onMessageCreated(MessageCreateEvent)}.
	 */
	@PostConstruct
	private void subscribe() {
		this.gateway.on(MessageCreateEvent.class).subscribe(this::onMessageCreated);
	}
	
	/**
	 * Call when a new {@link MessageCreateEvent} is received. First check if the message 
	 * doesn't come from a bot and starts with {@link AbstractMessageCreateEventSubscriber#getCommandName()} 
	 * of the implementation. Then, call {@link AbstractMessageCreateEventSubscriber#execute(MessageCreateEvent)}
	 * written in the implementation.
	 * 
	 * @param event the {@link MessageCreateEvent} to process
	 */
	protected void onMessageCreated(MessageCreateEvent event) {
		String message = event.getMessage().getContent();
		
		if(authorIsNotBot(event) && messageStartWithCommandName(message)) {
			getLogger().info("New message received: [{}]", message);
			this.execute(event);
		}
	}
	
	/**
	 * The main process of the command to handle.
	 * 
	 * @param event the {@link MessageCreateEvent} to process
	 */
	protected abstract void execute(MessageCreateEvent event);
	
	/**
	 * Get the name of the command.
	 * @return the command name as a String value
	 */
	protected abstract String getCommandName();
	
	/**
	 * Get the logger of the implementation.
	 * @return the logger
	 */
	protected abstract Logger getLogger();
	
	/**
	 * Indicate if the given String value starts with the {@link AbstractMessageCreateEventSubscriber#getCommandName()}
	 * defined in the implementation of this abstract class.
	 * 
	 * @param message to check
	 * @return true if the String argument starts with the command name otherwise false.
	 */
	private boolean messageStartWithCommandName(String message) {
		return message.startsWith(getCommandName());
	}
	
	/**
	 * Indicate if the author of the {@link MessageCreateEvent} is not a bot.
	 * @param event to check
	 * @return true if the author is not a bot, otherwise false.
	 */
	private boolean authorIsNotBot(MessageCreateEvent event) {
		boolean isNotBot = false;
		
		Optional<Member> optionalMember = event.getMember();
		
		if(optionalMember.isPresent() && !optionalMember.get().isBot()) {
			isNotBot = true;
		}
		
		return isNotBot;
	}
}
