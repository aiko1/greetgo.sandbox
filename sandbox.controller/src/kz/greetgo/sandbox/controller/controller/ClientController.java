package kz.greetgo.sandbox.controller.controller;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonGetter;
import kz.greetgo.depinject.core.Bean;
import kz.greetgo.depinject.core.BeanGetter;
import kz.greetgo.mvc.annotations.*;
import kz.greetgo.mvc.annotations.on_methods.*;
import kz.greetgo.mvc.interfaces.TunnelCookies;
import kz.greetgo.sandbox.controller.model.*;
import kz.greetgo.sandbox.controller.register.AuthRegister;
import kz.greetgo.sandbox.controller.register.ClientRegister;
import kz.greetgo.sandbox.controller.security.PublicAccess;
import kz.greetgo.sandbox.controller.util.Controller;
import kz.greetgo.security.session.SessionIdentity;

import java.util.List;

import static kz.greetgo.sandbox.controller.util.SandboxViews.G_SESSION;

/**
 * как составлять контроллеры написано
 * <a href="https://github.com/greetgo/greetgo.mvc/blob/master/doc/controller_spec.md">здесь</a>
 */
@Bean
@ControllerPrefix("/client")
public class ClientController implements Controller {

    public BeanGetter<ClientRegister> clientRegister;

    @ToJson
    @PublicAccess
    @OnGet("/list")
    public List<ClientRecord> getList(@ParamsTo FilterParams params) {
        return clientRegister.get().getClients(params);
    }

    @ToJson
    @PublicAccess
    @OnGet("/charms")
    public List<Charm> getCharmsList() {
        return clientRegister.get().getCharmsList();
    }

    @ToJson
    @PublicAccess
    @OnGet("/clientDetails/{id}")
    public ClientDetail getDetails(@ParPath("id") int id) {
        return clientRegister.get().getDetails(id);
    }

    @ToJson
    @PublicAccess
    @OnPost("/clientDetails")
    public ClientDetail updateClient(@Par("detail") @Json ClientDetail cd) {
        return clientRegister.get().editClient(cd);
    }

    @PublicAccess
    @OnDelete("/clientDetails/{id}")
    public void deleteClient(@ParPath("id") int id) {
        clientRegister.get().deleteClient(id);
    }

//    @ToJson
//    @PublicAccess
//    @OnPost("/clientDetails/{cd}")
//    public ClientDetail addClient(@ParPath("cd") ClientDetail cd) {
//        return clientRegister.get().editClient(cd);
//    }
}
