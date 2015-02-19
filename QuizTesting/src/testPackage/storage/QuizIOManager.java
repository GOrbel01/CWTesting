package testPackage.storage;

import testPackage.Player;
import testPackage.QuizObject;

import java.io.*;
import java.util.List;

/**
 * Created by Squall on 01/02/2015.
 */
public class QuizIOManager {

    private File quizFile;
    private File playerFile;
    private File scoreFile;

    public QuizIOManager()
    {
        quizFile = new File("QuizList.txt");
        playerFile = new File("Players.txt");
    }

    public List<QuizObject> readQuiz()
    {
        return readFile(quizFile);
    }

    public void writeQuiz(List<QuizObject> list)
    {
        writeFile(list, quizFile);
    }

    public void writePlayers(List<Player> list)
    {
        writeFile(list, playerFile);
    }

    public List<Player> readPlayers()
    {
        return readFile(playerFile);
    }

    private <T> List<T> readFile(File file)
    {
        try
                (
                        FileInputStream fis = new FileInputStream(file);
                        ObjectInputStream ois = new ObjectInputStream(fis);
                )
        {
            return (List<T>) ois.readObject();
        }
        catch (ClassNotFoundException | IOException ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    private <T> void writeFile(List<T> list, File file)
    {
        try (
                FileOutputStream fos = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        )
        {
            oos.writeObject(list);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    public boolean fileExists(String text)
    {
        File file = new File(text);
        return file.exists();
    }
}
