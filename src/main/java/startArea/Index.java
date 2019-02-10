package startArea;

import databaseComunication.DatabaseConnect;
import databaseComunication.DatabaseIndexerCom;
import indexer.IndexStarter;

import java.util.Scanner;

/**
 * Created by Pavel on 03.02.2019.
 */
public class Index {
    public static void main(String[] args) {
        try {
            DatabaseConnect databaseConnect = new DatabaseConnect();
            DatabaseIndexerCom databaseIndexerCom = new DatabaseIndexerCom(databaseConnect);
            IndexStarter indexer = new IndexStarter(databaseIndexerCom,Runtime.getRuntime().availableProcessors());
            Thread thread = new Thread(indexer);
            thread.start();
            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();
            while(thread.isAlive()) {
                thread.interrupt();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
