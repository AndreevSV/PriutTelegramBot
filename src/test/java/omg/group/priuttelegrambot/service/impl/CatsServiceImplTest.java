package omg.group.priuttelegrambot.service.impl;

import omg.group.priuttelegrambot.dto.animals.CatDto;
import omg.group.priuttelegrambot.entity.animals.Cat;
import omg.group.priuttelegrambot.entity.animals.enimalsenum.CatsBreed;
import omg.group.priuttelegrambot.entity.owners.OwnerCat;
import omg.group.priuttelegrambot.repository.CatsRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

import static jdk.jfr.internal.jfc.model.Constraint.any;
import static org.mockito.Mockito.*;

/**
 * Class for testing CatsServiceImpl
 * @author OMGgroup
 * @see CatsServiceImpl
 * @see CatsRepository *
 */
@ExtendWith(MockitoExtension.class)
class CatsServiceImplTest {

    public static final CatDto CORRECT_CATDTO = new CatDto();
    @Mock
    private CatsRepository catsRepositoryMock;

    @InjectMocks
    private CatsServiceImpl out;

    /**
     * Test for method <b>add()</b> in CatsService
     * <br>
     * Mockito: when <b>CatsRepository::save()</b> method called, returns <b>expected</b> object
     */
    @Test
    public void shouldCallRepositoryMethodWhenAddCat() {
        when(catsRepositoryMock.save(CAT_1)).thenReturn(new Cat(CAT_1));

        assertEquals(CAT_1,out.add(CAT_1));

        verify(catsRepositoryMock,times(1)).save(CAT_1);

        Cat expected = new Cat();

        expected.setBreed(CatsBreed.valueOf("testBreed"));

        expected.setVolunteer("testVolunteer");
        expected.setOwner(testOwnerCat);


        Mockito.when(catsRepositoryMock.save(any(CatDto.class))).thenReturn(expected);

        CatDto actual = out.constructCatDtoFromCat(expected);

        Assertions.assertThat(actual.getBreed()).isEqualTo(expected.getBreed());
        Assertions.assertThat(actual.getVolunteer()).isEqualTo(expected.getVolunteer());
        Assertions.assertThat(actual.getOwner()).isEqualTo(expected.getOwner();

    }


//    @Test
//    void updateById() {
//    }

    /**
     * Test for method <b>getById()</b> in CatService
     * <br>
     * Mockito: when <b>CatRepository::findById()</b> method called, returns <b>expected</b> object
     */
    @Test
    public void shouldCallRepositoryMethodWhenGetCatById() {
        CatDto expected = new CatDto();
        expected.s("testName");
        expected.setDescription("testDesc");
        expected.setBreed("testBreed");
        expected.setYearOfBirth(2021);

        Mockito.when(catRepositoryMock.findById(any(Long.class))).thenReturn(Optional.of(expected));

        Cat actual = catService.getById(1L);

        Assertions.assertThat(actual.getName()).isEqualTo(expected.getName());
        Assertions.assertThat(actual.getBreed()).isEqualTo(expected.getBreed());
        Assertions.assertThat(actual.getDescription()).isEqualTo(expected.getDescription());
        Assertions.assertThat(actual.getYearOfBirth()).isEqualTo(expected.getYearOfBirth());
    }

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