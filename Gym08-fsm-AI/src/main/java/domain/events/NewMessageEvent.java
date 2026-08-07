package domain.events;

import domain.Member;
import domain.Message;

public record NewMessageEvent(Message message) implements AuthoredEvent {
    @Override
    public Member creator() {
        return message.author();
    }
}
