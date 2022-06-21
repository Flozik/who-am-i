package com.eleks.academy.whoami.repository;

import com.eleks.academy.whoami.core.SynchronousGame;
import com.eleks.academy.whoami.core.impl.PersistentGame;
import com.eleks.academy.whoami.model.request.NewGameRequest;
import com.eleks.academy.whoami.repository.impl.GameInMemoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class GameInMemoryRepositoryTest {

	private final NewGameRequest gameRequest = new NewGameRequest();

	private final GameInMemoryRepository gameRepository = new GameInMemoryRepository();

	@BeforeEach
	public void setMockMvc() {
		gameRequest.setMaxPlayers(4);
	}

	@Test
	void saveTest() {
		final String player = "player";

		SynchronousGame game = new PersistentGame(player, gameRequest.getMaxPlayers());

		assertThat(gameRepository.save(game))
				.isEqualTo(new PersistentGame(player, 4));
	}

	@Test
	void findByIdTest() {
		final String player = "player";

		SynchronousGame game = new PersistentGame(player, gameRequest.getMaxPlayers());

		var savedGame = gameRepository.save(game);
		Optional<SynchronousGame> op = Optional.of(savedGame);
		String id = savedGame.getId();

		var foundGame = gameRepository.findById(id);

		assertEquals(op, foundGame);
	}

	@Test
	void findByIdNotFoundTest() {
		final String player = "player";
		final String fakeId = "12345";

		SynchronousGame game = new PersistentGame(player, gameRequest.getMaxPlayers());

		var savedGame = gameRepository.save(game);
		Optional<SynchronousGame> op = Optional.of(savedGame);

		var foundGame = gameRepository.findById(fakeId);

		assertFalse(foundGame.isPresent());
	}

}
