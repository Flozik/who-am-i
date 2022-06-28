package com.eleks.academy.whoami.model.response;

import com.eleks.academy.whoami.core.SynchronousPlayer;
import com.eleks.academy.whoami.model.request.QuestionAnswer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerWithState {

	private SynchronousPlayer player;

	private QuestionAnswer answer;

	private PlayerState state;

	public PlayerWithState(SynchronousPlayer player, PlayerState state) {
		this.player = player;
	}

	public static PlayerWithState of(SynchronousPlayer player) {
		return PlayerWithState.builder()
				.player(player)
				.build();
	}

}
