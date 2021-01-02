package me.kiwi.jingle.bot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.kiwi.jingle.bot.entity.Jingle;
import me.kiwi.jingle.bot.repository.JingleRepository;

@RestController
@RequestMapping("/jingles")
public class JingleController {

	private final JingleRepository jingleRepository;

	public JingleController(JingleRepository jingleRepository) {
		this.jingleRepository = jingleRepository;
	}
	
	@GetMapping
	public ResponseEntity<Iterable<Jingle>> findAll() {
		return ResponseEntity.ok(this.jingleRepository.findAll());
	}
	
}
