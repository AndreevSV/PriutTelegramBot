package service;

import omg.group.priuttelegrambot.entity.clients.Clients;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ClientsServiceImpl implements ClientsService {
    private static long id = 0;
    private static Map<Long, Clients> clientsMap = new HashMap<>();

    @Override
    public void addClient(Clients clients) {

    }

    @Override
    public Clients findClientsBySurname(String surname) {
        return null;
    }

    @Override
    public Clients findClientsByTelephone(Long telephone) {
        return null;
    }

    @Override
    public Clients findClientsById(Long id) {
        return null;
    }

    @Override
    public Clients findClientsByUsername(String username) {
        return null;
    }

    @Override
    public List<Clients> getAllClients() {
        return null;
    }

    @Override
    public Clients updateClients(long id, Clients clients) {
        return null;
    }

    @Override
    public Clients deleteClients(long id) {
        return null;
    }
}
