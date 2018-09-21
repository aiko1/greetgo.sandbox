package kz.greetgo.sandbox.register.impl;

import kz.greetgo.depinject.core.BeanGetter;
import kz.greetgo.sandbox.controller.model.*;
import kz.greetgo.sandbox.register.test.dao.ClientTestDao;
import kz.greetgo.sandbox.register.test.util.ParentTestNg;
import kz.greetgo.util.RND;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Objects;

import static org.fest.assertions.api.Assertions.assertThat;

public class ClientRegisterImplTest extends ParentTestNg {

    public BeanGetter<ClientTestDao> clientTestDao;
    public BeanGetter<ClientRegisterImpl> clientRegister;

    FilterParams params = new FilterParams();
    List<ClientRecord> clients;

    @Test
    public void getClientList_simple_record() {
        deleteAllClients();

        ClientRecord record = init(RND.plusInt(1000000), RND.str(10), RND.str(10));

        params.filter = "";
        params.filterCol = "";
        params.sortBy = "";
        params.sortDir = "";
        clients = clientRegister.get().getClients(params);

        assertThat(clients).isNotNull();
        assertThat(clients).hasSize(1);
        assertThat(clients.get(0).id).isEqualTo(record.id);
        assertThat(clients.get(0).fio).isEqualTo(record.fio);
    }

    @Test
    public void getClientList_filter() {
        deleteAllClients();

        ClientRecord record1 = init(RND.plusInt(1000000), "Ivanov", "Ivan");
        ClientRecord record2 = init(RND.plusInt(1000000), "Petrov", "Ivan");
        ClientRecord record3 = init(RND.plusInt(1000000), "Petrov", "Petr");

        params.filterCol = FilterBy.NAME.toString();
        params.filter = "iv";
        params.sortBy = "";
        params.sortDir = "";
        clients = clientRegister.get().getClients(params);

        assertThat(clients).isNotNull();
        assertThat(clients).hasSize(2);
        assertThat(clients.get(0).fio).isEqualTo(record1.fio);
        assertThat(clients.get(1).fio).isEqualTo(record2.fio);
        assertThat(clients.get(0).surname).isNotEqualTo(record3.surname);
        assertThat(clients.get(1).surname).isNotEqualTo(record3.surname);
        assertThat(Objects.equals((clients.get(1)), record2));
    }

    @Test
    public void getClientList_sort_asc() {
        deleteAllClients();

        ClientRecord record1 = init(RND.plusInt(1000000), "Ivanov", "Ivan");
        ClientRecord record2 = init(RND.plusInt(1000000), "Petrov", "Ivan");
        ClientRecord record3 = init(RND.plusInt(1000000), "Coi", "Petr");
        ClientRecord record4 = init(RND.plusInt(1000000), "Anutin", "Petr");
        ClientRecord record5 = init(RND.plusInt(1000000), "Li", "Petr");
        ClientRecord record6 = init(RND.plusInt(1000000), "Kim", "Petr");

        params.filterCol = "";
        params.filter = "";
        params.sortBy = SortBy.SURNAME.toString();
        params.sortDir = SortDir.ASC.toString();
        clients = clientRegister.get().getClients(params);

        assertThat(clients).isNotNull();
        assertThat(clients).hasSize(6);
        assertThat(clients.get(0).fio).isEqualTo(record4.fio);
        assertThat(clients.get(5).fio).isEqualTo(record2.fio);
    }

    @Test
    public void getClientList_sort_desc() {
        deleteAllClients();

        ClientRecord record1 = init(RND.plusInt(1000000), "Ivanov", "Ivan");
        ClientRecord record2 = init(RND.plusInt(1000000), "Petrov", "A");
        ClientRecord record3 = init(RND.plusInt(1000000), "Coi", "B");
        ClientRecord record4 = init(RND.plusInt(1000000), "Anutin", "Petr");
        ClientRecord record5 = init(RND.plusInt(1000000), "Li", "C");
        ClientRecord record6 = init(RND.plusInt(1000000), "Kim", "D");

        params.filterCol = "";
        params.filter = "";
        params.sortBy = SortBy.NAME.toString();
        params.sortDir = SortDir.DESC.toString();
        clients = clientRegister.get().getClients(params);

        assertThat(clients).isNotNull();
        assertThat(clients).hasSize(6);
        assertThat(clients.get(0).fio).isEqualTo(record4.fio);
        assertThat(clients.get(5).fio).isEqualTo(record2.fio);
    }

