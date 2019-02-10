package databaseComunication.metamodel;

import databaseComunication.entity.WebPage;
import databaseComunication.entity.Word;
import databaseComunication.entity.WordPosition;
import databaseComunication.entity.WordWeight;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * Created by Pavel on 10.02.2019.
 */
@StaticMetamodel(WordWeight.class)
public abstract class WordWeight_ {
    public static volatile SingularAttribute<WordWeight,Long> id;
    public static volatile SingularAttribute<WordWeight, Word> word;
    public static volatile SingularAttribute<WordWeight, WebPage> webPage;
    public static volatile SingularAttribute<WordWeight,Double> tf;
    public static volatile SingularAttribute<WordWeight,Double> tfIdf;
    public static volatile SetAttribute<WordWeight, WordPosition> wordPositionSet;
}
