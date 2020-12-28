package me.kiwi.jingle.bot.ibm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.text_to_speech.v1.TextToSpeech;

@Configuration
public class IbmTextToSpeechConfiguration {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(IbmTextToSpeechConfiguration.class);

	private final IbmTtsProperties ibmTtsProperties;
	
	public IbmTextToSpeechConfiguration(IbmTtsProperties ibmTtsProperties) {
		this.ibmTtsProperties = ibmTtsProperties;
	}
	
	@Bean
	public TextToSpeech getTextToSpeech() {
		LOGGER.info("Logging to IBM API...");
		IamAuthenticator authenticator = new IamAuthenticator(ibmTtsProperties.getKey());
		TextToSpeech textToSpeech = new TextToSpeech(authenticator);
		textToSpeech.setServiceUrl("https://api.eu-de.text-to-speech.watson.cloud.ibm.com");
		return textToSpeech;
	}
}
