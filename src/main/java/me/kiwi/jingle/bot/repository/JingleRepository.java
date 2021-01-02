package me.kiwi.jingle.bot.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import me.kiwi.jingle.bot.entity.Jingle;

public interface JingleRepository extends CrudRepository<Jingle, Long>{

	public Optional<Jingle> findByName(String name);
}
