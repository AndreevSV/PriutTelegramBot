package omg.group.priuttelegrambot.service.impl;

import omg.group.priuttelegrambot.dto.animals.CatDto;
import omg.group.priuttelegrambot.repository.CatsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CatsServiceImplTest {
//    public static CatDto catDto =("Abyssinian";O);

    @Mock
    private CatsRepository catsRepositoryMock;

    @InjectMocks
    private CatsServiceImpl out;

//    @Test
//    public void shouldCallRepositoryMethodWhenAddCat() {
//        when(catsRepositoryMock.save(CAT_))
//    }

//    @Test
//    void updateById() {
//    }

    @Test
    void findById() {
    }

    @Test
    void findBySex() {
    }

    @Test
    void findByNickname() {
    }

    @Test
    void findByBreed() {
    }

    @Test
    void findByBirthdayBetweenDates() {
    }

    @Test
    void getAll() {
    }

    @Test
    void deleteById() {
    }

    @Test
    void getCatsRepository() {
    }

    @Test
    void testEquals() {
    }

    @Test
    void canEqual() {
    }

    @Test
    void testHashCode() {
    }

    @Test
    void testToString() {
    }
}