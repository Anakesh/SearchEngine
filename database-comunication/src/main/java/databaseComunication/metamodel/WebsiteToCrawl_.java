package databaseComunication.metamodel;

import databaseComunication.entity.WebsiteToCrawl;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * Created by Pavel on 10.02.2019.
 */
@StaticMetamodel(WebsiteToCrawl.class)
public abstract class WebsiteToCrawl_ {
    public static volatile SingularAttribute<WebsiteToCrawl,Long> id;
    public static volatile SingularAttribute<WebsiteToCrawl,String> url;
}
