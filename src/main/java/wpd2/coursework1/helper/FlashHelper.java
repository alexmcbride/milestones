package wpd2.coursework1.helper;

import wpd2.coursework1.util.SessionWrapper;

import java.util.ArrayList;
import java.util.List;

/*
 * Helper for making 'flash' drop down messages appear.
 */
public class FlashHelper {
    public static final String SUCCESS = "success";
    public static final String DANGER = "danger";
    public static final String INFO = "info";
    public static final String WARNING = "warning";
    public static final String KEY_FLASH_MESSAGES = "flash-messages";

    private final SessionWrapper session;

    /**
     * Creates a new FlashHelper
     *
     * @param session the user's current session
     */
    public FlashHelper(SessionWrapper session) {
        this.session = session;
    }

    /**
     * Gets the current flash messages from the session.
     *
     * @return a list of message objects.
     */
    public List<FlashMessage> getMessages() {
        List<FlashMessage> messages = (List<FlashMessage>)session.getAttribute(KEY_FLASH_MESSAGES);
        if (messages == null) {
            return new ArrayList<>();
        }
        return messages;
    }

    /**
     * Returns a boolean indicating if there are currently flash messages for this user.
     *
     * @return true if there are messages
     */
    public boolean hasMessages() {
        return session.getAttribute(KEY_FLASH_MESSAGES) != null;
    }

    /*
     * Clears any flash messages in the user's session.
     */
    public void clearMessages() {
        session.removeAttribute(KEY_FLASH_MESSAGES);
    }

    /**
     * Stores the list of flash messages in the session.
     *
     * @param messages the messages to store
     */
    private void setMessages(List<FlashMessage> messages) {
        session.setAttribute(KEY_FLASH_MESSAGES, messages);
    }

    /**
     * Adds a flash message to the session.
     *
     * @param content the message to show.
     */
    public void message(String content) {
        message(content, SUCCESS);
    }

    /**
     * Adds a flash message to the session with the specified type.
     *
     * @param content the message to show.
     * @param type the type of flash message to display.
     */
    public void message(String content, String type) {
        List<FlashMessage> messages = getMessages();
        messages.add(new FlashMessage(content, type));
        setMessages(messages);
    }

    /*
     * Internal class to represent a flash message.
     */
    public static class FlashMessage {
        private String content;
        private String type;

        public FlashMessage(String content, String type) {
            this.content = content;
            this.type = type;
        }

        public String getContent() {
            return content;
        }

        public String getType() {
            return type;
        }
    }
}
