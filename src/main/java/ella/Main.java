package ella;

import java.io.InputStream;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * JavaFX GUI for Ella.
 * User messages appear on the right, Ella's messages appear on the left.
 */
public class Main extends Application {

    private static final String APP_TITLE = "Ella";
    private static final String SAVE_PATH = "data/ella.txt";

    private Ella ella;

    @Override
    public void start(Stage stage) {
        ella = new Ella(SAVE_PATH);

        VBox dialogContainer = new VBox(8);
        dialogContainer.setPadding(new Insets(10));

        ScrollPane scrollPane = new ScrollPane(dialogContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        // Auto-scroll to bottom when new messages appear
        dialogContainer.heightProperty().addListener((obs, oldVal, newVal) -> scrollPane.setVvalue(1.0));

        TextField userInput = new TextField();
        userInput.setPromptText("Type a command...");
        Button sendButton = new Button("Send");
        sendButton.setDefaultButton(true);

        HBox inputBox = new HBox(8, userInput, sendButton);
        HBox.setHgrow(userInput, Priority.ALWAYS);

        VBox root = new VBox(10, scrollPane, inputBox);
        root.setPadding(new Insets(10));

        // Startup warnings (e.g., storage issues)
        String warning = ella.getStartupWarning();
        if (warning != null && !warning.isBlank()) {
            dialogContainer.getChildren().add(DialogBox.ella("⚠️ " + warning));
        }

        // Initial welcome bubble
        dialogContainer.getChildren().add(DialogBox.ella("✨ Hey! I'm Ella.\nWhat can I do for you?"));

        Runnable send = () -> {
            String input = userInput.getText();
            if (input == null) {
                return;
            }

            input = input.trim();
            if (input.isEmpty()) {
                userInput.clear();
                return;
            }

            // User bubble
            dialogContainer.getChildren().add(DialogBox.user(input));

            String response;
            try {
                response = Parser.handle(input, ella.getTasks(), ella.getStorage());
            } catch (EllaException e) {
                response = "Oops! " + e.getMessage();
            } catch (Exception e) {
                // Safety net: prevents GUI from crashing on unexpected errors.
                response = "Something went wrong on my side. Please try again.";
            }

            if (response != null && !response.isEmpty()) {
                dialogContainer.getChildren().add(DialogBox.ella(response));
            }

            userInput.clear();

            if (input.equalsIgnoreCase("bye")) {
                // Disable further input, then close the app.
                userInput.setDisable(true);
                sendButton.setDisable(true);
                Platform.exit();
            }
        };

        sendButton.setOnAction(e -> send.run());
        userInput.setOnAction(e -> send.run());

        stage.setTitle(APP_TITLE);

        // Use ella.png as the app/window icon
        Image icon = loadIcon("/images/ella.png");
        if (icon != null) {
            stage.getIcons().add(icon);
        }

        stage.setScene(new Scene(root, 520, 640));
        stage.show();
    }

    private Image loadIcon(String resourcePath) {
        try (InputStream is = Main.class.getResourceAsStream(resourcePath)) {
            return (is == null) ? null : new Image(is);
        } catch (Exception e) {
            return null;
        }
    }
}
