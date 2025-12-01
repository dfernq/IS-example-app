package events;

import javax.swing.SwingUtilities;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Typed observer bus that executes Commands built from published payloads.
 */
public class EventBus {

    private final Map<AppEventType, List<CommandFactory<?>>> subscribers = new EnumMap<>(AppEventType.class);

    public <T> void subscribe(AppEventType type, CommandFactory<T> factory) {
        subscribers.computeIfAbsent(type, t -> new CopyOnWriteArrayList<>()).add(factory);
    }

    public <T> void publish(AppEventType type, T payload) {
        Runnable delivery = () -> {
            for (CommandFactory<?> rawFactory : subscribers.getOrDefault(type, List.of())) {
                @SuppressWarnings("unchecked")
                CommandFactory<T> factory = (CommandFactory<T>) rawFactory;
                Command command = factory.create(payload);
                if (command != null) {
                    command.execute();
                }
            }
        };

        if (SwingUtilities.isEventDispatchThread()) {
            delivery.run();
        } else {
            SwingUtilities.invokeLater(delivery);
        }
    }
}
