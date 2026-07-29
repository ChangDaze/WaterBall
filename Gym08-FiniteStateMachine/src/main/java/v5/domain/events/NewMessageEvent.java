package v5.domain.events;

import v5.domain.Member;
import v5.domain.Message;

public record NewMessageEvent(Message message) implements AuthoredEvent {
    @Override
    public Member creator() {
        return message.author();
    }
}
