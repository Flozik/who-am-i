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

	@NotNull(message = "Nickname must not be null!")
	@NotBlank(message = "Nickname must not be blank")
	@Size(min = 2, max = 50, message = "Nickname size must be between 2 and 50 characters")
	private String nickName;

	@NotNull(message = "Character must not be null!")
	@NotBlank(message = "Character must not be blank!")
	@Size(min = 2, max = 50, message = "Character length must be between {min} and {max}!")
	private String character;

}
