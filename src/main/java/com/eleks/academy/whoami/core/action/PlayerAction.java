package com.eleks.academy.whoami.core.action;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class PlayerAction {

	final String player;

	final Action action;

	@Setter
	String value;

	public enum Action {
		QUESTION, ANSWER
	}

}
