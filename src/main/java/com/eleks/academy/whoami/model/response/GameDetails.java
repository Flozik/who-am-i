package com.eleks.academy.whoami.model.response;

import com.eleks.academy.whoami.core.SynchronousGame;
import com.eleks.academy.whoami.core.action.PlayerAction;
import com.eleks.academy.whoami.enums.GameStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameDetails {

	private String id;

	private GameStatus status;

	private List<PlayerAction> currentTurn;

	private List<PlayerWithState> players;

	public static GameDetails of(SynchronousGame game) {
		return GameDetails.builder()
				.id(game.getId())
				.status(game.getStatus())
				.currentTurn(game.getTurn())
				.players(game.getPlayersInGame())
				.build();
	}

}
