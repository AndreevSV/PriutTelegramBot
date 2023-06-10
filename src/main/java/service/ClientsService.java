package service;

import omg.group.priuttelegrambot.entity.clients.Clients;

import java.util.List;

public interface ClientsService {
    long addClient(Clients clients);
    Clients findClientsBySurname(String surname);
    Clients findClientsByTelephone(String telephone);
    Clients findClientsById(Long id);
    Clients findClientsByUsername(String username);
    List<Clients> getAllClients();
    Clients updateClient(long id,Clients clients);
    boolean deleteClient(long id);
}
