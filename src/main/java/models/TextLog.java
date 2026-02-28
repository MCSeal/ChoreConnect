package models;
public class TextLog extends Log {

    public TextLog(String title, String content) {
        super(title, content);
    }

    @Override
    public String toString() {
        return "TextLog{" +
                "id='" + getId() + '\'' +
                ", title='" + getTitle() + '\'' +
                ", content='" + getContent() + '\'' +
                ", timestamp='" + getTimestamp() + '\'' +
                '}';
    }
}