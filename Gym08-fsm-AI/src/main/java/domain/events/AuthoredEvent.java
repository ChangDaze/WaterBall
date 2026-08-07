package domain.events;

import domain.Member;
import events.Event;

/**
 * Refinement over the five events a member (including the bot itself)
 * can author — enables the bot's polymorphic self-filter.
 */
public interface AuthoredEvent extends Event {
    Member creator();
}
