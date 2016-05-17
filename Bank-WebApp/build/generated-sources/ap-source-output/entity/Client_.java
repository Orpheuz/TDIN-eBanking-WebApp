package entity;

import entity.Operation;
import entity.Stock;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-05-17T20:45:02")
@StaticMetamodel(Client.class)
public class Client_ { 

    public static volatile ListAttribute<Client, Operation> operationList;
    public static volatile SingularAttribute<Client, Integer> clientId;
    public static volatile ListAttribute<Client, Stock> stockList;
    public static volatile SingularAttribute<Client, String> name;
    public static volatile SingularAttribute<Client, String> email;

}