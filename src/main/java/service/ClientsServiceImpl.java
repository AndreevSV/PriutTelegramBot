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
    public long addClient(Clients clients) {

        return 0;
    }

    @Override
    public Clients findClientsBySurname(String surname) {
        return null;
    }

    @Override
    public Clients findClientsByTelephone(String telephone) {
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
    public Clients updateClient(long id, Clients clients) {
        return null;
    }

    @Override
    public boolean deleteClient(long id) {

        for (Clients clients : clientsMap.values()) {
            if (clientsMap.containsKey(id)) {
                clientsMap.remove(id);
                return true;
            }
        }
        return false;
    }
}
