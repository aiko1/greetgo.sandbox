package kz.greetgo.sandbox.controller.controller;

import kz.greetgo.depinject.core.Bean;
import kz.greetgo.depinject.core.BeanGetter;
import kz.greetgo.mvc.annotations.*;
import kz.greetgo.mvc.annotations.on_methods.ControllerPrefix;
import kz.greetgo.mvc.annotations.on_methods.OnGet;
import kz.greetgo.mvc.annotations.on_methods.OnPost;
import kz.greetgo.mvc.interfaces.TunnelCookies;
import kz.greetgo.sandbox.controller.model.ClientDetail;
import kz.greetgo.sandbox.controller.model.ClientRecord;
import kz.greetgo.sandbox.controller.model.FilterParams;
import kz.greetgo.sandbox.controller.model.PersonDisplay;
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
    @OnGet("/clientDetails/{id}")
    public ClientDetail getDetails(@ParPath("id") int id) {
        return clientRegister.get().getDetails(id);
    }
}
