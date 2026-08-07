package v5.app.states;

import v5.domain.Member;

/**
 * Context is a shared memory object can be set value during the State change.
 * let State if self no need to have the field that over the scope of the fsm.
 * to bound the information in and out of fsm.
 * fsm would be defined at the start point of the application, so fsm flow would be fixed after application start, so state need a changeable object to react to mutation.
 */
public class RecordContext {
    public Member recorder;
}
