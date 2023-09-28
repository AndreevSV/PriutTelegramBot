package omg.group.priuttelegrambot.dto.addresses;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
@Getter
@RequiredArgsConstructor
public abstract class AddressDto {

    private Long id;

    @Pattern(regexp = "\\d{6}")
    private int index;

    private String country;

    private String region;

    private String city;

    private String street;

    private int house;

    private char letter;

    private int building;

    private int flat;
}
