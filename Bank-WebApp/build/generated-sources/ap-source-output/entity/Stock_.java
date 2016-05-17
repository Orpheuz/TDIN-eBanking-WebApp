package entity;

import entity.Client;
import entity.Company;
import entity.Operation;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-05-17T20:45:02")
@StaticMetamodel(Stock.class)
public class Stock_ { 

    public static volatile SingularAttribute<Stock, Boolean> forsale;
    public static volatile ListAttribute<Stock, Operation> operationList;
    public static volatile SingularAttribute<Stock, String> quantity;
    public static volatile SingularAttribute<Stock, Client> fkOwnerId;
    public static volatile SingularAttribute<Stock, Integer> stockId;
    public static volatile SingularAttribute<Stock, Company> fkCompanyId;

}