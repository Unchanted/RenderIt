package com.arman.research.geom.transforms;

import com.arman.research.geom.vectors.Vector3f;

public class MovingTransform3f extends Transform3f {

    private static Vector3f temp = new Vector3f();

    private Vector3f velocity;
    private Movement velocityMovement;
    private Movement velocityAngleX;
    private Movement velocityAngleY;
    private Movement velocityAngleZ;

    public MovingTransform3f() {
        velocity = new Vector3f(0, 0, 0);
        velocityMovement = new Movement();
        velocityAngleX = new Movement();
        velocityAngleY = new Movement();
        velocityAngleZ = new Movement();
    }

    public MovingTransform3f(Transform3f t) {
        super(t);
        velocity = new Vector3f(0, 0, 0);
        velocityMovement = new Movement();
        velocityAngleX = new Movement();
        velocityAngleY = new Movement();
        velocityAngleZ = new Movement();
    }

    public void update(long elapsedTime) {
        float delta = velocityMovement.distance(elapsedTime);
        if (delta != 0) {
            temp.setTo(velocity);
            temp.multiply(delta);
            getLocation().add(temp);
        }
        rotateAngle(velocityAngleX.distance(elapsedTime), velocityAngleY.distance(elapsedTime), velocityAngleZ.distance(elapsedTime));
    }

    public void stop() {
        velocity.setTo(new Vector3f());
        velocityMovement.setTo(0, 0);
        velocityAngleX.setTo(0, 0);
        velocityAngleY.setTo(0, 0);
        velocityAngleZ.setTo(0, 0);
    }

    public void moveTo(Vector3f dest, float speed) {
        temp.setTo(dest);
        temp.subtract(getLocation());
        float dist = temp.length();
        long time = (long) (dist / speed);
        temp.divide(dist);
        temp.multiply(speed);
        setVelocity(temp, time);
    }

    public boolean isMoving() {
        return !velocityMovement.stopped() && !velocity.equals(new Vector3f());
    }

    public boolean isMovingIgnoringY() {
        return !velocityMovement.stopped() && (velocity.getX() != 0 || velocity.getZ() != 0);
    }

    public long getRemainingMoveTime() {
        if (!isMoving()) {
            return 0;
        } else {
            return velocityMovement.getRemainingTime();
        }
    }

    public Vector3f getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector3f v) {
        setVelocity(v, -1);
    }

    public void setVelocity(Vector3f v, long time) {
        velocity.setTo(v);
        if (v.equals(new Vector3f())) {
            velocityMovement.setTo(0, 0);
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

    public void rotateXTo(float ad, float speed) {
        rotateTo(velocityAngleX, getAngleX(), ad, speed);
    }

    public void rotateYTo(float ad, float speed) {
        rotateTo(velocityAngleY, getAngleY(), ad, speed);
    }

    public void rotateZTo(float ad, float speed) {
        rotateTo(velocityAngleZ, getAngleZ(), ad, speed);
    }

    public void rotateXTo(float y, float z, float ao, float speed) {
        rotateXTo((float) Math.atan2(-z, y) + ao, speed);
    }

    public void rotateYTo(float x, float z, float ao, float speed) {
        rotateYTo((float) Math.atan2(-z, x) + ao, speed);
    }

    public void rotateZTo(float x, float y, float ao, float speed) {
        rotateZTo((float) Math.atan2(y, x) + ao, speed);
    }

    public void rotateTo(Movement movement, float sa, float ea, float speed) {
        // TODO
        if (sa == ea) {
            movement.setTo(0, 0);
        } else {
            float distl;
            float distr;
            float pi2 = (float) (2 * Math.PI);
            if (sa < ea) {
                distl = sa - ea + pi2;
                distr = ea - sa;
            } else {
                distl = sa - ea;
                distr = ea - sa + pi2;
            }
            if (distl < distr) {
                speed = -Math.abs(speed);
                movement.setTo(speed, (long) (distl / -speed));
            } else {
                speed = Math.abs(speed);
                movement.setTo(speed, (long) (distr / speed));
            }
        }
    }

    public void setVelocityAngleX(float speed) {
        setVelocityAngleX(speed, -1);
    }

    public void setVelocityAngleY(float speed) {
        setVelocityAngleY(speed, -1);
    }

    public void setVelocityAngleZ(float speed) {
        setVelocityAngleZ(speed, -1);
    }

    public void setVelocityAngleX(float speed, long time) {
        velocityAngleX.setTo(speed, time);
    }

    public void setVelocityAngleY(float speed, long time) {
        velocityAngleY.setTo(speed, time);
    }

    public void setVelocityAngleZ(float speed, long time) {
        velocityAngleZ.setTo(speed, time);
    }

    public float getVelocityAngleX() {
        return isRotatingX() ? velocityAngleX.getSpeed() : 0;
    }

    public float getVelocityAngleY() {
        return isRotatingY() ? velocityAngleY.getSpeed() : 0;
    }

    public float getVelocityAngleZ() {
        return isRotatingZ() ? velocityAngleZ.getSpeed() : 0;
    }

    public boolean isRotatingX() {
        return !velocityAngleX.stopped();
    }

    public boolean isRotatingY() {
        return !velocityAngleY.stopped();
    }

    public boolean isRotatingZ() {
        return !velocityAngleZ.stopped();
    }

    public Object clone() {
        return new MovingTransform3f(this);
    }

    private static class Movement {

        float speed;
        long remainingTime;

        public void setTo(float speed, long time) {
            this.speed = speed;
            this.remainingTime = time;
        }

        public boolean stopped() {
            return (speed == 0) || (remainingTime == 0);
        }

        public float distance(long elapsedTime) {
            if (remainingTime == 0) {
                return 0;
            } else if (remainingTime != -1) {
                elapsedTime = Math.min(elapsedTime, remainingTime);
                remainingTime -= elapsedTime;
            }
            return speed * elapsedTime;
        }

        public long getRemainingTime() {
            return remainingTime;
        }

        public float getSpeed() {
            return speed;
        }

    }

}
