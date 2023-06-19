package omg.group.priuttelegrambot.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class OwnerCatNotFoundException extends RuntimeException{
    public OwnerCatNotFoundException() {
        super("OwnerCat is not found!");
    }
}

