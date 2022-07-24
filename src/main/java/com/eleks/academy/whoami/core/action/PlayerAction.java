package com.eleks.academy.whoami.core.action;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class PlayerAction {

	final String player;

	Action action;

	String value;

	public enum Action {
		QUESTION, ANSWER
	}

	public enum turnResult {
		SUCCESSFUL, UNSUCCESSFUL, NOT_ASK
	}

}
