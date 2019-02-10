package databaseComunication.metamodel;

import databaseComunication.entity.Word;
import databaseComunication.entity.WordWeight;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * Created by Pavel on 10.02.2019.
 */
@StaticMetamodel(Word.class)
public abstract class Word_ {
    public static volatile SingularAttribute<Word, Long> id;
    public static volatile SingularAttribute<Word,String> word;
    public static volatile SetAttribute<Word, WordWeight> wordWeightSet;
}
