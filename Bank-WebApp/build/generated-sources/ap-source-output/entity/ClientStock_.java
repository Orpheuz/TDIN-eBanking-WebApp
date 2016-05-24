package entity;

import entity.Client;
import entity.ClientStockPK;
import entity.Stock;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-05-24T16:59:53")
@StaticMetamodel(ClientStock.class)
public class ClientStock_ { 

    public static volatile SingularAttribute<ClientStock, ClientStockPK> clientStockPK;
    public static volatile SingularAttribute<ClientStock, Integer> quantity;
    public static volatile SingularAttribute<ClientStock, Client> client;
    public static volatile SingularAttribute<ClientStock, Stock> stock;

}