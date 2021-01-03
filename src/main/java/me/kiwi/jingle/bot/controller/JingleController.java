package me.kiwi.jingle.bot.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	
	@PostMapping
	public ResponseEntity<Jingle> create(@RequestBody @Valid Jingle jingle) {
		Jingle persistedJingle = this.jingleRepository.save(jingle);
		return ResponseEntity.ok(persistedJingle);
	}
	
	@GetMapping
	public ResponseEntity<Iterable<Jingle>> findAll() {
		return ResponseEntity.ok(this.jingleRepository.findAll());
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Jingle> update(@PathVariable Long id, @RequestBody @Valid Jingle jingle) {
		ResponseEntity<Jingle> response;
		
		if(this.jingleRepository.findById(id).isPresent()) {
			jingle.setId(id);
			Jingle updatedJingle = this.jingleRepository.save(jingle);
			response = ResponseEntity.ok(updatedJingle);
		} else {
			response = ResponseEntity.notFound().build();
		}
		
		return response;
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> delete(@PathVariable Long id) {
		ResponseEntity<Object> response;
		
		if(this.jingleRepository.findById(id).isPresent()) {
			this.jingleRepository.deleteById(id);
			response = ResponseEntity.ok().build();
		} else {
			response = ResponseEntity.notFound().build();
		}
		
		return response;		
	}
}
