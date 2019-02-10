package databaseComunication.repository;

import databaseComunication.entity.WebsiteToCrawl;
import org.hibernate.SessionFactory;

import javax.persistence.NoResultException;

public class WebsiteToCrawlRep extends GenericRepository<WebsiteToCrawl> {
    public WebsiteToCrawlRep(SessionFactory sessionFactory) {
        super(sessionFactory, WebsiteToCrawl.class);
    }
    public WebsiteToCrawl popFirst(){
        try {
            WebsiteToCrawl firstPage = firstById();
            deleteById(firstPage.getId());
            return firstPage;
        }catch (NoResultException ex){
            return null;
        }
    }

}
