package kz.greetgo.sandbox.register.impl;

import kz.greetgo.depinject.core.BeanGetter;
import kz.greetgo.sandbox.controller.model.*;
import kz.greetgo.sandbox.register.test.dao.ClientTestDao;
import kz.greetgo.sandbox.register.test.util.ParentTestNg;
import kz.greetgo.util.RND;
import org.testng.annotations.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;

import static org.fest.assertions.api.Assertions.assertThat;

public class ClientRegisterImplTest extends ParentTestNg {

    public BeanGetter<ClientTestDao> clientTestDao;
    public BeanGetter<ClientRegisterImpl> clientRegister;

    FilterParams params = new FilterParams();
    List<ClientRecord> clients;

    @Test
    public void getClientList_simple_record() throws ParseException {
        deleteAllClients();

        ClientDetail record = init(RND.str(10), RND.str(10));

        params = new FilterParams();
        clients = clientRegister.get().getClients(params);
        System.out.println(clients.size());

        assertThat(clients).isNotNull();
        assertThat(clients).hasSize(1);
        assertThat(clients.size()).isEqualTo(1);
        assertThat(clients.get(0).id).isEqualTo(record.id);
        assertThat(clients.get(0).fio).isEqualTo(record.surname + " " + record.name);
    }

    @Test
    public void getClientRecords_charm_test() throws ParseException {
        deleteAllClients();

        ClientDetail cd = new ClientDetail("Ivanov", "Petr", "MALE", formatDate("31.01.1993"), 1, "sensitive", "RegStreet",
                "RegHouse", "regFlat", "8-777-555-55-55");

        clientRegister.get().editClient(cd);

        params = new FilterParams();
        clients = clientRegister.get().getClients(params);

        assertThat(clients).isNotNull();
        assertThat(clients).hasSize(1);
        assertThat(clients.get(0).charm).isEqualTo("sensitive");
    }

    @Test
    public void getClientRecords_age_compute_test() throws ParseException {
        deleteAllClients();

        ClientDetail cd1 = new ClientDetail("Ivanov", "Petr", "MALE", formatDate("31.01.1993"), 1, "sensitive", "RegStreet",
                "RegHouse", "regFlat", "8-777-555-55-55");

        clientRegister.get().editClient(cd1);

        ClientDetail cd2 = new ClientDetail("Kim", "Petr", "MALE", formatDate("31.09.1998"), 1, "sensitive", "RegStreet",
                "RegHouse", "regFlat", "8-777-555-55-55");

        clientRegister.get().editClient(cd2);

        params = new FilterParams();
        params.sortBy = SortBy.FIO.toString();
        params.sortDir = SortDir.ASC.toString();
        clients = clientRegister.get().getClients(params);

        assertThat(clients).isNotNull();
        assertThat(clients).hasSize(2);
        assertThat(clients.get(0).age).isEqualTo(25);
        assertThat(clients.get(1).age).isEqualTo(19);
    }

    @Test
    public void getClientList_filter() throws ParseException {
        deleteAllClients();

        ClientDetail record1 = init("Ivanov", "Ivan");
        ClientDetail record2 = init("Petrov", "Ivan");
        ClientDetail record3 = init("Kim", "Petr");

        params = new FilterParams();
        params.filterCol = FilterBy.NAME.toString();
        params.filter = "Iv";
        clients = clientRegister.get().getClients(params);

        assertThat(clients).isNotNull();
        assertThat(clients).hasSize(2);
        assertThat(clients.get(0).name).isEqualTo(record1.name);
        assertThat(clients.get(1).name).isEqualTo(record2.name);
        assertThat(clients.get(0).surname).isNotEqualTo(record3.surname);
        assertThat(clients.get(1).surname).isNotEqualTo(record3.surname);
        assertThat(Objects.equals((clients.get(1)), record2));
    }

    @Test
    public void getClientList_sort_desc() throws ParseException {
        deleteAllClients();

        ClientDetail record1 = init("Ivanov", "Ivan");
        ClientDetail record2 = init("Petrov", "A");
        ClientDetail record3 = init("Coi", "B");
        ClientDetail record4 = init("Anutin", "Petr");
        ClientDetail record5 = init("Li", "C");
        ClientDetail record6 = init("Kim", "D");

        params = new FilterParams();
        params.sortBy = SortBy.NAME.toString();
        params.sortDir = SortDir.DESC.toString();
        clients = clientRegister.get().getClients(params);

        assertThat(clients).isNotNull();
        assertThat(clients).hasSize(6);
        assertThat(clients.get(0).fio).isEqualTo(record4.surname + " " + record4.name);
        assertThat(clients.get(5).fio).isEqualTo(record2.surname + " " + record2.name);
    }

