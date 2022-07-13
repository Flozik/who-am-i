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

public class MessageValidationTest {

    Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @ParameterizedTest
    @CsvSource({"This_text_will_have_256_+_characters__And-will-be-used-to-validate_the_max_length_message__So_sorry_for_the_next_sentence_dfkhnfghfjrthtbjkbvzrdbnrlzbnarjkqbnaqjbnaqjbnafrjagndkhnadkjgnrvgjnarvkjgnadrgagjmaugvrgkjHVSYvbSYbYLFESHBfdljHBLJSHEblgvydSEBhgg256+1,Message length must be between 1 and 256!",
            "'  ',Message must not be blank and not null!"})
    void messageValidationTest(String ask, String message) {
        Message asking = new Message();
        asking.setMessage(ask);

        Set<ConstraintViolation<Message>> violations = validator.validate(asking);

        assertEquals(message, violations.stream().findAny().get().getMessage());
    }
}
