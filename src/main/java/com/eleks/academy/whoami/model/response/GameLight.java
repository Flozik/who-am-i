package com.eleks.academy.whoami.model.response;

import com.eleks.academy.whoami.core.SynchronousGame;
import com.eleks.academy.whoami.enums.GameStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameLight {

	private String id;

	private GameStatus status;

	public static GameLight of(SynchronousGame game) {
		return GameLight.builder()
				.id(game.getId())
				.status(game.getStatus())
				.build();
	}

}