    @Test
    public void getClientList_sort_filter() {
        deleteAllClients();

        ClientRecord record1 = init(RND.plusInt(1000000), "Ivanov", "Ivan");
        ClientRecord record2 = init(RND.plusInt(1000000), "Petrov", "A");
        ClientRecord record3 = init(RND.plusInt(1000000), "Coi", "B");
        ClientRecord record4 = init(RND.plusInt(1000000), "Petrova", "Petr");
        ClientRecord record5 = init(RND.plusInt(1000000), "Li", "C");
        ClientRecord record6 = init(RND.plusInt(1000000), "Kim", "D");

        params.filterCol = FilterBy.SURNAME.toString();
        params.filter = "p";
        params.sortBy = SortBy.NAME.toString();
        params.sortDir = SortDir.ASC.toString();
        clients = clientRegister.get().getClients(params);

        assertThat(clients).isNotNull();
        assertThat(clients).hasSize(2);
        assertThat(clients.get(0).fio).isEqualTo(record2.fio);
        assertThat(clients.get(1).fio).isEqualTo(record4.fio);
    }

    @Test
    public void getClientList_sort_filter_desc() {
        deleteAllClients();

        ClientRecord record1 = init(RND.plusInt(1000000), "Ivanov", "Ivan");
        ClientRecord record2 = init(RND.plusInt(1000000), "Petrov", "A");
        ClientRecord record3 = init(RND.plusInt(1000000), "Coi", "B");
        ClientRecord record4 = init(RND.plusInt(1000000), "Petrova", "AA");
        ClientRecord record5 = init(RND.plusInt(1000000), "Li", "C");
        ClientRecord record6 = init(RND.plusInt(1000000), "Kim", "AAA");

        params.filterCol = FilterBy.NAME.toString();
        params.filter = "a";
        params.sortBy = SortBy.SURNAME.toString();
        params.sortDir = SortDir.DESC.toString();
        clients = clientRegister.get().getClients(params);

        assertThat(clients).isNotNull();
        assertThat(clients).hasSize(3);
        assertThat(clients.get(0).fio).isEqualTo(record4.fio);
        assertThat(clients.get(1).fio).isEqualTo(record2.fio);
        assertThat(clients.get(2).fio).isEqualTo(record6.fio);
    }

    @Test
    public void deleteClient_test() {
        deleteAllClients();

        ClientRecord record1 = init(RND.plusInt(1000000), "Ivanov", "Ivan");
        ClientRecord record2 = init(RND.plusInt(1000000), "Petrov", "A");
        //
        ClientRecord record3 = init(RND.plusInt(1000000), "Coi", "B");
        //
        ClientRecord record4 = init(RND.plusInt(1000000), "Anutin", "Petr");
        ClientRecord record5 = init(RND.plusInt(1000000), "Li", "C");
        ClientRecord record6 = init(RND.plusInt(1000000), "Kim", "D");

        clientRegister.get().deleteClient(record3.id);
        params.filterCol = "";
        params.filter = "";
        params.sortBy = "";
        params.sortDir = "";
        clients = clientRegister.get().getClients(params);

        assertThat(clients).isNotNull();
        assertThat(clients).hasSize(5);
        assertThat(clients.get(0).fio).isNotEqualTo(record3.fio);
        assertThat(clients.get(1).fio).isNotEqualTo(record3.fio);
        assertThat(clients.get(2).fio).isNotEqualTo(record3.fio);
        assertThat(clients.get(3).fio).isNotEqualTo(record3.fio);
        assertThat(clients.get(4).fio).isNotEqualTo(record3.fio);
    }

    @Test
    public void addClient_test() {
        deleteAllClients();
        //init data

        ClientDetail cd = new ClientDetail(0, "Ivanov", "Petr", "MALE", java.sql.Date.valueOf("1993-01-31"), 1, 5, "RegStreet",
                "RegHouse", "regFlat", "8-777-555-55-55");
        cd.mobileNumber3 = "4";

        //call testing method
        clientRegister.get().editClient(cd);

        //get from test dao
        ClientDetail cdTest = clientTestDao.get().selectClientByName("Petr");
        System.out.println(cdTest.mobileNumber1);

        //test
        assertThat(cdTest).isNotNull();
        assertThat(Objects.equals(cdTest.surname, cd.surname));
        assertThat(cdTest.mobileNumber3).isEqualTo("4");
        assertThat(Objects.equals(cdTest.name, cd.name));
        assertThat(Objects.equals(cdTest.mobileNumber1, cd.mobileNumber1));
    }

