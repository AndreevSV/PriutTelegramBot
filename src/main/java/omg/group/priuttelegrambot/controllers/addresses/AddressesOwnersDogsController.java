package omg.group.priuttelegrambot.controllers.addresses;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import omg.group.priuttelegrambot.dto.addresses.AddressOwnerCatDto;
import omg.group.priuttelegrambot.dto.addresses.AddressOwnerDogDto;
import omg.group.priuttelegrambot.service.addresses.AddressesOwnersDogsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dogs/addresses")
@Tag(name = "Адреса владельцев собак")
public class AddressesOwnersDogsController {

    private final AddressesOwnersDogsService addressesOwnersDogsService;

    public AddressesOwnersDogsController(AddressesOwnersDogsService addressesOwnersDogsService) {
        this.addressesOwnersDogsService = addressesOwnersDogsService;
    }

    @GetMapping("/{ownerId}/address")
    @Operation(summary = "Получение адреса",
            description = "Получение адреса клиента по id клиента")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Адрес успешно получен",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AddressOwnerDogDto.class))}),
            @ApiResponse(responseCode = "400", description = "Параметры запроса отсутствуют или имеют некорректный формат", content = @Content),
            @ApiResponse(responseCode = "404", description = "Адрес у клиента с таким id не найден", content = @Content),
            @ApiResponse(responseCode = "500", description = "Произошла ошибка, не зависящая от вызывающей стороны", content = @Content)
    })
    ResponseEntity<AddressOwnerDogDto> getAddressOfOwner(@PathVariable Long ownerId) {
        AddressOwnerDogDto addressDto = addressesOwnersDogsService.getAddressByOwnerId(ownerId);
        return new ResponseEntity<>(addressDto, HttpStatus.OK);
    }

    @PostMapping("/{ownerId}/address")
    @Operation(summary = "Создание адреса клиенту",
            description = "Установление клиенту адреса по id клиента")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Адрес успешно создан",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Параметры запроса отсутствуют или имеют некорректный формат", content = @Content),
            @ApiResponse(responseCode = "404", description = "Клиент с таким id не найден", content = @Content),
            @ApiResponse(responseCode = "500", description = "Произошла ошибка, не зависящая от вызывающей стороны", content = @Content)
    })
    ResponseEntity<Long> setAddressToOwner(@PathVariable Long ownerId, @RequestBody AddressOwnerDogDto addressDto) {
        Long addressId = addressesOwnersDogsService.setAddressToOwner(ownerId, addressDto);
        return new ResponseEntity<>(addressId, HttpStatus.CREATED);
    }

    @PatchMapping("/{addressId}")
    @Operation(summary = "Обновление адреса",
            description = "Данные адреса обновляются по id адреса и json-файла с новыми данными")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Адрес успешно обновлен",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AddressOwnerCatDto.class))}),
            @ApiResponse(responseCode = "400", description = "Параметры запроса отсутствуют или имеют некорректный формат", content = @Content),
            @ApiResponse(responseCode = "404", description = "Адрес с таким id не найден", content = @Content),
            @ApiResponse(responseCode = "500", description = "Произошла ошибка, не зависящая от вызывающей стороны", content = @Content)
    })
    ResponseEntity<HttpStatus> updateAddress(@PathVariable Long addressId, @RequestBody AddressOwnerDogDto addressDto) {
        addressesOwnersDogsService.updateAddress(addressId, addressDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{addressId}")
    @Operation(summary = "Удаление адреса",
            description = "Производится поиск адреса по идентификатору и в случае успеха данный адрес удаляется из базы данных")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Адрес успешно удален", content = @Content),
            @ApiResponse(responseCode = "400", description = "Параметры запроса отсутствуют или имеют некорректный формат", content = @Content),
            @ApiResponse(responseCode = "404", description = "Адрес с таким id не найден", content = @Content),
            @ApiResponse(responseCode = "500", description = "Произошла ошибка, не зависящая от вызывающей стороны", content = @Content)
    })
    ResponseEntity<HttpStatus> deleteAddress(@PathVariable Long addressId) {
        addressesOwnersDogsService.deleteAddress(addressId);
        return ResponseEntity.ok().build();
    }
}
