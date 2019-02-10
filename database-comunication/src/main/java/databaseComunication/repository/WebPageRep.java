package databaseComunication.repository;

import databaseComunication.entity.WebPage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.RollbackException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by Pavel on 02.02.2019.
 */
public class WebPageRep extends GenericRepository<WebPage> {
    public WebPageRep(SessionFactory sessionFactory) {
        super(sessionFactory, WebPage.class);
    }

    public WebPage getFirstNotIndexed(){
        Session session = getSession();
        EntityManager entityManager = session.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = null;
        WebPage webPage;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<WebPage> entityCriteria =  criteriaBuilder.createQuery(WebPage.class);
            Root<WebPage> entityRoot = entityCriteria.from(WebPage.class);
            entityCriteria.select(entityRoot);
            entityCriteria.where(criteriaBuilder.equal(entityRoot.get("indexed"),false));
            webPage = entityManager.createQuery(entityCriteria).setMaxResults(1).getResultList().get(0);
            transaction.commit();
        } catch (RollbackException ex){
            if(transaction!=null) transaction.rollback();
            ex.printStackTrace();
            throw ex;
        }finally {
            entityManager.close();
            session.close();
        }
        return webPage;
    }

    public boolean exists(WebPage webPage) {
        Session session = getSession();
        EntityManager entityManager = session.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = null;
        boolean exists = false;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Long> entityCriteria =  criteriaBuilder.createQuery(Long.class);
            Root<WebPage> entityRoot = entityCriteria.from(WebPage.class);
            entityCriteria.select(criteriaBuilder.count(entityRoot));
            entityCriteria.where(criteriaBuilder.equal(entityRoot.get("url"),webPage.getUrl()));
            exists = entityManager.createQuery(entityCriteria).getSingleResult()>0;
            transaction.commit();
        } catch (RollbackException ex){
            if(transaction!=null) transaction.rollback();
            ex.printStackTrace();
            throw ex;
        }finally {
            entityManager.close();
            session.close();
        }
        return exists;
    }

    public void update(WebPage webPage) {
        Session session = getSession();
        EntityManager entityManager = session.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaUpdate<WebPage> criteriaUpdate =  criteriaBuilder.createCriteriaUpdate(WebPage.class);
            Root<WebPage> entityRoot = criteriaUpdate.from(WebPage.class);
            criteriaUpdate.set(entityRoot.get("text"),webPage.getText())
            .set(entityRoot.get("title"),webPage.getTitle())
            .set(entityRoot.get("crc"),webPage.getCrc())
            .set(entityRoot.get("indexed"),webPage.getIndexed())
            .set(entityRoot.get("indexDate"),webPage.getIndexDate())
            .where(criteriaBuilder.equal(entityRoot.get("url"),webPage.getUrl()));
            entityManager.createQuery(criteriaUpdate).executeUpdate();
            transaction.commit();
        } catch (RollbackException ex){
            if(transaction!=null) transaction.rollback();
            ex.printStackTrace();
            throw ex;
        }finally {
            entityManager.close();
            session.close();
        }
    }


    public WebPage getByUrl(String url){
        Session session = getSession();
        EntityManager entityManager = session.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = null;
        WebPage pageToCrawl;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<WebPage> entityCriteria =  criteriaBuilder.createQuery(WebPage.class);
            Root<WebPage> entityRoot = entityCriteria.from(WebPage.class);
            entityCriteria.select(entityRoot);
            entityCriteria.where(criteriaBuilder.equal(entityRoot.get("url"),url));
            pageToCrawl = entityManager.createQuery(entityCriteria).getSingleResult();
            transaction.commit();
        } catch (RollbackException ex){
            if(transaction!=null) transaction.rollback();
            ex.printStackTrace();
            throw ex;
        }finally {
            entityManager.close();
            session.close();
        }
        return pageToCrawl;
    }

    public List<WebPage> getNotIndexed(int numOfWebPages){
        Session session = getSession();
        EntityManager entityManager = session.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = null;
        List<WebPage> pageToCrawl;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<WebPage> entityCriteria =  criteriaBuilder.createQuery(WebPage.class);
            Root<WebPage> entityRoot = entityCriteria.from(WebPage.class);
            entityCriteria.select(entityRoot);
            entityCriteria.where(criteriaBuilder.equal(entityRoot.get("indexed"),false));
            pageToCrawl = entityManager.createQuery(entityCriteria).setMaxResults(numOfWebPages).getResultList();
            transaction.commit();
        } catch (RollbackException ex){
            if(transaction!=null) transaction.rollback();
            ex.printStackTrace();
            throw ex;
        }finally {
            entityManager.close();
            session.close();
        }
        return pageToCrawl;

    }
}
