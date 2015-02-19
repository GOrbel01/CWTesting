package testPackage;

import java.io.Serializable;

/**
 * Created by Squall on 29/01/2015.
 */
public class Answer implements Serializable {
    private static final long serialVersionUID = 5713037108281620664L;
    private int id;
    private String answer;

    public Answer(int id, String ans)
    {
        this.id = id;
        answer = ans;
    }

    public String getAnswer()
    {
        return answer;
    }

    public Integer getId()
    {
        return id;
    }

    @Override
    public boolean equals(Object ans)
    {
        return (this.answer.equals(ans));
    }

    public String toString()
    {
        return id + ". Answer: " + answer;
    }
}
