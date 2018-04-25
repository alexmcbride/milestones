package wpd2.coursework1.servlet;

import wpd2.coursework1.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
 * Servlet for supplying autocomplete JSON.
 */
public class ApiAutocompleteServletBase extends BaseJsonServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.doGet(request, response);

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
