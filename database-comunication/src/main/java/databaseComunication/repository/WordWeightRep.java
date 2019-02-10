package databaseComunication.repository;

import databaseComunication.entity.WebPage;
import databaseComunication.entity.Word;
import databaseComunication.entity.WordWeight;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.util.List;

/**
 * Created by Pavel on 05.02.2019.
 */
public class WordWeightRep extends GenericRepository<WordWeight> {
    public WordWeightRep(SessionFactory sessionFactory) {
        super(sessionFactory, WordWeight.class);
    }

    public void addList(List<WordWeight> wordWeightList){
        Session session = getSession();
        EntityManager entityManager = session.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            for(WordWeight wordWeight : wordWeightList){
                entityManager.merge(wordWeight);
            }
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

    public List<WordWeight> getAllByWord(Word word){
        Session session = getSession();
        EntityManager entityManager = session.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = null;
        List<WordWeight> pageToCrawl;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<WordWeight> entityCriteria =  criteriaBuilder.createQuery(WordWeight.class);
            Root<WordWeight> entityRoot = entityCriteria.from(WordWeight.class);
            entityCriteria.select(entityRoot);
            entityCriteria.where(criteriaBuilder.equal(entityRoot.get("word"),word));
            pageToCrawl = entityManager.createQuery(entityCriteria).getResultList();
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

    public List<WordWeight> getAllContainingWords(List<Word> words){
        Session session = getSession();
        EntityManager entityManager = session.getEntityManagerFactory().createEntityManager();
        EntityGraph<WordWeight> fetchPos = entityManager.createEntityGraph(WordWeight.class);
        fetchPos.addSubgraph("wordPositionSet");
        fetchPos.addSubgraph("word");
        fetchPos.addSubgraph("webPage");
        EntityTransaction transaction = null;
        List<WordWeight> pageToCrawl;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<WordWeight> entityCriteria =  criteriaBuilder.createQuery(WordWeight.class);
            Root<WordWeight> entityRoot = entityCriteria.from(WordWeight.class);
            entityCriteria.select(entityRoot);
            Predicate[] predicates = new Predicate[words.size()];
            for(int i = 0;i<words.size();i++){
                Subquery<WebPage> sub = entityCriteria.subquery(WebPage.class);
                Root<WordWeight> subRoot = sub.from(WordWeight.class);
                sub.select(subRoot.get("webPage")).where(criteriaBuilder.equal(subRoot.get("word"),words.get(i)));
                predicates[i] = criteriaBuilder.in(entityRoot.get("webPage")).value(sub);
            }
            Predicate[] predicates1 = new Predicate[words.size()];
            for(int i = 0;i<words.size();i++){
                predicates1[i] = criteriaBuilder.equal(entityRoot.get("word"),words.get(i));
            }

            entityCriteria.where(criteriaBuilder.and(criteriaBuilder.or(predicates1),criteriaBuilder.and(predicates)));
            pageToCrawl = entityManager.createQuery(entityCriteria).setHint("javax.persistence.fetchgraph",fetchPos).getResultList();
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

    public void updateTfIdf(WordWeight wwp) {
        Session session = getSession();
        EntityManager entityManager = session.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaUpdate<WordWeight> criteriaUpdate =  criteriaBuilder.createCriteriaUpdate(WordWeight.class);
            Root<WordWeight> entityRoot = criteriaUpdate.from(WordWeight.class);
            criteriaUpdate.set(entityRoot.get("tfIdf"),wwp.getTfIdf())
                    .where(criteriaBuilder.equal(entityRoot.get("id"),wwp.getId()));
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

    public Long getNumOfPagesWithWord(Word word){
        Session session = getSession();
        EntityManager entityManager = session.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = null;
        Long  webPageCount = -1L;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Long> entityCriteria =  criteriaBuilder.createQuery(Long.class);
            Root<WordWeight> entityRoot = entityCriteria.from(WordWeight.class);
            entityCriteria.select(criteriaBuilder.count(entityRoot));
            entityCriteria.where(criteriaBuilder.equal(entityRoot.get("word"),word));
            webPageCount = entityManager.createQuery(entityCriteria).getSingleResult();
            transaction.commit();
        } catch (RollbackException ex){
            if(transaction!=null) transaction.rollback();
            ex.printStackTrace();
            throw ex;
        }finally {
            entityManager.close();
            session.close();
        }
        return webPageCount;
    }

    public void addIfNotExist(WordWeight wordWeight) {
        Session session = getSession();
        EntityManager entityManager = session.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<WordWeight> entityCriteria =  criteriaBuilder.createQuery(WordWeight.class);
            Root<WordWeight> entityRoot = entityCriteria.from(WordWeight.class);
            entityCriteria.select(entityRoot);

            entityCriteria.where(criteriaBuilder.and(criteriaBuilder.equal(entityRoot.get("word").get("id"),wordWeight.getWord().getId()),
                    criteriaBuilder.equal(entityRoot.get("webPage").get("id"),wordWeight.getWebPage().getId())));

            Boolean exists = !entityManager.createQuery(entityCriteria).setMaxResults(1).getResultList().isEmpty();
            if(!exists) {
                entityManager.merge(wordWeight);
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

    public WordWeight getByPageWord(WebPage webPage, Word word) {
        Session session = getSession();
        EntityManager entityManager = session.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = null;
        WordWeight wordWeight = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<WordWeight> entityCriteria =  criteriaBuilder.createQuery(WordWeight.class);
            Root<WordWeight> entityRoot = entityCriteria.from(WordWeight.class);
            entityCriteria.select(entityRoot);

            entityCriteria.where(criteriaBuilder.and(criteriaBuilder.equal(entityRoot.get("word").get("id"),word.getId()),
                    criteriaBuilder.equal(entityRoot.get("webPage").get("id"),webPage.getId())));

            wordWeight = entityManager.createQuery(entityCriteria).getSingleResult();
                transaction.commit();
        } catch (PersistenceException ex){
            if(transaction!=null) transaction.rollback();
            ex.printStackTrace();
            throw ex;
        }finally {
            entityManager.close();
            session.close();
        }
        return wordWeight;
    }
}
