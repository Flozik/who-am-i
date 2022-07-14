package com.eleks.academy.whoami.service.impl;

import com.eleks.academy.whoami.core.SynchronousGame;
import com.eleks.academy.whoami.core.SynchronousPlayer;
import com.eleks.academy.whoami.core.impl.PersistentGame;
import com.eleks.academy.whoami.enums.GameStatus;
import com.eleks.academy.whoami.model.request.CharacterSuggestion;
import com.eleks.academy.whoami.model.request.NewGameRequest;
import com.eleks.academy.whoami.model.response.GameDetails;
import com.eleks.academy.whoami.model.response.GameLight;
import com.eleks.academy.whoami.model.response.TurnDetails;
import com.eleks.academy.whoami.repository.GameRepository;
import com.eleks.academy.whoami.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

	private final GameRepository gameRepository;

	@Override
	public List<GameLight> findAvailableGames(String player) {
		return this.gameRepository.findAllAvailable(player)
				.stream()
				.map(GameLight::of)
				.toList();
	}

	@Override
	public GameDetails createGame(String player, NewGameRequest gameRequest) {
		final var game = this.gameRepository.save(new PersistentGame(player, gameRequest.getMaxPlayers()));

		return GameDetails.of(game);
	}

	@Override
	public SynchronousPlayer enrollToGame(String id, String player) {
		return this.gameRepository.findById(id)
				.filter(SynchronousGame::isAvailable)
				.map(game -> game.enrollToGame(player))
				.orElseThrow(
						() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot enroll to a game")
				);
	}

	@Override
	public Optional<GameDetails> findByIdAndPlayer(String id, String player) {
		return this.gameRepository.findById(id)
				.filter(game -> game.findPlayer(player).isPresent())
				.map(GameDetails::of);
	}

	@Override
	public void suggestCharacter(String id, String player, CharacterSuggestion suggestion) {
		this.gameRepository.findById(id)
				.filter(SynchronousGame::isAvailableToSuggestCharacter)
				.map(game -> game.findPlayer(player))
				.ifPresentOrElse(p -> p.ifPresentOrElse(suggest -> suggest.suggestCharacter(suggestion),
								() -> {
									throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Player not found");
								}
						),
						() -> {
							throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Game not found");
						}
				);
	}

	@Override
	public Optional<GameDetails> startGame(String id, String player) {
		return this.gameRepository.findById(id)
				.filter(game -> game.findPlayer(player).isPresent())
				.map(SynchronousGame::start)
				.map(GameDetails::of);
	}

	@Override
	public void askQuestion(String gameId, String player, String message) {
		this.gameRepository.findById(gameId)
				.ifPresent(game -> game.askQuestion(player, message));
	}

	@Override
	public Optional<TurnDetails> findTurnInfo(String id, String player) {
		return Optional.empty();
	}

	@Override
	public void submitGuess(String id, String player, String guess) {

	}

	@Override
	public void answerQuestion(String id, String player, String answer) {

	}

	@Override
	public void leaveGame(String id, String player) {
		var game = gameRepository.findById(id).orElseThrow(
				() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Game not found"));

		if (game.getStatus().equals(GameStatus.WAITING_FOR_PLAYERS)) {
			game.leaveGame(player);
		} else {
			this.gameRepository.deleteById(id);
		}
	}

}
