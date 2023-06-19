package omg.group.priuttelegrambot.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class OwnerDogNotFoundException extends RuntimeException {
    public OwnerDogNotFoundException() {
        super("OwnerDog is not found!");
    }
}

