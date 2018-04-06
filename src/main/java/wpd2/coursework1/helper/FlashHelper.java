package wpd2.coursework1.helper;

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

    public FlashHelper(SessionWrapper session) {
        this.session = session;
    }

    public List<FlashMessage> getMessages() {
        List<FlashMessage> messages = (List<FlashMessage>)session.getAttribute(KEY_FLASH_MESSAGES);
        if (messages == null) {
            return new ArrayList<>();
        }
        return messages;
    }

    public boolean hasMessages() {
        return session.getAttribute(KEY_FLASH_MESSAGES) != null;
    }

    public void clearMessages() {
        session.removeAttribute(KEY_FLASH_MESSAGES);
    }

    private void setMessages(List<FlashMessage> messages) {
        session.setAttribute(KEY_FLASH_MESSAGES, messages);
    }

    public void message(String content) {
        message(content, SUCCESS);
    }

    public void message(String content, String type) {
        List<FlashMessage> messages = getMessages();
        messages.add(new FlashMessage(content, type));
        setMessages(messages);
    }

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
