package wpd2.coursework1.servlet;

import java.util.ArrayList;
import java.util.List;

public class MessageBoard {
    private String name;
    private List<String> topics;

    protected MessageBoard() {
        this.topics = new ArrayList<>();
    }

    public String getName() {
        return this.name;
    }

    public List<String> getTopics() {
        return this.topics;
    }

    protected void setName(String n) {
        this.name = n;
    }

    protected void setTopics(List<String> t) {
        this.topics = t;
    }

    public String toString() {
        return ("name: " + name + ", topics: " + topics.toString());
    }
}
