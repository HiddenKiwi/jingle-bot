package me.kiwi.jingle.bot.audio;

import java.nio.ByteBuffer;

import org.springframework.stereotype.Component;

import com.sedmelluq.discord.lavaplayer.format.StandardAudioDataFormats;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;

import discord4j.voice.AudioProvider;

@Component
public class LavaPlayerAudioProvider extends AudioProvider {

	private AudioPlayer audioPlayer;
	
	private final MutableAudioFrame mutableAudioFrame;
	
	public LavaPlayerAudioProvider() {
		super(ByteBuffer.allocate(StandardAudioDataFormats.DISCORD_OPUS.maximumChunkSize()));
		this.mutableAudioFrame = new MutableAudioFrame();
		this.mutableAudioFrame.setBuffer(getBuffer());
	}
	
	@Override
	public boolean provide() {
		final boolean didProvide = audioPlayer.provide(mutableAudioFrame);
		
		if(didProvide) {
			getBuffer().flip();
		}
		
		return didProvide;
	}

	/**
	 * @param audioPlayer the audioPlayer to set
	 */
	public void setAudioPlayer(AudioPlayer audioPlayer) {
		this.audioPlayer = audioPlayer;
	}
}