    @Test
    public void editClient_test() {
        deleteAllClients();

        //init data
        int id = RND.plusInt(1000000);
        ClientDetail cd = new ClientDetail(id, "Igoreva", "Natazha",
                "FEMALE", java.sql.Date.valueOf("1986-04-17"), 1, 5,
                "s84", "f84", "h84", "87002000025");
        clientTestDao.get().insertTestClient(cd);
        clientTestDao.get().insertTestAddrREG(cd);
        clientTestDao.get().insertTestPhone(cd);

        ClientDetail cdTest = clientTestDao.get().selectClientByID(id);
        cdTest.name = "Nada";
        cdTest.mobileNumber1 = "81112223335";
        cdTest.mobileNumber3 = "55555555";
        cdTest.factStreet = "test street";

        //test
        clientRegister.get().editClient(cdTest);
        ClientDetail cdTest1 = clientTestDao.get().selectClientByID(cdTest.id);
        System.out.println(cdTest1.mobileNumber1);

        assertThat(cdTest1).isNotNull();
        assertThat(cdTest.id).isEqualTo(cdTest1.id);
        assertThat(cdTest1.id).isEqualTo(id);
        assertThat(cdTest1.name).isEqualTo("Nada");
        assertThat(cdTest1.mobileNumber1).isEqualTo("81112223335");
        assertThat(cdTest1.mobileNumber3).isEqualTo("55555555");
        assertThat(cdTest1.factStreet).isEqualTo("test street");
        assertThat(cdTest1.surname).isEqualTo(cd.surname);
        assertThat(cdTest1.gender).isEqualTo(cd.gender);
        assertThat(cdTest1.charm).isEqualTo(cd.charm);
        assertThat(cdTest1.regStreet).isEqualTo(cd.regStreet);
        assertThat(cdTest1.regNo).isEqualTo(cd.regNo);
        assertThat(cdTest1.regFlat).isEqualTo(cd.regFlat);
        assertThat(cdTest1.mobileNumber1).isNotEqualTo(cd.mobileNumber1);
    }

    @Test
    public void checkForDublicateMobileNumber_add_test() {
        //init
        deleteAllClients();

        //init data
        ClientDetail cd = new ClientDetail(0, "Igoreva", "Natazha",
                "FEMALE", java.sql.Date.valueOf("1986-04-17"), 1, 5,
                "s84", "f84", "h84", "87002000025");
        cd.mobileNumber2 = "87002000025";
        cd.mobileNumber3 = "87002000025";

        //perform
        clientRegister.get().editClient(cd);

        ClientDetail test = clientTestDao.get().selectClientByName("Natazha");

        //test
        assertThat(test).isNotNull();
        assertThat(test.mobileNumber2).isNull();
        assertThat(test.mobileNumber3).isNull();
        assertThat(test.mobileNumber1).isEqualTo("87002000025");
    }

    @Test
    public void checkForDublicateMobileNumber_edit_test() {
        deleteAllClients();

        //init data
        int id = RND.plusInt(1000000);
        ClientDetail cd = new ClientDetail(id, "Igoreva", "Natazha",
                "FEMALE", java.sql.Date.valueOf("1986-04-17"), 1, 5,
                "s84", "f84", "h84", "87002000025");
        clientTestDao.get().insertTestClient(cd);
        clientTestDao.get().insertTestAddrREG(cd);
        clientTestDao.get().insertTestPhone(cd);

        ClientDetail cdTest = clientTestDao.get().selectClientByID(id);

        cdTest.mobileNumber2 = "22";
        cdTest.mobileNumber3 = "22";
        cdTest.factStreet = "test street";

        //test
        clientRegister.get().editClient(cdTest);
        ClientDetail cdTest1 = clientTestDao.get().selectClientByID(cdTest.id);
        System.out.println(cdTest1.mobileNumber1);

        assertThat(cdTest1).isNotNull();
        assertThat(cdTest.id).isEqualTo(cdTest1.id);
        assertThat(cdTest1.id).isEqualTo(id);
        assertThat(cdTest1.mobileNumber2).isEqualTo("22");
        assertThat(cdTest1.mobileNumber3).isEqualTo(null);
        assertThat(cdTest1.factStreet).isEqualTo("test street");
    }

    private void deleteAllClients() {
        clientTestDao.get().deleteAllClients();
    }

    private ClientRecord init(int id, String surname, String name) {
        ClientRecord record = new ClientRecord();
        record.fio = surname + " " + name;
        record.id = id;
        record.name = name;
        record.surname = surname;
        clientTestDao.get().insertNotFullClient(record.id, surname, name, 1);
        return record;
    }

}
