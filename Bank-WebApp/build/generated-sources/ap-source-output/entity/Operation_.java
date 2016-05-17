package entity;

import entity.Client;
import entity.Stock;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-05-17T20:45:02")
@StaticMetamodel(Operation.class)
public class Operation_ { 

    public static volatile SingularAttribute<Operation, Date> operationDate;
    public static volatile SingularAttribute<Operation, Double> operationStockValue;
    public static volatile ListAttribute<Operation, Stock> stockList;
    public static volatile SingularAttribute<Operation, Client> fkOwnerId;
    public static volatile SingularAttribute<Operation, Date> executionDate;
    public static volatile SingularAttribute<Operation, Integer> operationId;
    public static volatile SingularAttribute<Operation, Boolean> operationType;
    public static volatile SingularAttribute<Operation, String> state;

}