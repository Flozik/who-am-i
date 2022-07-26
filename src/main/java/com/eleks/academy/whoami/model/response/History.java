package com.eleks.academy.whoami.model.response;

import com.eleks.academy.whoami.core.action.PlayerAction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class History {

	private List<List<PlayerAction>> turns;

	public static History of(List<List<PlayerAction>> turns) {
		return History.builder()
				.turns(turns)
				.build();
	}

}
