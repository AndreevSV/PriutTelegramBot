package omg.group.priuttelegrambot.dto.reports;

import lombok.Data;
import lombok.EqualsAndHashCode;
import omg.group.priuttelegrambot.dto.owners.OwnerCatDto;
import omg.group.priuttelegrambot.dto.pets.CatDto;

@EqualsAndHashCode(callSuper = true)
@Data
public class ReportsCatsDto extends ReportsDto {
    private OwnerCatDto ownerDto;
    private CatDto petDto;
}
