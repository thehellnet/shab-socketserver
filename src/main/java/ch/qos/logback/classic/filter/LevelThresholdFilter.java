package ch.qos.logback.classic.filter;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

/**
 * Created by sardylan on 10/05/16.
 */
public class LevelThresholdFilter extends Filter<ILoggingEvent> {

    public enum Condition {
        GREATER,
        GREATER_EQUAL,
        LESSER,
        LESSER_EQUAL
    }

    private Level threshold = Level.WARN;
    private Condition condition = Condition.GREATER_EQUAL;

    @Override
    public FilterReply decide(ILoggingEvent event) {
        if (!isStarted()) {
            return FilterReply.NEUTRAL;
        }

        switch (condition) {
            case GREATER:
                return (event.getLevel().levelInt > threshold.levelInt) ? FilterReply.NEUTRAL : FilterReply.DENY;
            case GREATER_EQUAL:
                return (event.getLevel().levelInt >= threshold.levelInt) ? FilterReply.NEUTRAL : FilterReply.DENY;
            case LESSER:
                return (event.getLevel().levelInt < threshold.levelInt) ? FilterReply.NEUTRAL : FilterReply.DENY;
            case LESSER_EQUAL:
                return (event.getLevel().levelInt <= threshold.levelInt) ? FilterReply.NEUTRAL : FilterReply.DENY;
            default:
                return FilterReply.NEUTRAL;
        }
    }

    public Level getThreshold() {
        return threshold;
    }

    public void setThreshold(Level threshold) {
        this.threshold = threshold;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }
}
