package square.objects;

import java.io.Serializable;

public class Score implements Comparable<Score>, Serializable {

    private int score;
    private String playerName;

    public Score(int score, String playerName) {
        this.score = score;
        this.playerName = playerName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Score score1 = (Score) o;

        if (score != score1.score) return false;
        return playerName.equals(score1.playerName);
    }

    @Override
    public int hashCode() {
        int result = score;
        result = 31 * result + playerName.hashCode();
        return result;
    }

    public int getScore() {
        return score;
    }

    @Override
    public int compareTo(Score o) {
        return Integer.compare(o.score, score);
    }

    @Override
    public String toString() {
        return playerName + " : " + score;
    }
}
