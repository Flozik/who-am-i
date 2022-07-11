package com.eleks.academy.whoami.model.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CharacterSuggestionTest {

	Validator validator;

	@BeforeEach
	public void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@ParameterizedTest
	@CsvSource(value = {"c,character,Nickname length must be between 2 and 50!",
			"123456789012345678901234567890123456789012345678901,character,Nickname length must be between 2 and 50!",
			"'  ',character,Nickname must not be blank!",
			"NULL,character,Nickname must not be null!"}, nullValues = "NULL")
	void nickNameAnnotationsValidationTest(String nickName, String character, String message) {
		CharacterSuggestion suggestion = new CharacterSuggestion();
		suggestion.setNickName(nickName);
		suggestion.setCharacter(character);

		Set<ConstraintViolation<CharacterSuggestion>> violations = validator.validate(suggestion);

		assertEquals(message, violations.stream().findFirst().get().getMessage());
	}

	@ParameterizedTest
	@CsvSource(value = {"nickName,c,Character length must be between 2 and 50!",
			"nickName,123456789012345678901234567890123456789012345678901,Character length must be between 2 and 50!",
			"nickName,'  ',Character must not be blank!",
			"nickName,NULL,Character must not be null!"}, nullValues = "NULL")
	void characterAnnotationsValidationTest(String nickname, String character, String message) {
		CharacterSuggestion suggestion = new CharacterSuggestion();
		suggestion.setNickName(nickname);
		suggestion.setCharacter(character);

		Set<ConstraintViolation<CharacterSuggestion>> violations = validator.validate(suggestion);

		assertEquals(message, violations.stream().findFirst().get().getMessage());
	}

}
