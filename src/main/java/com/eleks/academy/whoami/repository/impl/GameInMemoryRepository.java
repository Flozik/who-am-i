package com.eleks.academy.whoami.repository.impl;

import com.eleks.academy.whoami.core.SynchronousGame;
import com.eleks.academy.whoami.enums.GameStatus;
import com.eleks.academy.whoami.repository.GameRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class GameInMemoryRepository implements GameRepository {

	private final Map<String, SynchronousGame> games = new ConcurrentHashMap<>();

	@Override
	public List<SynchronousGame> findAllAvailable(String player) {
		Predicate<SynchronousGame> freeToJoin = SynchronousGame::isAvailable;

		Predicate<SynchronousGame> playersGame = game ->
				game.findPlayer(player).isPresent();

		return this.games.values()
				.stream()
				.filter(freeToJoin.or(playersGame))
				.collect(Collectors.toList());
	}

	@Override
	public SynchronousGame save(SynchronousGame game) {
		this.games.put(game.getId(), game);

		return game;
	}

	@Override
	public Optional<SynchronousGame> findById(String id) {
		return Optional.ofNullable(this.games.get(id));
	}

	@Override
	public void deleteByIdPlayer(String id, String player) {
		if (this.games.get(id).getStatus() == GameStatus.WAITING_FOR_PLAYERS) {
			this.games.get(id).leaveGame(player);
		} else if (this.games.get(id).getStatus() == GameStatus.SUGGESTING_CHARACTERS) {
			this.games.remove(id);
		}
	}
}
