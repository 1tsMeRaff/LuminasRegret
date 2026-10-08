package com.luminasregret.engine.physics;

/**
 * Pure primitive mathematical utilities for Axis-Aligned Bounding Box (AABB) collisions.
 * Operates purely on the CPU stack / registers with ZERO heap allocations.
 */
public final class CollisionMath {

    private CollisionMath() {
        // Utility class - non-instantiable
    }

    /**
     * Checks if two rectangles defined by (x, y, width, height) overlap.
     * Equivalent to java.awt.Rectangle.intersects without heap allocation.
     */
    public static boolean intersects(int x1, int y1, int w1, int h1,
                                     int x2, int y2, int w2, int h2) {
        return x1 < x2 + w2 &&
               x1 + w1 > x2 &&
               y1 < y2 + h2 &&
               y1 + h1 > y2;
    }
}
