package databaseComunication.repository;

import databaseComunication.entity.WordPosition;
import org.hibernate.SessionFactory;

/**
 * Created by Pavel on 08.02.2019.
 */
public class WordPositionRep extends GenericRepository<WordPosition> {
    public WordPositionRep(SessionFactory sessionFactory) {
        super(sessionFactory, WordPosition.class);
    }
}
