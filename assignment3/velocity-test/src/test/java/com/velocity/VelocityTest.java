package com.velocity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VelocityTest {

    private static final double DELTA = 1e-6;

    @Test
    public void testDefaultConstructor() {
        Velocity v = new Velocity();
        assertEquals(0, v.getSpeed(), DELTA);
        assertEquals(0, v.getDirection(), DELTA);
        assertEquals(0, v.getSpeedX(), DELTA);
        assertEquals(0, v.getSpeedY(), DELTA);
    }

    @Test
    public void testParameterizedConstructor() {
        Velocity v = new Velocity(10, 90);
        assertEquals(10, v.getSpeed(), DELTA);
        assertEquals(90, v.getDirection(), DELTA);
        assertEquals(10 * Math.cos(Math.toRadians(90)), v.getSpeedX(), DELTA);
        assertEquals(10 * Math.sin(Math.toRadians(90)), v.getSpeedY(), DELTA);
    }

    @Test
    public void testConstructorNegativeSpeedThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Velocity(-1, 90));
    }

    @Test
    public void testConstructorInvalidDirectionThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Velocity(10, 360));
    }

    @Test
    public void testGetSpeed() {
        Velocity v = new Velocity(15, 45);
        assertEquals(15, v.getSpeed(), DELTA);
    }

    @Test
    public void testGetDirection() {
        Velocity v = new Velocity(10, 180);
        assertEquals(180, v.getDirection(), DELTA);
    }

    @Test
    public void testGetSpeedX() {
        Velocity v = new Velocity(10, 0);
        assertEquals(10 * Math.cos(Math.toRadians(0)), v.getSpeedX(), DELTA);
    }

    @Test
    public void testGetSpeedY() {
        Velocity v = new Velocity(10, 90);
        assertEquals(10 * Math.sin(Math.toRadians(90)), v.getSpeedY(), DELTA);
    }

    @Test
    public void testSetDirection() {
        Velocity v = new Velocity(10, 0);
        v.setDirection(90);
        assertEquals(90, v.getDirection(), DELTA);
        assertEquals(10 * Math.cos(Math.toRadians(90)), v.getSpeedX(), DELTA);
        assertEquals(10 * Math.sin(Math.toRadians(90)), v.getSpeedY(), DELTA);
    }

    @Test
    public void testSetSpeed() {
        Velocity v = new Velocity(10, 90);
        v.setSpeed(20);
        assertEquals(20, v.getSpeed(), DELTA);
        assertEquals(90, v.getDirection(), DELTA);
        assertEquals(20 * Math.cos(Math.toRadians(90)), v.getSpeedX(), DELTA);
        assertEquals(20 * Math.sin(Math.toRadians(90)), v.getSpeedY(), DELTA);
    }

    @Test
    public void testReverseEast() {
        Velocity v = new Velocity(10, 0);
        v.reverse();
        assertEquals(180, v.getDirection(), DELTA);
        assertTrue(v.getSpeedX() < 0);
        assertEquals(0, v.getSpeedY(), DELTA);
    }

    @Test
    public void testReverseNorth() {
        Velocity v = new Velocity(10, 90);
        v.reverse();
        assertEquals(270, v.getDirection(), DELTA);
        assertTrue(v.getSpeedY() < 0);
        assertEquals(0, v.getSpeedX(), DELTA);
    }

    @Test
    public void testReverseWest() {
        Velocity v = new Velocity(10, 180);
        v.reverse();
        assertEquals(0, v.getDirection(), DELTA);
        assertTrue(v.getSpeedX() > 0);
        assertEquals(0, v.getSpeedY(), DELTA);
    }

    @Test
    public void testReverseTwiceReturnsOriginal() {
        Velocity v = new Velocity(10, 45);
        double originalX = v.getSpeedX();
        double originalY = v.getSpeedY();
        v.reverse();
        v.reverse();
        assertEquals(45, v.getDirection(), DELTA);
        assertEquals(originalX, v.getSpeedX(), DELTA);
        assertEquals(originalY, v.getSpeedY(), DELTA);
    }

    @Test
    public void testReverseZeroSpeed() {
        Velocity v = new Velocity(0, 90);
        v.reverse();
        assertEquals(270, v.getDirection(), DELTA);
        assertEquals(0, v.getSpeedX(), DELTA);
        assertEquals(0, v.getSpeedY(), DELTA);
        assertEquals(0, v.getSpeed(), DELTA);
    }

    @Test
    public void testReverseXEast() {
        Velocity v = new Velocity(10, 0);
        double originalSpeedX = v.getSpeedX();
        v.reverseX();
        assertEquals(-originalSpeedX, v.getSpeedX(), DELTA);
        assertEquals(0, v.getSpeedY(), DELTA);
        assertEquals(10, v.getSpeed(), DELTA);
    }

    @Test
    public void testReverseXDiagonal() {
        Velocity v = new Velocity(10, 45);
        double originalSpeedY = v.getSpeedY();
        double originalSpeedX = v.getSpeedX();
        v.reverseX();
        assertEquals(-originalSpeedX, v.getSpeedX(), DELTA);
        assertEquals(originalSpeedY, v.getSpeedY(), DELTA);
        assertEquals(10, v.getSpeed(), DELTA);
    }

    @Test
    public void testReverseXTwiceReturnsOriginal() {
        Velocity v = new Velocity(10, 45);
        double originalSpeedX = v.getSpeedX();
        double originalSpeedY = v.getSpeedY();
        v.reverseX();
        v.reverseX();
        assertEquals(originalSpeedX, v.getSpeedX(), DELTA);
        assertEquals(originalSpeedY, v.getSpeedY(), DELTA);
    }

    @Test
    public void testReverseXPurelyVertical() {
        Velocity v = new Velocity(10, 90);
        double originalSpeedY = v.getSpeedY();
        v.reverseX();
        assertEquals(0, v.getSpeedX(), DELTA);
        assertEquals(originalSpeedY, v.getSpeedY(), DELTA);
        assertEquals(10, v.getSpeed(), DELTA);
    }

    @Test
    public void testReverseXZeroSpeed() {
        Velocity v = new Velocity(0, 45);
        v.reverseX();
        assertEquals(0, v.getSpeedX(), DELTA);
        assertEquals(0, v.getSpeedY(), DELTA);
        assertEquals(0, v.getSpeed(), DELTA);
    }
}
