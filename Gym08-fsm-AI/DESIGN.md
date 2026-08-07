# Waterball 社群機器人 — Design Document
Course: 水球軟體學院 軟體設計模式精通之旅 — Chapter 4 boss: 社群機器人引擎 | 有限狀態機框架
Language: Java (17+; records & pattern matching assumed)

This document is the single source of truth for implementation. The design phase is complete;
do not redesign while implementing. Testcases live in `testcases/` (unit-tests + integration-tests);
`.out` files are ground truth and outrank this document if they ever disagree.

---

## 1. Package / layer map (dependencies point DOWN only)

```
app      → bot, domain, fsm, events     (Main, InputDispatcher, WaterballBot, states, guards/actions)
bot      → fsm, events                  (abstract Bot — Template Method)
domain   → events                       (Community world + rich domain events)
fsm      → events                       (engine + composite plugin package fsm.composite)
events   → nothing                      (shared kernel: Event marker, EventListener port)
```

Rules:
- `grep -r "^import" domain/` must show only events/java imports. Same discipline per layer.
- Deleting `fsm/composite/` must leave `fsm/` compiling (sub-machine support is a plugin — OCP).
- All object wiring happens in `Main` (composition root). Objects never wire themselves.

## 2. events (shared kernel)

- `Event` — empty marker interface.
- `EventListener` — `void onEvent(Event e)`.

## 3. fsm (generic engine — zero domain knowledge)

- `Guard` — `@FunctionalInterface boolean test(Event e)`; constant `ALWAYS = e -> true`.
  Guards READ, never write (they may be evaluated speculatively).
- `Action` — `@FunctionalInterface void execute(Event e)`; constant `NONE = e -> {}`.
  Actions WRITE, never decide.
- `State` — interface; `onEnter()`, `onExit()`, `handle(Event e)` — all default no-op.
  Only StateMachine ever calls these.
- `Transition` — record `(State from, Class<? extends Event> eventType, Guard guard, Action action, State to)`
  with `matches(current, e) = from == current && eventType.isInstance(e)`.
  No nulls — use ALWAYS / NONE.
- `StateMachine` — fields: `Supplier<State> initialState`, `List<Transition> transitions`, `State current`.
  API: `addTransition(t)`, `start()`, `handle(Event e)`, `stop()`, `currentState()`.

`handle(e)` algorithm (ordering is load-bearing):
```
if current == null: return
current.handle(e)                                  // ① state reacts FIRST
t = first transition where t.matches(current,e) && t.guard.test(e)   // first-match-wins, list order
if t: current.onExit(); t.action.execute(e); current = t.to; current.onEnter()
// no match → silence, not an error
```
`start()`: `current = initialState.get()` (re-resolved EVERY start — conditional initial states), then `onEnter`.
`stop()`: `current.onExit(); current = null`.

### fsm.composite (plugin)
`CompositeState implements State`, has-a inner `StateMachine`:
`onEnter → inner.start()`, `handle → inner.handle(e)`, `onExit → inner.stop()`.
Exit cascades inside-out; re-entry resets inner machine (this is what restarts carousels).

### fsm test checklist (must be green before moving on)
1. start() enters initial (log `enterA`)
2. handle invokes current state's handle
3. Golden: `[handleB, exitB, actionB2, enterA]` and current changed
4. Guard false → only `[handleB]`
5. Wrong event type → guard never invoked (spy guard)
6. Two transitions same event type → first passing wins; swapping order flips result
7. Composite: events reach inner current state
8. Composite exit: `[exitInner, exitComposite]`
9. Re-entry resets inner; Supplier re-evaluated at each start
10. Depth-3 nesting works
11. Spec's A/B/E1/E2/E3 example reproduced (b + b2 + a ordering)

## 4. domain (the world — functional with zero bots)

Aggregate root & Mediator: `Community`
- owns: `members` (insertion-ordered; online list), `chatRoom`, `broadcast`, `forum`, `clock`, `quota`, `listeners`
- API: `start(time, quota)`, `login(userId, isAdmin)`, `logout(userId)`,
  `sendMessage(authorId, content, tags)` (+ overload for bot Member), `newPost(...)`, `addComment(...)`,
  `goBroadcasting(speakerId)`, `speak(speakerId, content)`, `stopBroadcasting(speakerId)`,
  `elapse(amount, unit)`, `addListener(l)`, `registerBot(botId) : Member`, private `notifyListeners(e)`.