    @Test
    public void getClientList_sort_asc_fio() throws ParseException {
        deleteAllClients();

        ClientDetail record1 = init("Ivanov", "Ivan");
        ClientDetail record2 = init("Petrov", "Ivan");
        ClientDetail record3 = init("Coi", "Petr");
        ClientDetail record4 = init("Anutin", "Petr");
        ClientDetail record5 = init("Li", "Petr");
        ClientDetail record6 = init("Kim", "Petr");

        params = new FilterParams();
        params.sortBy = SortBy.FIO.toString();
        params.sortDir = SortDir.ASC.toString();
        clients = clientRegister.get().getClients(params);

        assertThat(clients).isNotNull();
        assertThat(clients).hasSize(6);
        assertThat(clients.get(0).fio).isEqualTo(record4.surname + " " + record4.name);
        assertThat(clients.get(5).fio).isEqualTo(record2.surname + " " + record2.name);
    }

    @Test
    public void getClientList_sort_asc_charm() throws ParseException {
        deleteAllClients();

        ClientDetail record1 = init_with_charm("Ivanov", "Ivan", "polite");
        ClientDetail record2 = init_with_charm("Petrov", "Ivan", "ambitious");
        ClientDetail record3 = init_with_charm("Coi", "Petr", "quiet");
        ClientDetail record4 = init_with_charm("Anutin", "Petr", "artistic");
        ClientDetail record5 = init_with_charm("Li", "Petr", "patient");
        ClientDetail record6 = init_with_charm("Kim", "Petr", "aggressive");

        params = new FilterParams();
        params.sortBy = SortBy.CHARM.toString();
        params.sortDir = SortDir.ASC.toString();
        clients = clientRegister.get().getClients(params);

        assertThat(clients).isNotNull();
        assertThat(clients).hasSize(6);
        assertThat(clients.get(0).charm).isEqualTo(record6.charm);
        assertThat(clients.get(5).charm).isEqualTo(record3.charm);
    }

    @Test
    public void getClientList_sort_desc_charm() throws ParseException {
        deleteAllClients();

        ClientDetail record1 = init_with_charm("Ivanov", "Ivan", "polite");
        ClientDetail record2 = init_with_charm("Petrov", "Ivan", "ambitious");
        ClientDetail record3 = init_with_charm("Coi", "Petr", "quiet");
        ClientDetail record4 = init_with_charm("Anutin", "Petr", "artistic");
        ClientDetail record5 = init_with_charm("Li", "Petr", "patient");
        ClientDetail record6 = init_with_charm("Kim", "Petr", "aggressive");

        params = new FilterParams();
        params.sortBy = SortBy.CHARM.toString();
        params.sortDir = SortDir.DESC.toString();
        clients = clientRegister.get().getClients(params);

        assertThat(clients).isNotNull();
        assertThat(clients).hasSize(6);
        assertThat(clients.get(0).charm).isEqualTo(record3.charm);
        assertThat(clients.get(5).charm).isEqualTo(record6.charm);
    }

    @Test
    public void getClientList_sort_asc_totalBalance() throws ParseException {
        deleteAllClients();

        ClientDetail record1 = init_with_charm("Ivanov", "Ivan", "polite");
        ClientDetail record2 = init_with_charm("Petrov", "Ivan", "ambitious");
        ClientDetail record3 = init_with_charm("Coi", "Petr", "quiet");
        ClientDetail record4 = init_with_charm("Anutin", "Petr", "artistic");
        ClientDetail record5 = init_with_charm("Li", "Petr", "patient");
        ClientDetail record6 = init_with_charm("Kim", "Petr", "aggressive");

        params = new FilterParams();
        params.sortBy = SortBy.TOTALBALANCE.toString();
        params.sortDir = SortDir.ASC.toString();
        clients = clientRegister.get().getClients(params);

        assertThat(clients).isNotNull();
        assertThat(clients).hasSize(6);
    }

