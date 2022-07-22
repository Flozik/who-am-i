package com.eleks.academy.whoami.core.action;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class PlayerAction {

	String player;

	Action action;

	String value;

	public enum Action {
		QUESTION, ANSWER
	}

}
