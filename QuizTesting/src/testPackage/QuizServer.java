package testPackage;

import testPackage.storage.QuizIOManager;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Squall on 27/01/2015.
 */
public class QuizServer extends UnicastRemoteObject implements QuizService, PlayerService {
    //NEWEST VERSION MUST COMMIT!
    private List<QuizObject> quizList;
    private List<Player> players;
    private Score highScore;

    private QuizIOManager ioManager;

    public QuizServer() throws RemoteException
    {
        quizList = new ArrayList<QuizObject>();
        players = new ArrayList<Player>();
        this.highScore = null;
        ioManager = new QuizIOManager();
        System.out.println("Quiz Server Running");
        setupLists();
        System.out.println("Loaded " + quizList.size() + " Quizzes " + players.size() + " Players");
        Runtime.getRuntime().addShutdownHook(flushHook());
    }

    public QuizObject echoQuiz(QuizObject qo) {
        quizList.add(qo);
        quizInfo();
        return qo;
    }

    private Thread flushHook()
    {
        Runnable r1 = () -> {
            ioManager.writeQuiz(quizList);
            ioManager.writePlayers(players);
        };
        return new Thread(r1);
    }

    public String[] showQuizChoices() throws RemoteException
    {
        System.out.println("Sent quiz names to Player: ");
        String[] quizNames = new String[quizList.size()];
        for (int i = 0; i < quizList.size(); i++)
        {
            quizNames[i] = quizList.get(i).getName();
        }
        return quizNames;
    }

    public QuizObject getRemoteQuiz(String name)
    {
        for (int i = 0; i < quizList.size(); i++)
        {
            if (quizList.get(i).getName().equals(name))
            {
                return quizList.get(i);
            }
            else
            {
                return null;
            }
        }
        return null;
    }

    public boolean isExistingId(String userName)
    {
        boolean exists = false;
        for (int i = 0; i < players.size(); i++)
        {
            if (players.get(i).getUserName().equals(userName))
            {
                exists = true;
            }
        }
        return exists;
    }

    public String testPlayerEntry()
    {
        return "Name: " + players.get(players.size()-1).getName() + "\nUN: " + players.get(players.size()-1).getUserName();
    }

    private void setupLists()
    {
        if (ioManager.fileExists("QuizList.txt"))
        {
            quizList = ioManager.readQuiz();
        }
        else
        {
            System.out.println("No Quizzes Loaded");
        }
        if (ioManager.fileExists("Players.txt"))
        {
            players = ioManager.readPlayers();
        }
        else
        {
            System.out.println("No Players Loaded");
        }
        setHighScore();
    }

    private void setHighScore()
    {
        if (!(players.isEmpty()))
        {
            int highscore = 0;
            for (int i = 0; i < players.size(); i++)
            {
                if (players.get(i).getHighestScore() != null)
                {
                    if (players.get(i).getHighestScore().getScore() > highscore)
                    {
                        highscore = players.get(i).getHighestScore().getScore();
                    }
                }
            }
            this.highScore = new Score(highscore);
        }
    }

    private void quizInfo()
    {
        System.out.println("Added quiz titled \"" + quizList.get(quizList.size()-1).getName() + "\" to server");
        System.out.println("The server now has " + quizList.size() + " quizzes stored");
    }

    public void fetchPlayer(Player aPlayer)
    {
        players.add(aPlayer);
    }
}
