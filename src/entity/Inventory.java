package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Encapsulated Inventory container for Entities and Items.
 * Adheres to Effective Java: Item 17 (Minimizing mutability) and
 * Item 50 (Make defensive copies when needed).
 */
public class Inventory {

    private final int maxSize;
    private final List<Entity> items;

    public Inventory(int maxSize) {
        this.maxSize = Math.max(1, maxSize);
        this.items = new ArrayList<>(this.maxSize);
    }

    public boolean isFull() {
        return items.size() >= maxSize;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public boolean add(Entity item) {
        if (item == null || isFull()) {
            return false;
        }
        return items.add(item);
    }

    public Entity remove(int index) {
        if (index >= 0 && index < items.size()) {
            return items.remove(index);
        }
        return null;
    }

    public boolean remove(Entity item) {
        return items.remove(item);
    }

    public Entity get(int index) {
        if (index >= 0 && index < items.size()) {
            return items.get(index);
        }
        return null;
    }

    public int size() {
        return items.size();
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void clear() {
        items.clear();
    }

    /**
     * Returns an unmodifiable view of the current items list.
     * Prevents external mutation of the internal container.
     */
    public List<Entity> getItems() {
        return Collections.unmodifiableList(items);
    }
}
