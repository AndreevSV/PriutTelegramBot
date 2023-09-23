package omg.group.priuttelegrambot.entity.telegram;

import lombok.Data;

@Data
public class Result {
    private String file_id;
    private String file_unique_id;
    private Integer file_size;
    private String file_path;
}
