package omg.group.priuttelegrambot.testController;

import omg.group.priuttelegrambot.controller.animals.impl.CatsController;
import omg.group.priuttelegrambot.dto.animals.CatDto;
import omg.group.priuttelegrambot.entity.animals.enimalsenum.CatsBreed;
import omg.group.priuttelegrambot.entity.animals.enimalsenum.Sex;
import omg.group.priuttelegrambot.service.CatsService;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

 public class CatsControllerTest {
    CatsService catsService = mock(CatsService.class);
    CatDto catDto = new CatDto();
    CatsController catsController = new CatsController(catsService);

    Long id = 1L;

    /**
     * Тестирование функциональности метода add класса CatsController.
     */
    @Test
    public void testAdd() {
        ResponseEntity<HttpStatus> response = catsController.add(catDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(catsService).add(catDto);
    }

    /**
     * Тест метода updateById класса CatsController.
     */
    @Test
    public void testUpdateById() {
        ResponseEntity<HttpStatus> response = catsController.updateById(id, catDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(catsService).updateById(id, catDto);
    }

    /**
     * Тест метода findById класса CatsController.
     */
    @Test
    public void testFindById() {
        ResponseEntity<List<CatDto>> response = catsController.findById(id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(catsService).findById(id);
    }

    /**
     * Тест метода findByIdSexNicknameBreed класса CatsController.
     */
    @Test
    public void testFindByIdSexNicknameBreed() {

        Sex sex = Sex.male;
        String nickname = "Garfield";
        CatsBreed breed = CatsBreed.Outbred;

        when(catsService.findById(id)).thenReturn(Collections.singletonList(new CatDto()));
        when(catsService.findBySex(sex)).thenReturn(Collections.singletonList(new CatDto()));
        when(catsService.findByNickname(nickname)).thenReturn(Collections.singletonList(new CatDto()));
        when(catsService.findByBreed(breed)).thenReturn(Collections.singletonList(new CatDto()));

        ResponseEntity<List<CatDto>> responseById = catsController.findByIdSexNicknameBreed(id, null, null, null);
        ResponseEntity<List<CatDto>> responseBySex = catsController.findByIdSexNicknameBreed(null, sex, null, null);
        ResponseEntity<List<CatDto>> responseByNickname = catsController.findByIdSexNicknameBreed(null, null, nickname, null);
        ResponseEntity<List<CatDto>> responseByBreed = catsController.findByIdSexNicknameBreed(null, null, null, breed);

        assertEquals(ResponseEntity.ok().body(Collections.singletonList(new CatDto())), responseById);
        assertEquals(ResponseEntity.ok().body(Collections.singletonList(new CatDto())), responseBySex);
        assertEquals(ResponseEntity.ok().body(Collections.singletonList(new CatDto())), responseByNickname);
        assertEquals(ResponseEntity.ok().body(Collections.singletonList(new CatDto())), responseByBreed);
        assertThrows(NullPointerException.class, () -> catsController.findByIdSexNicknameBreed(null, null, null, null));

        verify(catsService, times(1)).findById(id);
        verify(catsService, times(1)).findBySex(sex);
        verify(catsService, times(1)).findByNickname(nickname);
        verify(catsService, times(1)).findByBreed(breed);
        verifyNoMoreInteractions(catsService);
    }


    /**
     * Тест метода deleteById класса CatsController.
     */
    @Test
    public void testDeleteById() {
        ResponseEntity<HttpStatus> response = catsController.deleteById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(catsService).deleteById(id);
    }

    /**
     * Тест метода getAll класса CatsController.
     */
    @Test
    public void testGetAll() {
        ResponseEntity<List<CatDto>> response = catsController.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(catsService).getAll();
    }

    /**
     * Тест метода findByBirthdayBetweenStartDateAndEndDate класса CatsController.
     */
    @Test
    public void testFindByBirthdayBetweenStartDateAndEndDate() {
        Date startDate = new Date();
        Date endDate = new Date();

        ResponseEntity<List<CatDto>> response = catsController.findByBirthdayBetweenStartDateAndEndDate(startDate, endDate);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(catsService).findByBirthdayBetweenDates(startDate, endDate);
    }
}