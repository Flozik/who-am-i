package com.eleks.academy.whoami.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {

	@NotBlank(message = "Message must not be blank and not null!")
	@Size(min = 1, max = 256, message = "Message length must be between {min} and {max}!")
	private String message;

}
