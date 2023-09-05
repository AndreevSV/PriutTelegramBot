package omg.group.priuttelegrambot.testController;

import omg.group.priuttelegrambot.controller.owners.impl.OwnersCatsController;
import omg.group.priuttelegrambot.dto.owners.OwnerCatDto;
import omg.group.priuttelegrambot.service.OwnersCatsService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class OwnersCatsControllerTest {

    OwnersCatsService ownersCatsService = mock(OwnersCatsService.class);

    OwnerCatDto ownerCatDto = new OwnerCatDto();

    OwnersCatsController ownersCatsController = new OwnersCatsController(ownersCatsService);

    Long id = 1L;

    /**
     * Тестирование функциональности метода add класса OwnersCatsController.
     */
    @Test
    public void testAdd() {
        ResponseEntity<HttpStatus> response = ownersCatsController.add(ownerCatDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(ownersCatsService).add(ownerCatDto);

    }

    /**
     * Тест метода updateById класса OwnersCatsController.
     */
    @Test
    public void testUpdateById() {
        ResponseEntity<HttpStatus> response = ownersCatsController.updateById(id, ownerCatDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(ownersCatsService).updateById(id, ownerCatDto);
    }


    /**
     * Тест метода deleteById класса OwnersCatsController.
     */
    @Test
    public void testDeleteById() {
        ResponseEntity<HttpStatus> response = ownersCatsController.deleteById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(ownersCatsService).deleteById(id);
    }

    /**
     * Тест метода getAll класса OwnersCatsController.
     */
    @Test
    public void testGetAll() {
        ResponseEntity<List<OwnerCatDto>> response = ownersCatsController.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(ownersCatsService).getAll();
    }
    /**
     * Тест метода findByIdUsernameSurnameTelephone класса OwnersCatsController.
     */
    @Test
    void testFindByIdUsernameSurnameTelephone() {

        String username = "Ivan";
        String surname = "Ivanov";
        String telephone = "88008881234";

        when(ownersCatsService.findById(id)).thenReturn(Collections.singletonList(new OwnerCatDto()));
        when(ownersCatsService.findByUsername(username)).thenReturn(Collections.singletonList(new OwnerCatDto()));
        when(ownersCatsService.findBySurname(surname)).thenReturn(Collections.singletonList(new OwnerCatDto()));
        when(ownersCatsService.findByTelephone(telephone)).thenReturn(Collections.singletonList(new OwnerCatDto()));


        ResponseEntity<List<OwnerCatDto>> responseById = ownersCatsController.findByIdUsernameSurnameTelephone(id, null, null, null);
        ResponseEntity<List<OwnerCatDto>> responseByUsername = ownersCatsController.findByIdUsernameSurnameTelephone(null, username, null, null);
        ResponseEntity<List<OwnerCatDto>> responseBySurname = ownersCatsController.findByIdUsernameSurnameTelephone(null, null, surname, null);
        ResponseEntity<List<OwnerCatDto>> responseByTelephone = ownersCatsController.findByIdUsernameSurnameTelephone(null, null, null, telephone);


        assertEquals(ResponseEntity.ok().body(Collections.singletonList(new OwnerCatDto())), responseById);
        assertEquals(ResponseEntity.ok().body(Collections.singletonList(new OwnerCatDto())), responseByUsername);
        assertEquals(ResponseEntity.ok().body(Collections.singletonList(new OwnerCatDto())), responseBySurname);
        assertEquals(ResponseEntity.ok().body(Collections.singletonList(new OwnerCatDto())), responseByTelephone);
        assertThrows(NullPointerException.class, () -> ownersCatsController.findByIdUsernameSurnameTelephone(null, null, null, null));

        verify(ownersCatsService, times(1)).findById(id);
        verify(ownersCatsService, times(1)).findByUsername(username);
        verify(ownersCatsService, times(1)).findBySurname(surname);
        verify(ownersCatsService, times(1)).findByTelephone(telephone);
        verifyNoMoreInteractions(ownersCatsService);
    }
}