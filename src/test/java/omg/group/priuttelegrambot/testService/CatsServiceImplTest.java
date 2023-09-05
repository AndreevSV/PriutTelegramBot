package omg.group.priuttelegrambot.testService;

import omg.group.priuttelegrambot.dto.animals.CatDto;
import omg.group.priuttelegrambot.entity.animals.Cat;
import omg.group.priuttelegrambot.entity.animals.enimalsenum.CatsBreed;
import omg.group.priuttelegrambot.repository.CatsRepository;
import org.junit.jupiter.api.Test;
import omg.group.priuttelegrambot.service.impl.CatsServiceImpl;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CatsServiceImplTest {
    /**
     * Тест метода add класса CatsServiceImpl.
     */
    @Test
    void testAdd() {
        // Создать объект класса CatDto с заполненными полями
        CatDto catDto = new CatDto();
        catDto.setBreed(CatsBreed.Ocicat);
        catDto.setNickName("Garfield");
        // Создать mock объект класса CatsRepository
        CatsRepository catsRepository = mock(CatsRepository.class);

        // Создать объект класса CatsServiceImpl, передавая в конструктор созданный CatsRepository
        CatsServiceImpl catsService = new CatsServiceImpl(catsRepository);

        // Вызвать метод add() на объекте CatsServiceImpl, передавая созданный CatDto
        catsService.add(catDto);

        // Проверить, что в CatsRepository был вызван метод save() с созданным объектом Cat
        verify(catsRepository).save(any(Cat.class));

        // Проверить, что метод add() вернул HttpStatus.CREATED
        assertEquals(HttpStatus.CREATED, catsService.add(catDto));
    }

    /**
     * Тест метода updateById (передаётся существующий id)класса CatsServiceImpl.
     */
    @Test
    void testUpdateById() {
        // Создать объект класса CatDto с заполненными полями
        CatDto catDto = new CatDto();
        catDto.setBreed(CatsBreed.Oriental_Long_Hair);
        catDto.setNickName("Tom");
        catDto.setId(1L);
        // Создать mock объект класса CatsRepository и добавить в него объект класса Cat с определенным id
        CatsRepository catsRepository = mock(CatsRepository.class);
        Cat cat = new Cat();
        cat.setId(1L);
        cat.setBreed(CatsBreed.Ocicat);
        cat.setNickName("Garfield");
        cat.setUpdatedAt(LocalDateTime.now());
        when(catsRepository.existsById(1L)).thenReturn(true);
        when(catsRepository.findById(1L)).thenReturn(Optional.of(cat));

        // Создать объект класса CatsServiceImpl, передавая в конструктор созданный CatsRepository
        CatsServiceImpl catsService = new CatsServiceImpl(catsRepository);

        // Вызвать метод updateById() на объекте CatsServiceImpl, передавая id и созданный CatDto
        catsService.updateById(1L, catDto);

        // Проверить, что в CatsRepository был вызван метод save() с созданным объектом Cat
        verify(catsRepository).save(any(Cat.class));

        // Проверить, что метод updateById() вернул HttpStatus.OK
        assertEquals(HttpStatus.OK, catsService.updateById(1L, catDto));

        // Проверить, что поле updatedAt объекта Cat было установлено на текущую дату и время
        assertNotNull(cat.getUpdatedAt());
    }

    /**
     * Тест метода updateById (передаётся несуществующий id)класса CatsServiceImpl.
     */
    @Test
    void testUpdateByIdNotFound() {
        // Создать объект класса CatDto с заполненными полями
        CatDto catDto = new CatDto();
        catDto.setBreed(CatsBreed.Oriental_Long_Hair);
        catDto.setNickName("Tom");

        // Создать mock объект класса CatsRepository и не добавлять в него объект класса Cat
        CatsRepository catsRepository = mock(CatsRepository.class);

        // Создать объект класса CatsServiceImpl, передавая в конструктор созданный CatsRepository
        CatsServiceImpl catsService = new CatsServiceImpl(catsRepository);

        // Вызвать метод updateById() на объекте CatsServiceImpl, передавая id и созданный CatDto
        RuntimeException exception = assertThrows(RuntimeException.class, () -> catsService.updateById(1L, catDto));

        // Проверить, что метод updateById() выбросил исключение RuntimeException с сообщением "Кот/кошка с id {id} не найден"
        assertEquals("Кот/кошка с id 1 не найден", exception.getMessage());
    }
}

