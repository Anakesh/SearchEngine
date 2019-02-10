package databaseComunication.metamodel;

import databaseComunication.entity.WebPage;
import databaseComunication.entity.WordWeight;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.time.LocalDateTime;

/**
 * Created by Pavel on 10.02.2019.
 */
@StaticMetamodel(WebPage.class)
public abstract class WebPage_ {
    public static volatile SingularAttribute<WebPage,Long> id;
    public static volatile SingularAttribute<WebPage,String> url;
    public static volatile SingularAttribute<WebPage,String> title;
    public static volatile SingularAttribute<WebPage,String> text;
    public static volatile SingularAttribute<WebPage,String> crc;
    public static volatile SingularAttribute<WebPage,Boolean> indexed;
    public static volatile SingularAttribute<WebPage,LocalDateTime> indexDate;
    public static volatile SetAttribute<WebPage, WordWeight> wordWeightSet;
}
