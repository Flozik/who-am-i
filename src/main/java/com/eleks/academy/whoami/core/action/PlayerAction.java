package com.eleks.academy.whoami.core.action;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlayerAction {

	String player;

	String value;

	public enum Action {
		QUESTION, ANSWER
	}

}
