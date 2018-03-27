package wpd2.coursework1.util;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

public class FlashHelper {
    public static final String SUCCESS = "success";
    public static final String DANGER = "danger";
    public static final String INFO = "info";
    public static final String WARNING = "warning";
    private static final String KEY_FLASH_MESSAGES = "flash-messages";

    private final HttpSession session;

    public FlashHelper(HttpSession session) {
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

    public class FlashMessage {
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
