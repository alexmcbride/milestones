package wpd2.coursework1.servlet;

import wpd2.coursework1.model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
 * Servlet for supplying autocomplete JSON.
 */
public class ApiUserAutocompleteServlet extends JsonServlet {
    protected void doGet() throws IOException {
        List<AutocompleteItem> items = new ArrayList<>();

        String term = request.getParameter("term");
        if (term != null && term.trim().length() > 0) {
            List<User> users = User.search(term);
            for (User user : users) {
                items.add(new AutocompleteItem(user.getId(), user.getUsername()));
            }
        }

        view(items);
    }

    // Format used by jQuery autocomplete plugin
    private class AutocompleteItem {
        private int id;
        private String value;

        AutocompleteItem(int id, String value) {
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
