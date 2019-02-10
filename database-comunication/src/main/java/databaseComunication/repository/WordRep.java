package databaseComunication.repository;

import databaseComunication.entity.Word;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.RollbackException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Created by Pavel on 02.02.2019.
 */
public class WordRep extends GenericRepository<Word> {
    public WordRep(SessionFactory sessionFactory) {
        super(sessionFactory, Word.class);
    }

    public Word getByStrWord(String word){
        Session session = getSession();
        EntityManager entityManager = session.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = null;
        Word pageToCrawl;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Word> entityCriteria =  criteriaBuilder.createQuery(Word.class);
            Root<Word> entityRoot = entityCriteria.from(Word.class);
            entityCriteria.select(entityRoot);
            entityCriteria.where(criteriaBuilder.equal(entityRoot.get("word"),word));
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



    public boolean exists(String strWord){
        Session session = getSession();
        EntityManager entityManager = session.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = null;
        Boolean exists;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Word> entityCriteria =  criteriaBuilder.createQuery(Word.class);
            Root<Word> entityRoot = entityCriteria.from(Word.class);
            entityCriteria.select(entityRoot);
            entityCriteria.where(criteriaBuilder.equal(entityRoot.get("word"),strWord));
            exists = !entityManager.createQuery(entityCriteria).setMaxResults(1).getResultList().isEmpty();
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

    public void addIfNotExists(Word word){
        Session session = getSession();
        EntityManager entityManager = session.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Word> entityCriteria =  criteriaBuilder.createQuery(Word.class);
            Root<Word> entityRoot = entityCriteria.from(Word.class);
            entityCriteria.select(entityRoot);
            entityCriteria.where(criteriaBuilder.equal(entityRoot.get("word"),word.getWord()));
            Boolean exists = !entityManager.createQuery(entityCriteria).setMaxResults(1).getResultList().isEmpty();
            if(!exists) {
                entityManager.persist(word);
                transaction.commit();
            } else{
                transaction.rollback();
            }
        } catch (PersistenceException ex){
            if(transaction!=null) transaction.rollback();
            ex.printStackTrace();
            throw ex;
        }finally {
            entityManager.close();
            session.close();
        }

    }
}