    @Test
    public void getClientList_sort_filter() throws ParseException {
        deleteAllClients();

        ClientDetail record1 = init("Ivanov", "Ivan");
        ClientDetail record2 = init("Petrov", "A");
        ClientDetail record3 = init("Coi", "B");
        ClientDetail record4 = init("Petrova", "Petr");
        ClientDetail record5 = init("Li", "C");
        ClientDetail record6 = init("Kim", "D");

        params = new FilterParams();
        params.filterCol = FilterBy.SURNAME.toString();
        params.filter = "P";
        params.sortBy = SortBy.FIO.toString();
        params.sortDir = SortDir.ASC.toString();
        clients = clientRegister.get().getClients(params);

        assertThat(clients).isNotNull();
        assertThat(clients).hasSize(2);
        assertThat(clients.get(0).fio).isEqualTo(record2.surname + " " + record2.name);
        assertThat(clients.get(1).fio).isEqualTo(record4.surname + " " + record4.name);
    }

    @Test
    public void getClientList_sort_filter_desc() throws ParseException {
        deleteAllClients();

        ClientDetail record1 = init("Ivanov", "Ivan");
        ClientDetail record2 = init("Petrov", "A");
        ClientDetail record3 = init("Coi", "B");
        ClientDetail record4 = init("Petrova", "AA");
        ClientDetail record5 = init("Li", "C");
        ClientDetail record6 = init("Kim", "AAA");

        params = new FilterParams();
        params.filterCol = FilterBy.NAME.toString();
        params.filter = "A";
        params.sortBy = SortBy.FIO.toString();
        params.sortDir = SortDir.DESC.toString();
        clients = clientRegister.get().getClients(params);

        assertThat(clients).isNotNull();
        assertThat(clients).hasSize(3);
        assertThat(clients.get(0).fio).isEqualTo(record4.surname + " " + record4.name);
        assertThat(clients.get(1).fio).isEqualTo(record2.surname + " " + record2.name);
        assertThat(clients.get(2).fio).isEqualTo(record6.surname + " " + record6.name);
    }

    @Test
    public void limit_test() throws ParseException {
        deleteAllClients();

        ClientDetail record1 = init("Ivanov", "Ivan");
        ClientDetail record2 = init("Petrov", "A");
        ClientDetail record3 = init("Coi", "B");
        ClientDetail record4 = init("Anutin", "Petr");
        ClientDetail record5 = init("Li", "C");
        ClientDetail record6 = init("Kim", "D");

        params = new FilterParams();
        params.limit = 2;
        clients = clientRegister.get().getClients(params);

        assertThat(clients).isNotNull();
        assertThat(clients).hasSize(2);
    }

    @Test
    public void offset_test() throws ParseException {
        deleteAllClients();

        ClientDetail record1 = init("Ivanov", "Ivan");
        ClientDetail record2 = init("Petrov", "A");
        ClientDetail record3 = init("Coi", "B");
        ClientDetail record4 = init("Anutin", "Petr");
        ClientDetail record5 = init("Li", "C");
        ClientDetail record6 = init("Kim", "D");

        params = new FilterParams();
        params.offset = 2;
        clients = clientRegister.get().getClients(params);

        assertThat(clients).isNotNull();
        assertThat(clients).hasSize(4);
    }

    @Test
    public void limit_offset_test() throws ParseException {
        deleteAllClients();

        ClientDetail record1 = init("Ivanov", "Ivan");
        ClientDetail record2 = init("Petrov", "A");
        ClientDetail record3 = init("Coi", "B");
        ClientDetail record4 = init("Anutin", "Petr");
        ClientDetail record5 = init("Li", "C");
        ClientDetail record6 = init("Kim", "D");

        params = new FilterParams();
        params.limit = 3;
        params.offset = 4;
        clients = clientRegister.get().getClients(params);

        assertThat(clients).isNotNull();
        assertThat(clients).hasSize(2);
    }

    @Test
    public void sort_limit_offset_test() throws ParseException {
        deleteAllClients();

        ClientDetail record1 = init("Ivanov", "Ivan");//3
        ClientDetail record2 = init("Petrov", "A");//6
        ClientDetail record3 = init("Coi", "B");//2
        ClientDetail record4 = init("Anutin", "Petr");//1
        ClientDetail record5 = init("Li", "C");//5
        ClientDetail record6 = init("Kim", "D");//4

        params = new FilterParams();
        params.sortBy = SortBy.FIO.toString();
        params.sortDir = SortDir.ASC.toString();
        params.limit = 3;
        params.offset = 2;//3-5
        clients = clientRegister.get().getClients(params);

        assertThat(clients).isNotNull();
        assertThat(clients).hasSize(3);
        assertThat(clients.get(0).id).isEqualTo(record1.id);
        assertThat(clients.get(1).name).isEqualTo(record6.name);
        assertThat(clients.get(2).surname).isEqualTo(record5.surname);
    }

