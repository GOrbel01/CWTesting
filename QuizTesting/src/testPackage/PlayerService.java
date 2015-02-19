package testPackage;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Squall on 30/01/2015.
 */
public interface PlayerService extends Remote {
    String[] showQuizChoices() throws RemoteException;
    QuizObject getRemoteQuiz(String name) throws RemoteException;
    void fetchPlayer(Player player) throws RemoteException;
    boolean isExistingId(String userName) throws RemoteException;
    String testPlayerEntry() throws RemoteException;
}