- INVARIANT (every mutation method): mutate → echo → notify, in that order.
- Ids resolve to Member at the door (`findOnlineMember`); inside the domain, objects only. `assertSame` in tests.
- Constructor takes `Consumer<String> printer` (default `System.out::println`) for testability.

Classes: `Member(userId:String, isAdmin, loginOrder; display prefix — bot renders 🤖, humans 💬 N)`,
`ChatRoom(messages)`, `Message(author:Member, content, tags:String[])` — record,
`Broadcast(currentSpeaker:Member|null, speaks; isOnAir())`, `Speak(speaker:Member, content)` — record,
`Forum(posts)`, `Post(id:String!, title, content, tags:String[], author:Member, comments)` — entity,
`Comment(author:Member, content)` — record,
`Clock(now:LocalDateTime; elapse prints "🕑 {amount} {unit} elapsed..." — echoes amount+unit verbatim, NOT absolute time)`,
`Quota(remaining:int; canAfford(cost), consume(cost))` — ONE shared pool; 額度 numbers are COSTS per use
(proven by integration `quota.in`: 20 →king−5→15 →−5→10 →−5→5 →record−3→2 → both further commands denied).

Echo formats — copy byte-for-byte from `.out` files. Known: `💬 3: record @bot`, `🤖: good to hear @3`,
`📢 4 is broadcasting...`, `📢 4: Test`, `📢 4 stop broadcasting`, `🤖 comment in post 101: Nice post @2`,
bot broadcast: `🤖 go broadcasting...` / `🤖 speaking: The winner is 1` / `🤖 stop broadcasting...`,
replay: `🤖: [Record Replay] Line1\nLine2 @<recorder>`.

### domain events (in `domain.events`, implement kernel `Event`)
`LoginEvent(Member)`, `LogoutEvent(Member)`, `NewMessageEvent(Message)`, `NewPostEvent(Post)`,
`GoBroadcastingEvent(Member)`, `SpeakEvent(Speak)`, `BroadcastStoppedEvent(Member)`, `TimeElapsedEvent(amount, unit)`.
Optional refinement (chosen): `interface AuthoredEvent extends Event { Member creator(); }`
implemented by the five authored events → polymorphic self-filter.

## 5. bot (framework)

```java
public abstract class Bot implements EventListener {
    protected final StateMachine machine;
    public final void onEvent(Event e) { if (isMyOwn(e)) return; machine.handle(e); }  // Template Method
    protected abstract boolean isMyOwn(Event e);   // hook — base can't name domain types
}
```
Self-filter is MANDATORY: notify fires on the bot's own replies too (re-entrant); without the filter → event storm.
Later (milestone F): fluent builder DSL for transitions lives here.

## 6. app (Waterball-specific)

- `Main` — composition root: build Community(printer), `registerBot("bot")`, new WaterballBot(community, botMember),
  `community.addListener(bot)`, `rootMachine` started via bot; stdin loop, `[end]` breaks (control signal, not domain).
- `InputDispatcher` — regex ELAPSED first: `^\[(\d+) (seconds|hours) elapsed\]$` → `community.elapse(...)`;
  then GENERAL: `^\[(.+?)\](?: (\{.*\}))?$` → handler map name → typed Community call (Jackson/Gson readTree).
  Unknown name / bad JSON → throw loudly. Method-call style chosen (no input-event object family).
- `WaterballBot extends Bot` — holds `self:Member`, `RecordContext{Member recorder}`, `KingContext{questionDeadline, thanksDeadline}`;
  builds three CompositeState INSTANCES (normal/record/king — not subclasses) + root machine; all guards/actions are
  lambdas capturing domain objects (closure trick). `isMyOwn`: `e instanceof AuthoredEvent a && a.creator() == self`.
- States (all implement fsm State):
  - `DefaultConversationState` — Carousel; onEnter reset; on message reply next; on new post: `Nice post @<author>`
  - `InteractingState` — Carousel; onEnter reset; on new post: `How do you guys think about it? @bot, @1, ...` (login order)
  - `WaitingState` — nothing
  - `RecordingState` — buffer speaks; onEnter clear; **onExit flushes replay** (fires on BOTH stop-recording and stop broadcasting), tags recorder
  - `QuestioningState` — QuestionBank, scores Map<Member,Integer>; onEnter: reset scores/index, stamp 1h deadline, ask Q0; judge answers, Congrats/next
  - `ThanksForJoiningState` — onEnter: stamp 20s deadline; announce: if broadcast.isOnAir() → chat `🤖: The winner is 1` else bot broadcasts 3 lines; winner = top score, tie → `Tie!`
