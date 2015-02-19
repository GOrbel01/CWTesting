package testPackage;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by Squall on 30/01/2015.
 */
public class PlayerClient {

    Remote service;
    PlayerService plService;

    public PlayerClient()
    {
        if (System.getSecurityManager() == null)
        {
            System.setProperty("java.security.policy", "res/player.policy");
            System.setSecurityManager(new SecurityManager());
        }
        try {
            service = Naming.lookup("//127.0.0.1:1099/echo");
            plService = (PlayerService) service;
        }
        catch (MalformedURLException | NotBoundException | RemoteException ex )
        {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        PlayerClient pcl = new PlayerClient();
        try {
            pcl.setupPlayer();
            pcl.setupPlayerQuiz();
        }
        catch (RemoteException ex)
        {
            ex.printStackTrace();
        }
    }

    public void setupPlayer() throws RemoteException
    {
        boolean done = false;
        Scanner sc = new Scanner(System.in);
        String userName = "";
        System.out.println("Enter a Unique UserName as a String Containing a-z and/or 0-9");
        while (!done)
        {
            userName = sc.nextLine();
            if (!(InputValidator.validUserName(userName)))
            {
                System.out.println("Invalid Input Entered, You must Enter a Valid Username With a-z or 0-9.");
            }
            else
            {

                if (InputValidator.validUserName(userName))
                {
                    if (!(plService.isExistingId((userName))))
                    {
                        done = true;
                    }
                    else
                    {
                        System.out.println("You must Enter a Unique UserName!");
                    }
                }
            }
        }
        done = false;
        String playerName = "";
        System.out.println("Enter your name");
        while (!done)
        {
            playerName = sc.nextLine();
            if (InputValidator.validPlayerName(playerName))
            {
                done = true;
            }
            else
            {
                System.out.println("Invalid name entered, enter a valid string of a-z characters");
            }
        }
        System.out.println("Playing Quiz As");
        plService.fetchPlayer(new Player(userName, playerName));
        System.out.println(plService.testPlayerEntry());
    }

    public void setupPlayerQuiz() throws RemoteException
    {
        System.out.println("Quizzes Available:\n");
        Arrays.stream(plService.showQuizChoices()).forEach((name) -> System.out.println(name));
    }

    private QuizObject getQuizSelection() throws RemoteException
    {
        Scanner sc = new Scanner(System.in);
        String quizName = "";
        System.out.println("Enter the name of the quiz you would like to play");
        quizName = sc.nextLine();
        QuizObject qo = null;
        while (qo == null)
        {
            qo = plService.getRemoteQuiz(quizName);
            String conf = "";
            System.out.println("You have chosen to play: " + qo.getName());
            while (!(conf.equalsIgnoreCase("Yes") || conf.equalsIgnoreCase("No")))
            {
                System.out.println("Is this correct? Type Yes or No");
                conf = sc.nextLine();
                if (conf.equalsIgnoreCase("No"))
                {
                    qo = null;
                    System.out.println("Please re-enter your selections");
                }
                else if (conf.equalsIgnoreCase("Yes"))
                {
                    System.out.println("Setting Up Quiz...");
                }
                else
                {
                    System.out.println("You must Enter Yes or No");
                }
            }
        }
        return qo;
    }

    private void playQuiz(QuizObject qo)
    {
        System.out.println("The quiz has now started...");
        for (int i = 0; i < qo.getQuestions().size(); i++)
        {
            System.out.println(qo.getQuestions().get(i).getQuestion().getQuestionText());
        }
    }
}
