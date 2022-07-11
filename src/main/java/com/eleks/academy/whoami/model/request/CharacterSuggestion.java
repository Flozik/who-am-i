package com.eleks.academy.whoami.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CharacterSuggestion {

	@NotBlank(message = "Nickname must not be blank and not null!")
	@Size(min = 2, max = 50, message = "Nickname length must be between {min} and {max}!")
	private String nickName;

	@NotBlank(message = "Character must not be blank and not null!")
	@Size(min = 2, max = 50, message = "Character length must be between {min} and {max}!")
	private String character;

}
