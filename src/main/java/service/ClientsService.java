package service;

import omg.group.priuttelegrambot.entity.clients.Clients;

import java.util.List;

public interface ClientsService {
    void addClient(Clients clients);
    Clients findClientsBySurname(String surname);
    Clients findClientsByTelephone(Long telephone);
    Clients findClientsById(Long id);
    Clients findClientsByUsername(String username);
    List<Clients> getAllClients();
    Clients updateClients(long id,Clients clients);
    Clients deleteClients(long id);
}
