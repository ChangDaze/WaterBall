package webframework.exceptions;

import java.util.ArrayList;
import java.util.List;

import webframework.http.HttpStatus;

/**
 * The registered exception-to-status mappings, first match wins.
 */
public final class ExceptionRuleRegistry {

    private final List<ExceptionRule> rules = new ArrayList<>();
    private ExceptionRule defaultRule = new DefaultExceptionRule();

    public ExceptionRuleRegistry register(ExceptionRule rule) {
        rules.add(rule);
        return this;
    }

    /**
     * Convenience for the overwhelmingly common "this type means that status" mapping.
     */
    public ExceptionRuleRegistry register(Class<? extends Throwable> exceptionType, HttpStatus status) {
        return register(new ExceptionTypeRule(exceptionType, status));
    }

    /**
     * Replaces the catch-all. Rarely needed — an application that wants to hide internal messages
     * from clients would do it here.
     */
    public ExceptionRuleRegistry setDefaultRule(ExceptionRule defaultRule) {
        this.defaultRule = defaultRule;
        return this;
    }

    /**
     * @return the first registered rule that matches, or the default rule; never {@code null}
     */
    public ExceptionRule resolve(Throwable throwable) {
        for (ExceptionRule rule : rules) {
            if (rule.matches(throwable)) {
                return rule;
            }
        }
        return defaultRule;
    }
}
