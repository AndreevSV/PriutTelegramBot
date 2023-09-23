package omg.group.priuttelegrambot.dto.chats;

import lombok.Data;
import lombok.EqualsAndHashCode;
import omg.group.priuttelegrambot.dto.owners.OwnerCatDto;

@Data
@EqualsAndHashCode(callSuper = true)
public class ChatCatsDto extends ChatsDto {
    private OwnerCatDto ownerDto;
    private OwnerCatDto volunteerDto;
}


