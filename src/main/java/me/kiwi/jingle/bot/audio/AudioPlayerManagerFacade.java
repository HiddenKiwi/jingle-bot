package me.kiwi.jingle.bot.audio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

@Component
public class AudioPlayerManagerFacade {

	private static final Logger LOGGER = LoggerFactory.getLogger(AudioPlayerManagerFacade.class);

	private final AudioPlayerManager audioPlayerManager;
	
	private final LavaPlayerAudioProvider provider;
	
	private final TrackScheduler scheduler;
	
	public AudioPlayerManagerFacade(AudioPlayerManager audioPlayerManager, 
			LavaPlayerAudioProvider lavaPlayerAudioProvider, TrackScheduler trackScheduler) {
		this.audioPlayerManager = audioPlayerManager;
		AudioPlayer audioPlayer = this.audioPlayerManager.createPlayer();
		this.provider = lavaPlayerAudioProvider;
		this.scheduler = trackScheduler;
		audioPlayer.addListener(scheduler);
		this.provider.setAudioPlayer(audioPlayer);
		this.scheduler.setPlayer(audioPlayer);
	}
	
	public void load(String trackUrl) {
		this.audioPlayerManager.loadItem(trackUrl, new AudioLoadResultHandler() {
			
			@Override
			public void trackLoaded(AudioTrack track) {
				LOGGER.info("Track loaded: {}", track.getInfo().title);
				scheduler.queue(track);
			}
			
			@Override
			public void playlistLoaded(AudioPlaylist playlist) {
				LOGGER.info("Playlist loaded: {}", playlist.getName());
				playlist.getTracks().forEach(scheduler::queue);
			}
			
			@Override
			public void noMatches() {
				// Nothing to do
			}
			
			@Override
			public void loadFailed(FriendlyException exception) {
				LOGGER.error("Fail to load the track or playlist", exception);
			}
		});
	}

	public LavaPlayerAudioProvider getProvider() {
		return provider;
	}
}
