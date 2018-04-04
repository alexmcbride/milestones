package wpd2.coursework1.servlet;

import wpd2.coursework1.model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UsersAutocompleteServlet extends BaseServlet {
    protected void doGet() throws IOException {
        if (!jsonAuthorize()) return;

        List<AutocompleteItem> items = new ArrayList<>();

        String term = request.getParameter("term");
        if (term != null && term.trim().length() > 0) {
            List<User> users = User.search(term);
            for (User user : users) {
                items.add(new AutocompleteItem(user.getId(), user.getUsername()));
            }
        }

        json(items);
    }

    // Format used by jQuery autocomplete plugin
    private class AutocompleteItem {
        private int id;
        private String value;

        public AutocompleteItem(int id, String value) {
            this.id = id;
            this.value = value;
        }

        public int getId() {
            return id;
        }

        public String getValue() {
            return value;
        }
    }
}
