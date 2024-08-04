package com.arman.research.geom.transforms;

import com.arman.research.geom.vectors.Vector3f;

public class MovingTransform3f extends Transform3f {

    private static final Vector3f TEMP_VECTOR = new Vector3f();
    private static final Vector3f ZERO_VECTOR = new Vector3f();

    private Vector3f velocity;
    private Movement velocityMovement;
    private Movement velocityAngleX;
    private Movement velocityAngleY;
    private Movement velocityAngleZ;

    public MovingTransform3f() {
        initialize();
    }

    public MovingTransform3f(Transform3f t) {
        super(t);
        initialize();
    }

    private void initialize() {
        velocity = new Vector3f();
        velocityMovement = new Movement();
        velocityAngleX = new Movement();
        velocityAngleY = new Movement();
        velocityAngleZ = new Movement();
    }

    public void update(long elapsedTime) {
        updatePosition(elapsedTime);
        updateRotation(elapsedTime);
    }

    private void updatePosition(long elapsedTime) {
        float delta = velocityMovement.distance(elapsedTime);
        if (delta != 0) {
            TEMP_VECTOR.setTo(velocity).multiply(delta);
            getLocation().add(TEMP_VECTOR);
        }
    }

    private void updateRotation(long elapsedTime) {
        rotateAngle(
            velocityAngleX.distance(elapsedTime),
            velocityAngleY.distance(elapsedTime),
            velocityAngleZ.distance(elapsedTime)
        );
    }

    public void stop() {
        velocity.setTo(ZERO_VECTOR);
        resetMovement(velocityMovement);
        resetMovement(velocityAngleX);
        resetMovement(velocityAngleY);
        resetMovement(velocityAngleZ);
    }

    private void resetMovement(Movement movement) {
        movement.setTo(0, 0);
    }

    public void moveTo(Vector3f dest, float speed) {
        TEMP_VECTOR.setTo(dest).subtract(getLocation());
        float distance = TEMP_VECTOR.length();
        long time = (long) (distance / speed);
        TEMP_VECTOR.normalize().multiply(speed);
        setVelocity(TEMP_VECTOR, time);
    }

    public boolean isMoving() {
        return !velocityMovement.stopped() && !velocity.equals(ZERO_VECTOR);
    }

    public boolean isMovingIgnoringY() {
        return !velocityMovement.stopped() && (velocity.getX() != 0 || velocity.getZ() != 0);
    }

    public long getRemainingMoveTime() {
        return isMoving() ? velocityMovement.getRemainingTime() : 0;
    }

    public Vector3f getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector3f v) {
        setVelocity(v, -1);
    }

    public void setVelocity(Vector3f v, long time) {
        velocity.setTo(v);
        if (v.equals(ZERO_VECTOR)) {
            resetMovement(velocityMovement);
        } else {
            velocityMovement.setTo(1, time);
        }
    }

    public void addVelocity(Vector3f v) {
        if (isMoving()) {
            velocity.add(v);
        } else {
            setVelocity(v);
        }
    }

    public void rotateXTo(float targetAngle, float speed) {
        rotateTo(velocityAngleX, getAngleX(), targetAngle, speed);
    }

    public void rotateYTo(float targetAngle, float speed) {
        rotateTo(velocityAngleY, getAngleY(), targetAngle, speed);
    }

    public void rotateZTo(float targetAngle, float speed) {
        rotateTo(velocityAngleZ, getAngleZ(), targetAngle, speed);
    }

    public void rotateXTo(float y, float z, float offsetAngle, float speed) {
        rotateXTo((float) Math.atan2(-z, y) + offsetAngle, speed);
    }

    public void rotateYTo(float x, float z, float offsetAngle, float speed) {
        rotateYTo((float) Math.atan2(-z, x) + offsetAngle, speed);
    }

    public void rotateZTo(float x, float y, float offsetAngle, float speed) {
        rotateZTo((float) Math.atan2(y, x) + offsetAngle, speed);
    }

    private void rotateTo(Movement movement, float startAngle, float endAngle, float speed) {
        if (startAngle == endAngle) {
            movement.setTo(0, 0);
            return;
        }

        float distanceClockwise = calculateDistanceClockwise(startAngle, endAngle);
        float distanceCounterClockwise = calculateDistanceCounterClockwise(startAngle, endAngle);

        if (distanceClockwise < distanceCounterClockwise) {
            movement.setTo(-Math.abs(speed), (long) (distanceClockwise / -speed));
        } else {
            movement.setTo(Math.abs(speed), (long) (distanceCounterClockwise / speed));
        }
    }

    private float calculateDistanceClockwise(float startAngle, float endAngle) {
        float pi2 = (float) (2 * Math.PI);
        return startAngle < endAngle ? startAngle - endAngle + pi2 : startAngle - endAngle;
    }

    private float calculateDistanceCounterClockwise(float startAngle, float endAngle) {
        float pi2 = (float) (2 * Math.PI);
        return startAngle < endAngle ? endAngle - startAngle : endAngle - startAngle + pi2;
    }
}
