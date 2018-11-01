package com.guojun.dondonji;

public class MotionDecoder {
    private int initialAngle = 0;
    private int count = 0;
    private int progress = 0;
    private State state = State.NONE;
    public int target_angle = 10;


    public void setInitAngle(int initialAngle) {
        this.initialAngle = initialAngle;
        target_angle += initialAngle;
        this.state = State.INITIAL;
    }


    public int getTarget_angle() {
        return target_angle;
    }

    public StatePack putAngle(int angle) {
        synchronized (this.state){
            switch (this.state) {
                case ACTION:
                    if (angle > target_angle) {
                        count += 1;
                        state = State.ANGLE_REACHED;
                    }
                    break;
                case ANGLE_REACHED:
                    if (angle <= initialAngle) {
                        state = State.INITIAL;
                    }
                    break;
                case INITIAL:
                    if (angle >= initialAngle) {
                        state = State.ACTION;
                    }
                    break;
            }
        }
        progress = angle;

        return new StatePack(this.count, this.progress - initialAngle, this.state);
    }


    enum State {
        NONE, INITIAL, ACTION, ANGLE_REACHED
    }

    class StatePack {
        int count;
        double progress;
        State status;

        public StatePack(int count, double progress, State status) {
            this.count = count;
            if (progress < 0) {
                progress = 0;
            }
            if (progress >= target_angle) {
                progress = target_angle;
            }

            this.status = status;
            this.progress = progress;
        }

        public int getCount() {
            return count;
        }

        public Double getProgress() {
            return progress;
        }

        public State getStatus() {
            return status;
        }
    }
}