- `Carousel(replies[], index; next() wraps, reset())` — extracted (rule: ≥2 users + domain name + invariant)
- `Question`/`QuestionBank` — 3 fixed questions, render `"<i>. <text>\nA) ...\nB) ...\nC) ...\nD) ..."`

### Transition tables (the app's spec)
ROOT (normal / record / king composites):
| from | event | guard | action | to |
|---|---|---|---|---|
| normal | NewMessage | cmd "record" && canAfford(3) [permission? → grep spec] | consume 3; recordCtx.recorder = author | record |
| record | NewMessage | cmd "stop-recording" && author == recorder | — (flush in Recording.onExit) | normal |
| normal | NewMessage | cmd "king" && author.isAdmin && canAfford(5) | consume 5; say "KnowledgeKing is started!" | king |
| king | NewMessage | cmd "king-stop" [permission/output? → grep] | ? | normal |
| king | TimeElapsed | thanksDeadline != null && now >= it | clear ctx | normal |
| record | ? | stop-broadcasting also returns to normal? → verify spec/integration `recording.normal` | | |

normal.inner: Default ⇄ Interacting on Login/Logout, guard online ≥10 / <10 (COUNT INCLUDES BOT).
record.inner: initial supplier = `broadcast.isOnAir() ? recording : waiting`; Waiting→Recording on GoBroadcasting; Recording→Waiting on BroadcastStopped.
king.inner: Questioning→Thanks on (3rd correct) or (now ≥ questionDeadline); Thanks→Questioning on cmd "play again" && canAfford(5), action consume 5 + say "KnowledgeKing is gonna start again!".

cmd(name) helper: NewMessageEvent && content == name && tags contain bot.
Command failures are SILENT; state handles message FIRST (carousel reply), then transition scan → both fall out of the handle() ordering.

### Open string/behavior items — resolve by grepping testcases/spec BEFORE coding each state
1. Exact carousel sequences for Default and Interacting (spec + `_MessageCycle` tests)
2. Wrong-answer reply format (`KnowledgeKing_IncorrectAnswer.out`)
3. king-stop / record permissions; king-stop output (silent?)
4. Record: does stop broadcasting return to normal or idle in waiting? (spec wording; integration `recording.normal`)
5. Bot broadcast rendering template location (Broadcast renders by member prefix)
6. Winner announcement ordering after 3rd Congrats (should fall out of handle() ordering — verify)

## 7. Build plan (do IN ORDER; never start N+1 with N red)

A1 skeleton → A2 kernel+fsm tiny types → A3 Transition+test → A4 machine happy path → A5 golden test
`[handleB, exitB, actionB2, enterA]` → A6 first-match-wins → B1 CompositeState → B2 cascade+reset →
B3 depth-3 + spec example (FSM FROZEN) → C1 Member+Community → C2 ChatRoom → C3 Broadcast → C4 Forum →
C5 Clock+Quota → C6 events+publishing (mutate→echo→notify; 10th-login freshness test) (DOMAIN FROZEN) →
D1 InputDispatcher+parse tests → D2 harness (parameterized JUnit: run each `.in`, diff `.out`) →
D3 Bot shell + Default (first testcases green) → D4 Interacting+login transitions →
E1 Record → E2 KnowledgeKing questions → E3 ThanksForJoining/timeouts/play-again → E4 integration green →
F1 extract bot DSL (refactor under green tests) + optional ArchUnit layer rules.

## 8. Contracts summary (write as javadoc — graded thinking)
1. Only StateMachine calls state lifecycle methods.
2. Guards read, never write; actions write, never decide.
3. First matching transition wins; registration order is configuration.
4. No match = silence.
5. Fire order: exit → action → enter.
6. initialState is a Supplier re-resolved per start().
7. No nulls in transitions (ALWAYS / NONE).
8. Community: mutate → echo → notify; ids die at the door; whoever mutates, prints.
9. One dispatch table per boundary: InputDispatcher map (in), listener list (across), FSM (reaction). Never stack them.