    @Test
    public void sort_filter_limit_offset_test() throws ParseException {
        deleteAllClients();

        ClientDetail record1 = init("Ivanov", "Ivan");//4
        ClientDetail record2 = init("Petrov", "A");//1
        ClientDetail record3 = init("Coi", "B");//5
        ClientDetail record4 = init("Anutin", "Petr");//6
        ClientDetail record5 = init("Ka", "C");//3
        ClientDetail record6 = init("Kim", "D");//2

        params = new FilterParams();
        params.sortBy = SortBy.FIO.toString();
        params.sortDir = SortDir.DESC.toString();
        params.filterCol = FilterBy.SURNAME.toString();
        params.filter = "K";
        params.limit = 3;
        params.offset = 1;//3
        clients = clientRegister.get().getClients(params);

        assertThat(clients).isNotNull();
        assertThat(clients).hasSize(1);
        assertThat(clients.get(0).id).isEqualTo(record5.id);
    }

    @Test
    public void deleteClient_test() throws ParseException {
        deleteAllClients();

        ClientDetail record1 = init("Ivanov", "Ivan");
        ClientDetail record2 = init("Petrov", "A");
        //
        ClientDetail record3 = init("Coi", "B");
        //
        ClientDetail record4 = init("Anutin", "Petr");
        ClientDetail record5 = init("Li", "C");
        ClientDetail record6 = init("Kim", "D");

        clientRegister.get().deleteClient(record3.id);
        params = new FilterParams();
        clients = clientRegister.get().getClients(params);

        assertThat(clients).isNotNull();
        assertThat(clients).hasSize(5);
        assertThat(clients.get(0).fio).isNotEqualTo(record3.surname + " " + record3.name);
        assertThat(clients.get(1).fio).isNotEqualTo(record3.surname + " " + record3.name);
        assertThat(clients.get(2).fio).isNotEqualTo(record3.surname + " " + record3.name);
        assertThat(clients.get(3).fio).isNotEqualTo(record3.surname + " " + record3.name);
        assertThat(clients.get(4).fio).isNotEqualTo(record3.surname + " " + record3.name);
    }

    @Test
    public void addClient_test() throws ParseException {
        deleteAllClients();
        //init data

        ClientDetail cd = new ClientDetail("Ivanov", "Petr", "MALE", formatDate("31.01.1993"), 1, "sensitive", "RegStreet",
                "RegHouse", "regFlat", "8-777-555-55-55");
        cd.id = 0;
//        clientTestDao.get().updateClientField(id, "charm", "(select id from charm where name='" + charm + "')");
        cd.mobileNumber3 = "4";

        //call testing method
        clientRegister.get().editClient(cd);

        //get from test dao
        ClientDetail cdTest = clientTestDao.get().selectClientByName("Petr");
        System.out.println(cdTest.mobileNumber1);
        System.out.println(cdTest.birthDate);

        //test
        assertThat(cdTest).isNotNull();
        assertThat(Objects.equals(cdTest.surname, cd.surname));
        assertThat(cdTest.mobileNumber3).isEqualTo("4");
        assertThat(Objects.equals(cdTest.name, cd.name));
        assertThat(Objects.equals(cdTest.mobileNumber1, cd.mobileNumber1));
    }

    @Test
    public void getAddedClient_test() throws ParseException {
        deleteAllClients();
        //init data

        ClientDetail cd = new ClientDetail("Ivanov", "Petr", "MALE", formatDate("31.01.1993"), 1, "sensitive", "RegStreet",
                "RegHouse", "regFlat", "8-777-555-55-55");
        cd.id = 0;
//        clientTestDao.get().updateClientField(id, "charm", "(select id from charm where name='" + charm + "')");
        cd.mobileNumber3 = "4";
        int c = clientTestDao.get().selectCharmIdByName(cd.charm);
        clientTestDao.get().insertTestClient(cd, c);
        cd.id = clientTestDao.get().selectNewClientID();
        clientTestDao.get().insertTestAddrREG(cd);
        clientTestDao.get().insertTestPhone(cd);

        //call testing method
        ClientRecord cr = clientRegister.get().getAddedClient();

        //test
        assertThat(cr).isNotNull();
        assertThat(cd.name).isEqualTo(cr.name);
        assertThat(cd.charm).isEqualTo(cr.charm);
        assertThat(cr.surname).isEqualTo(cd.surname);
    }

