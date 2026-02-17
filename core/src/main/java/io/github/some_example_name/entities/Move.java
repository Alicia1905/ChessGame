package io.github.some_example_name.entities;

public class Move {
    public final int startX, startY;
    public final int endX, endY;

    public Move(int startX, int startY, int endX, int endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    @Override
    public String toString() {
        return "Move{" + startX + "," + startY + " -> " + endX + "," + endY + "}";
    }
}

