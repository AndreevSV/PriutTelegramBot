package omg.group.priuttelegrambot.dto.chats;

import lombok.Data;
import lombok.EqualsAndHashCode;
import omg.group.priuttelegrambot.dto.owners.OwnerDogDto;

@Data
@EqualsAndHashCode(callSuper = true)
public class ChatDogsDto extends ChatsDto {
    private OwnerDogDto ownerDto;
    private OwnerDogDto volunteerDto;
}