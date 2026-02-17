package ella;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    private static final String APP_TITLE = "Ella";
    private static final String WELCOME_MESSAGE = "Hello! I'm Ella\nWhat can I do for you?";

    private Ella ella;

    @Override
    public void start(Stage stage) {
        // Initialize app core (loads tasks from storage)
        ella = new Ella("data/duke.txt");

        VBox dialogContainer = new VBox(10);
        dialogContainer.setPadding(new Insets(10));
        dialogContainer.setFillWidth(true);

        ScrollPane scrollPane = new ScrollPane(dialogContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        // Auto-scroll to bottom when new messages arrive
        dialogContainer.heightProperty().addListener((obs, oldVal, newVal) -> scrollPane.setVvalue(1.0));

        TextField userInput = new TextField();
        userInput.setPromptText("Type a command...");
        Button sendButton = new Button("Send");
        sendButton.setDefaultButton(true);

        HBox inputBox = new HBox(10, userInput, sendButton);
        inputBox.setPadding(new Insets(10, 0, 0, 0));
        HBox.setHgrow(userInput, Priority.ALWAYS);

        VBox root = new VBox(10, scrollPane, inputBox);
        root.setPadding(new Insets(10));

        addDialog(dialogContainer, "Ella", WELCOME_MESSAGE, false);

        Runnable send = () -> {
            String input = userInput.getText();
            if (input == null) {
                return;
            }
            input = input.trim();
            if (input.isEmpty()) {
                return;
            }

            addDialog(dialogContainer, "You", input, true);

            try {
                String response = Parser.handle(input, ella.getTasks(), ella.getStorage());
                if (response != null && !response.isEmpty()) {
                    addDialog(dialogContainer, "Ella", response, false);
                }

                if (input.equalsIgnoreCase("bye")) {
                    Platform.exit();
                }
            } catch (EllaException e) {
                addDialog(dialogContainer, "Ella", "Oops! " + e.getMessage(), false);
            }

            userInput.clear();
        };

        sendButton.setOnAction(e -> send.run());
        userInput.setOnAction(e -> send.run());

        stage.setTitle(APP_TITLE);
        stage.setScene(new Scene(root, 520, 640));
        stage.show();
    }

    /**
     * Adds a single message row to the dialog container.
     * - User messages appear on the right.
     * - Ella messages appear on the left.
     * Uses a simple "bubble" style for readability.
     */
    private void addDialog(VBox container, String speaker, String text, boolean isUser) {
        Label bubble = new Label(text);
        bubble.setWrapText(true);
        bubble.setPadding(new Insets(10));
        bubble.setMaxWidth(360);

        // Simple bubble styling (no external CSS needed)
        if (isUser) {
            bubble.setStyle(
                    "-fx-background-color: #dbeafe;"
                            + "-fx-background-radius: 12;"
                            + "-fx-border-radius: 12;"
                            + "-fx-border-color: #93c5fd;"
                            + "-fx-font-size: 13px;"
            );
        } else {
            bubble.setStyle(
                    "-fx-background-color: #f3f4f6;"
                            + "-fx-background-radius: 12;"
                            + "-fx-border-radius: 12;"
                            + "-fx-border-color: #d1d5db;"
                            + "-fx-font-size: 13px;"
            );
        }

        Label nameTag = new Label(speaker);
        nameTag.setStyle("-fx-font-size: 10px; -fx-text-fill: #6b7280;");
        VBox messageBlock = new VBox(2, nameTag, bubble);

        HBox row = new HBox(10);
        row.setPadding(new Insets(2, 0, 2, 0));
        row.setAlignment(isUser ? Pos.TOP_RIGHT : Pos.TOP_LEFT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        if (isUser) {
            row.getChildren().addAll(spacer, messageBlock);
        } else {
            row.getChildren().addAll(messageBlock, spacer);
        }

        container.getChildren().add(row);
    }
}