    @Test
    public void editClient_test() throws ParseException {
        deleteAllClients();

        //init data
        ClientDetail cd = new ClientDetail("Igoreva", "Natazha",
                "FEMALE", formatDate("17.04.1986"), 1, "sensitive",
                "s84", "f84", "h84", "87002000025");

        int c = clientTestDao.get().selectCharmIdByName(cd.charm);
        clientTestDao.get().insertTestClient(cd, c);
        cd.id = clientTestDao.get().selectNewClientID();
        System.out.println(cd.id);
        clientTestDao.get().insertTestAddrREG(cd);
        clientTestDao.get().insertTestPhone(cd);

        ClientDetail cdTest = clientTestDao.get().selectClientByID(cd.id);
        System.out.println(cdTest);
        cdTest.name = "Nada";
        cdTest.mobileNumber1 = "81112223335";
        cdTest.mobileNumber3 = "55555555";
        cdTest.factStreet = "test street";

        //test
        clientRegister.get().editClient(cdTest);
        ClientDetail cdTest1 = clientTestDao.get().selectClientByID(cdTest.id);
        System.out.println(cdTest1.mobileNumber1);
        System.out.println(cdTest1.birthDate);

        assertThat(cdTest1).isNotNull();
        assertThat(cdTest.id).isEqualTo(cdTest1.id);
        assertThat(cdTest1.id).isEqualTo(cd.id);
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
        ClientDetail cd = new ClientDetail("Igoreva", "Natazha",
                "FEMALE", java.sql.Date.valueOf("1986-04-17"), 1, "sensitive",
                "s84", "f84", "h84", "87002000025");
        cd.id = 0;
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
        ClientDetail cd = new ClientDetail("Igoreva", "Natazha",
                "FEMALE", java.sql.Date.valueOf("1986-04-17"), 1, "sensitive",
                "s84", "f84", "h84", "87002000025");
        int c = clientTestDao.get().selectCharmIdByName(cd.charm);
        clientTestDao.get().insertTestClient(cd, c);
        cd.id = clientTestDao.get().selectNewClientID();

        clientTestDao.get().insertTestAddrREG(cd);
        clientTestDao.get().insertTestPhone(cd);

        ClientDetail cdTest = clientTestDao.get().selectClientByName("Natazha");

        cdTest.mobileNumber2 = "22";
        cdTest.mobileNumber3 = "22";
        cdTest.factStreet = "test street";

        //test
        clientRegister.get().editClient(cdTest);
        ClientDetail cdTest1 = clientTestDao.get().selectClientByID(cdTest.id);
        System.out.println(cdTest1.mobileNumber1);

        assertThat(cdTest1).isNotNull();
        assertThat(cdTest.id).isEqualTo(cdTest1.id);
        assertThat(cdTest1.mobileNumber2).isEqualTo("22");
        assertThat(cdTest1.mobileNumber3).isEqualTo(null);
        assertThat(cdTest1.factStreet).isEqualTo("test street");
    }

    private void deleteAllClients() {
        clientTestDao.get().deleteAllClients();
    }

    private ClientDetail init(String surname, String name) throws ParseException {
        ClientDetail cd = new ClientDetail(surname, name,
                "FEMALE", formatDate("17.04.1986"), 1, "sensitive",
                "s84", "f84", "h84", "87002000025");
        int c = clientTestDao.get().selectCharmIdByName(cd.charm);

        clientTestDao.get().insertTestClient(cd, c);
        cd.id = clientTestDao.get().selectNewClientID();

        clientTestDao.get().insertTestAddrREG(cd);
        clientTestDao.get().insertTestPhone(cd);
        return cd;
    }

    private ClientDetail init_with_charm(String surname, String name, String charm) throws ParseException {

        ClientDetail cd = new ClientDetail(surname, name,
                "FEMALE", formatDate("17.04.1986"), 1, charm,
                "s84", "f84", "h84", "87002000025");
        int c = clientTestDao.get().selectCharmIdByName(cd.charm);
        clientTestDao.get().insertTestClient(cd, c);
        cd.id = clientTestDao.get().selectNewClientID();
        clientTestDao.get().insertTestAddrREG(cd);
        clientTestDao.get().insertTestPhone(cd);

        return cd;
    }

    java.sql.Date formatDate(String birthDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        java.util.Date date = sdf.parse(birthDate);
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        return sqlDate;
    }

}
