package com.guojun.dondonji;

public class ExerciseAdapter {
    private Double initialAngle = null;
    private double target_angle = 0;
    private int count = 0;
    private int progress = 0;
    private State state = State.NONE;
    private static final Double RANGE = 20.0;
    private Double priAngle = null;

    public MotionInfo putAngle(double angle) {
        if(priAngle != null){
            if(Math.abs(priAngle - angle) > 30) {
                angle = priAngle;
            }
        }
        priAngle = angle;

        if (initialAngle == null) {
            initialAngle = angle;
//            target_angle = initialAngle + RANGE;

            state = State.INITIAL;
        }

        double moved_angle = angle - initialAngle;
        switch (this.state) {
            case ACTION:
                if (moved_angle > RANGE) {
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
                if (moved_angle > 0) {
                    state = State.ACTION;
                }
                break;
        }
        progress = Double.valueOf(100 * ((moved_angle) / RANGE)).intValue();


        return new MotionInfo(this.count, this.progress, this.state);
    }


    enum State {
        NONE, INITIAL, ACTION, ANGLE_REACHED
    }

    class MotionInfo {
        private int count;
        private int progress;
        private State status;

        public MotionInfo(int count, int progress, State status) {
            this.status = status;
            this.count = count;
            this.progress = progress;
        }

        public int getCount() {
            return count;
        }

        public int getProgress() {
            return progress;
        }

        public State getStatus() {
            return status;
        }
    }
}
