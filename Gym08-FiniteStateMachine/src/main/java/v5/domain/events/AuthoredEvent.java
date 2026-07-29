package v5.domain.events;

import v5.domain.Member;
import v5.events.Event;

/**
 * use to extend Event with get creator method
 * currently offer the capability let Bot know if the event cause by Bot
 */
public interface AuthoredEvent extends Event {
    Member creator();
}
