package kz.greetgo.sandbox.controller.register;

import kz.greetgo.sandbox.controller.model.Charm;
import kz.greetgo.sandbox.controller.model.ClientDetail;
import kz.greetgo.sandbox.controller.model.ClientRecord;
import kz.greetgo.sandbox.controller.model.FilterParams;

import java.util.List;

public interface ClientRegister {

    List<ClientRecord> getClients(FilterParams params);

    void deleteClient(int id);

    void editClient(ClientDetail cd);

    ClientDetail getDetails(int id);

    List<Charm> getCharmsList();
}
