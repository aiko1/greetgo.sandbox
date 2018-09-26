package kz.greetgo.sandbox.register.dao;

import kz.greetgo.sandbox.controller.model.ClientDetail;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface ClientDao {
    @Select("select id, surname, name, patronymic, gender, birth_date as birthDate, actual,\n" +
            "(select name from charm where id=c.charm) as charm,\n" +
            "(select street from client_addr where client=a.client and type='FACT') as factStreet,\n" +
            "(select house from client_addr where client=a.client and type='FACT') as factNo,\n" +
            "(select flat from client_addr where client=a.client and type='FACT') as factFlat,\n" +
            "(select street from client_addr where client=a.client and type='REG') as regStreet,\n" +
            "(select house from client_addr where client=a.client and type='REG') as regNo,\n" +
            "(select flat from client_addr where client=a.client and type='REG') as regFlat,\n" +
            "(select number from client_phone where client=phone.client and type='HOME') as homePhoneNumber,\n" +
            "(select number from client_phone where client=phone.client and type='WORK') as workPhoneNumber,\n" +
            "(SELECT number FROM client_phone where type='MOBILE1' and client=phone.client) as mobileNumber1,\n" +
            "(SELECT number FROM client_phone where type='MOBILE2' and client=phone.client) as mobileNumber2,\n" +
            "(SELECT number FROM client_phone where type='MOBILE3' and client=phone.client) as mobileNumber3\n" +
            "from client c left join client_addr a on c.id = a.client left join client_phone phone on c.id = phone.client\n" +
            "where c.id=#{id} and c.actual=1 limit 1")
    ClientDetail selectClientByID(@Param("id") int id);

    @Select("select id, surname, name, patronymic, gender, birth_date as birthDate, actual,\n" +
            "(select name from charm where id=c.charm) as charm,\n" +
            "(select street from client_addr where client=a.client and type='FACT') as factStreet,\n" +
            "(select house from client_addr where client=a.client and type='FACT') as factNo,\n" +
            "(select flat from client_addr where client=a.client and type='FACT') as factFlat,\n" +
            "(select street from client_addr where client=a.client and type='REG') as regStreet,\n" +
            "(select house from client_addr where client=a.client and type='REG') as regNo,\n" +
            "(select flat from client_addr where client=a.client and type='REG') as regFlat,\n" +
            "(select number from client_phone where client=phone.client and type='HOME') as homePhoneNumber,\n" +
            "(select number from client_phone where client=phone.client and type='WORK') as workPhoneNumber,\n" +
            "(SELECT number FROM client_phone where type='MOBILE1' and client=phone.client) as mobileNumber1,\n" +
            "(SELECT number FROM client_phone where type='MOBILE2' and client=phone.client) as mobileNumber2,\n" +
            "(SELECT number FROM client_phone where type='MOBILE3' and client=phone.client) as mobileNumber3\n" +
            "from client c left join client_addr a on c.id = a.client left join client_phone phone on c.id = phone.client\n" +
            "where c.name=#{name} and c.actual=1 limit 1")
    ClientDetail selectClientByName(@Param("name") String name);

    @Update("update client set actual=0 where id=#{id}")
    void deleteClientByID(@Param("id") int id);

    @Select("select id from charm where name=#{name}")
    int selectCharmIdByName(@Param("name") String name);

    @Select("select max(id) from client")
    int selectNewClientID();

    @Insert("insert into client (surname, name, gender, birth_date, actual, charm) " +
            "values (#{cd.surname}, #{cd.name}, #{cd.gender}, " +
            "#{cd.birthDate}, 1, #{charm})")
    void insertIntoClient(@Param("cd") ClientDetail cd,
                          @Param("charm") int charm);

    @Update("update client set ${fieldName} = #{fieldValue} where id = #{id}")
    void updateClientField(@Param("id") int id,
                           @Param("fieldName") String fieldName,
                           @Param("fieldValue") Object fieldValue);

    @Insert("insert into client_addr (client, type, street, house, flat) " +
            "values (#{cd.id}, 'FACT', #{cd.factStreet}, #{cd.factNo}, #{cd.factFlat})")
    void insertIntoClientAddrFACT(@Param("cd") ClientDetail cd);

    @Update("update client_addr set ${fieldName} = #{fieldValue} where client = #{id} and type = 'FACT'")
    void updateClientAddrFACTField(@Param("id") int id,
                                   @Param("fieldName") String fieldName,
                                   @Param("fieldValue") String fieldValue);

    @Insert("insert into client_addr (client, type, street, house, flat) " +
            "values (#{cd.id}, 'REG', #{cd.regStreet}, #{cd.regNo}, #{cd.regFlat})")
    void insertIntoClientAddrREG(@Param("cd") ClientDetail cd);

    @Update("update client_addr set ${fieldName} = #{fieldValue} where client = #{id} and type = 'REG'")
    void updateClientAddrREGField(@Param("id") int id,
                                  @Param("fieldName") String fieldName,
                                  @Param("fieldValue") String fieldValue);

    @Insert("insert into client_phone (client, type, number) " +
            "values (#{client}, #{type}, #{number})")
    void insertIntoClientPhone(@Param("client") int client,
                               @Param("type") String type,
                               @Param("number") String number);

    @Update("update client_phone set number = #{number} where client = #{id} and type = #{type}")
    void updateClientPhoneNumber(@Param("id") int id,
                                 @Param("number") String number,
                                 @Param("type") String type);

    @Select("select client from client_phone where client=#{client} " +
            "and type in ('MOBILE1', 'MOBILE2', 'MOBILE3') and number=#{number}")
    Integer checkForDublicateMobileNumber(@Param("client") int client,
                                          @Param("number") String number);

    @Select("select client from client_phone where client=#{client} " +
            "and type=#{type}")
    Integer checkForExistPhoneNumberType(@Param("client") int client,
                                         @Param("type") String type);

    @Select("select client from client_addr where client=#{client} and type='FACT'")
    Integer checkIfFactAddressRecordExists(@Param("client") int client);
}