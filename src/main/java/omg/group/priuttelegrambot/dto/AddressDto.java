package omg.group.priuttelegrambot.dto;

import jakarta.validation.constraints.Pattern;


public class AddressDto {
    private Long id;
    @Pattern(regexp = "d6//.")
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
