package entity;

import entity.Stock;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-05-17T20:45:02")
@StaticMetamodel(Company.class)
public class Company_ { 

    public static volatile SingularAttribute<Company, Double> stockValue;
    public static volatile SingularAttribute<Company, Integer> companyId;
    public static volatile SingularAttribute<Company, String> acronym;
    public static volatile ListAttribute<Company, Stock> stockList;
    public static volatile SingularAttribute<Company, String> name;

}