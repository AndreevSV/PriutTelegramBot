package omg.group.priuttelegrambot.entity.photo;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "photo_table_cats")
public class CatsPhotos extends Photos{
}
