package databaseComunication.metamodel;

import databaseComunication.entity.WordPosition;
import databaseComunication.entity.WordWeight;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * Created by Pavel on 10.02.2019.
 */
@StaticMetamodel(WordPosition.class)
public abstract class WordPosition_ {
    public static volatile SingularAttribute<WordPosition,Long> id;
    public static volatile SingularAttribute<WordPosition,Integer> position;
    public static volatile SingularAttribute<WordPosition, WordWeight> wordWeight;
}
