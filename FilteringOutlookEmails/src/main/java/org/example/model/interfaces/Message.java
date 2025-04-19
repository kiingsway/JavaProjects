package org.example.model.interfaces;

public record Message(
        String id,
        String subject,
        String bodyPreview,
        String from,
        String receivedDateTime,
        String webLink
) {

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", subject='" + subject + '\'' +
                ", from='" + from + '\'' +
                ", receivedDateTime='" + receivedDateTime + '\'' +
                ", bodyPreview='" + bodyPreview + '\'' +
                ", webLink='" + webLink + '\'' +
                '}';
    }
}
