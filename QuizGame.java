import java.io.*;
import java.util.*;

class QuizGame {
    private static final String QUESTIONS_FILE = "C:\\Users\\prade\\IdeaProjects\\college_project\\src\\questions.txt";
    private static final Scanner scanner = new Scanner(System.in);
    private static Map<String, Integer> playerScores = new HashMap<>();

    public static void main(String[] args) {
        System.out.println("Welcome to the Quiz Game!");
        System.out.println("1. Register and Play");
        System.out.println("2. View Leaderboard");
        System.out.println("3. Exit");

        while (true) {
            System.out.print("\nEnter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    playQuiz();
                    break;
                case 2:
                    displayLeaderboard();
                    break;
                case 3:
                    System.out.println("Thank you for playing!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void playQuiz() {
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        List<Question> questions = loadQuestions();
        if (questions.isEmpty()) {
            System.out.println("No questions available. Please check the question file.");
            return;
        }

        int score = 0;
        for (Question question : questions) {
            System.out.println("\n" + question.getQuestion());
            List<String> options = question.getOptions();
            for (int i = 0; i < options.size(); i++) {
                System.out.println((i + 1) + ". " + options.get(i));
            }

            System.out.print("Your answer (1-" + options.size() + "): ");
            int answer = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (answer == question.getCorrectOption()) {
                System.out.println("Correct!");
                score++;
            } else {
                System.out.println("Wrong! The correct answer was: " + options.get(question.getCorrectOption() - 1));
            }
        }

        System.out.println("\nQuiz Over! Your Score: " + score);
        playerScores.put(name, score);
    }

    private static List<Question> loadQuestions() {
        List<Question> questions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(QUESTIONS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length < 6) continue; // Ensure valid question format
                String questionText = parts[0];
                List<String> options = Arrays.asList(parts[1], parts[2], parts[3], parts[4]);
                int correctOption = Integer.parseInt(parts[5]);
                questions.add(new Question(questionText, options, correctOption));
            }
        } catch (IOException e) {
            System.out.println("Error loading questions: " + e.getMessage());
        }
        Collections.shuffle(questions); // Randomize question order
        return questions;
    }

    private static void displayLeaderboard() {
        System.out.println("\nLeaderboard:");
        playerScores.entrySet()
                .stream()
                .sorted((a, b) -> b.getValue() - a.getValue())
                .forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue() + " points"));
    }
}

class Question {
    private String question;
    private List<String> options;
    private int correctOption;

    public Question(String question, List<String> options, int correctOption) {
        this.question = question;
        this.options = options;
        this.correctOption = correctOption;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getOptions() {
        return options;
    }

    public int getCorrectOption() {
        return correctOption;
    }
}
