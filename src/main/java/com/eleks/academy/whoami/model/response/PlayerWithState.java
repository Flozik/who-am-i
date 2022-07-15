package com.eleks.academy.whoami.model.response;

import com.eleks.academy.whoami.core.SynchronousPlayer;
import com.eleks.academy.whoami.model.request.QuestionAnswer;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerWithState {

	private SynchronousPlayer player;

	private QuestionAnswer answer;

	public PlayerWithState(SynchronousPlayer player) {
		this.player = player;
	}

	public static PlayerWithState of(SynchronousPlayer player) {
		return PlayerWithState.builder()
				.player(player)
				.build();
	}

}
