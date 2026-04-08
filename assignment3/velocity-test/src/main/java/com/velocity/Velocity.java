package com.velocity;

public class Velocity {
    private double speed;
    private double direction;
    private double speedX;
    private double speedY;

    public Velocity() {
        this.speed = 0;
        this.direction = 0;
        this.speedX = 0;
        this.speedY = 0;
    }

    public Velocity(double speed, double direction) {
        if (speed < 0) throw new IllegalArgumentException("Speed cannot be negative");
        if (direction < 0 || direction >= 360) throw new IllegalArgumentException("Direction must be in [0, 360)");
        this.speed = speed;
        this.direction = direction;
        computeComponents();
    }

    private void computeComponents() {
        double radians = Math.toRadians(direction);
        this.speedX = speed * Math.cos(radians);
        this.speedY = speed * Math.sin(radians);
    }

    public double getSpeed() { return speed; }
    public double getSpeedX() { return speedX; }
    public double getSpeedY() { return speedY; }
    public double getDirection() { return direction; }

    public void setSpeed(double speed) {
        if (speed < 0) throw new IllegalArgumentException("Speed cannot be negative");
        this.speed = speed;
        computeComponents();
    }

    public void setDirection(double direction) {
        if (direction < 0 || direction >= 360) throw new IllegalArgumentException("Direction must be in [0, 360)");
        this.direction = direction;
        computeComponents();
    }

    public void reverse() {
        this.direction = (this.direction + 180) % 360;
        computeComponents();
    }

    public void reverseX() {
        this.speedX = -this.speedX;
        this.direction = Math.toDegrees(Math.atan2(speedY, speedX));
        if (this.direction < 0) this.direction += 360;
    }

    public void reverseY() {
        this.speedY = -this.speedY;
        this.direction = Math.toDegrees(Math.atan2(speedY, speedX));
        if (this.direction < 0) this.direction += 360;
    }
}
