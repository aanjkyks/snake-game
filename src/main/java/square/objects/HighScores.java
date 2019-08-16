package square.objects;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static square.Config.HIGH_SCORE_ENTRY_LIMIT;
import static square.Config.HIGH_SCORE_FILE;

public class HighScores {
    private List<Score> scores = new ArrayList<>();

    public HighScores() {
        loadScoreFile();
    }

    public List<Score> getScores() {
        return scores;
    }

    public void addScore(Score score) {
        if (scores.contains(score))
            return;
        if (scores.size() < HIGH_SCORE_ENTRY_LIMIT) {
            scores.add(score);
        } else
            for (int i = scores.size() - 1; i >= 0; i--) {
                Score score1 = scores.get(i);
                if (score.getScore() > score1.getScore()) {
                    scores.remove(score1);
                    scores.add(score);
                    break;
                }
            }
        Collections.sort(scores);
        updateScoreFile();
    }

    @SuppressWarnings("unchecked")
    private void loadScoreFile() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(HIGH_SCORE_FILE))) {
            scores = (ArrayList<Score>) inputStream.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("[Load] FNF Error: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("[Load] IO Error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("[Load] CNF Error: " + e.getMessage());
        }
        if (scores.isEmpty())
            loadDefaultScores();
    }

    private void loadDefaultScores() {
        addScore(new Score(1000, "JAKE"));
        addScore(new Score(500, "REDSQUARE"));
        addScore(new Score(0, "HACKERS UNWELCOME"));
        addScore(new Score(100, "BIG"));
    }

    private void updateScoreFile() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(HIGH_SCORE_FILE))) {
            outputStream.writeObject(scores);
        } catch (FileNotFoundException e) {
            System.out.println("[Update] FNF Error: " + e.getMessage() + ", the program will try and make a new file");
        } catch (IOException e) {
            System.out.println("[Update] IO Error: " + e.getMessage());
        }
    }

    public boolean newHiScore(int score) {
        if (scores.size() < HIGH_SCORE_ENTRY_LIMIT)
            return true;
        for (Score score1 : scores) {
            if (score > score1.getScore())
                return true;
        }
        return false;
    }
}
