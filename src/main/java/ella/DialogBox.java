package ella;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.InputStream;

/**
 * A dialog bubble with an avatar and a name label.
 * User messages appear on the right, Ella messages on the left.
 */
public class DialogBox extends HBox {
    private static final double MAX_BUBBLE_WIDTH = 360;
    private static final double AVATAR_SIZE = 44;

    // Same bubble color for both User and Ella
    private static final Color BUBBLE_COLOR = Color.web("#CFE8FF");

    private DialogBox(String senderName, String message, boolean isUser, Image avatar) {
        super(8);

        Label nameLabel = new Label(senderName);
        nameLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #666666; -fx-font-weight: bold;");

        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(MAX_BUBBLE_WIDTH);
        messageLabel.setPadding(new Insets(10, 12, 10, 12));

        messageLabel.setBackground(new Background(new BackgroundFill(
                BUBBLE_COLOR,
                new CornerRadii(14),
                Insets.EMPTY
        )));

        VBox textBlock = new VBox(4, nameLabel, messageLabel);
        textBlock.setMaxWidth(MAX_BUBBLE_WIDTH + 50);

        ImageView avatarView = new ImageView(avatar);
        avatarView.setFitWidth(AVATAR_SIZE);
        avatarView.setFitHeight(AVATAR_SIZE);
        avatarView.setPreserveRatio(true);

        StackPane avatarHolder = new StackPane(avatarView);
        avatarHolder.setMinSize(AVATAR_SIZE, AVATAR_SIZE);
        avatarHolder.setMaxSize(AVATAR_SIZE, AVATAR_SIZE);
        avatarHolder.setBackground(new Background(new BackgroundFill(
                Color.WHITE, new CornerRadii(AVATAR_SIZE), Insets.EMPTY
        )));
        avatarHolder.setPadding(new Insets(2));
        avatarHolder.setStyle("-fx-border-color: #DDDDDD; -fx-border-radius: 999; -fx-background-radius: 999;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        setPadding(new Insets(2, 2, 2, 2));

        if (isUser) {
            setAlignment(Pos.TOP_RIGHT);
            textBlock.setAlignment(Pos.TOP_RIGHT);
            getChildren().addAll(spacer, textBlock, avatarHolder);
        } else {
            setAlignment(Pos.TOP_LEFT);
            textBlock.setAlignment(Pos.TOP_LEFT);
            getChildren().addAll(avatarHolder, textBlock, spacer);
        }
    }

    public static DialogBox user(String message) {
        return new DialogBox("User", message, true, loadAvatarOrFallback("/images/user.png"));
    }

    public static DialogBox ella(String message) {
        return new DialogBox("Ella", message, false, loadAvatarOrFallback("/images/ella.png"));
    }

    private static Image loadAvatarOrFallback(String resourcePath) {
        try (InputStream is = DialogBox.class.getResourceAsStream(resourcePath)) {
            if (is != null) {
                return new Image(is);
            }
        } catch (Exception ignored) {
            // fall through
        }
        return new Image("data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAA"
                + "AAC0lEQVR42mP8/x8AAwMCAO7WmS0AAAAASUVORK5CYII=");
    }
}
