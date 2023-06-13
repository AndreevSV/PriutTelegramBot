package omg.group.priuttelegrambot.dto.addresses;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AddressDto {
    private Long id;
    @Pattern(regexp = "\\d{3}")
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
