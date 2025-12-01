package events;

/**
 * Factory that builds a Command for a given payload.
 */
public interface CommandFactory<T> {
    Command create(T payload);
}
