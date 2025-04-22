package io.github.RangoUnchained.Model.Components;

/**
 * Component representing transformation behavior over time for an entity,
 * such as scaling and directional movement.
 */
public class TransformationComponent implements Component {

    public static final int CIRCLE = 0;
    public static final int RECTANGLE = 1;
    public static final int UNKNOWN = -1;

    public static final int UP = 10;
    public static final int LEFT = 11;
    public static final int DOWN = 12;
    public static final int RIGHT = 13;

    private final int heightScale;
    private final int widthScale;
    private final int radiusScale;
    private int type;
    private int direction;
    private int duration;
    private int pause;

    private boolean alwaysReverse;
    private boolean isReversed = false;
    private final boolean REVERSE;
    private final int LIFETIME;

    private double transformationHeightStep;
    private double transformationWidthStep;
    private double transformationRadiusStep;


        public TransformationComponent(int heightScale, int widthScale, int radiusScale, int duration, int type, int direction, boolean reverse, int pause){
            this.heightScale = heightScale;
            this.widthScale = widthScale;
            this.radiusScale = radiusScale;
            this.duration = duration;

            setType(type);
            setDirection(direction);
            LIFETIME = duration;
            setTransformationSteps();
            REVERSE = reverse;

            this.pause = pause;
        }

        public void setTransformationSteps(){
            this.transformationHeightStep = (float) Math.pow(heightScale, 1.0 / LIFETIME);
            this.transformationWidthStep = (float) Math.pow(widthScale, 1.0 / LIFETIME);
            this.transformationRadiusStep = (float) Math.pow(radiusScale, 1.0 / LIFETIME);
        }

        public void setTransformationStepsReverse(){
            this.transformationHeightStep = calculateInverseScalingFactor(heightScale,LIFETIME);
            this.transformationWidthStep = calculateInverseScalingFactor(widthScale,LIFETIME);
            this.transformationRadiusStep = calculateInverseScalingFactor(radiusScale,LIFETIME);
        }

        // Method to calculate the inverse scaling factor
        public static double calculateInverseScalingFactor(double scale, int durations) {
            return Math.pow(scale, -1.0 / durations);
        }

        private void setType(int type){
            if (type == CIRCLE){
                this.type = CIRCLE;
            } else if (type == RECTANGLE){
                this.type = RECTANGLE;
            } else {
                this.type = UNKNOWN;
            }
        }

        private void setDirection(int direction){
            if (direction < 10 || direction > 14) {
                this.direction = UNKNOWN;
            } else {
                this.direction = direction;
            }

        }

        public int getType(){
            return type;
        }

        public double getTransformationHeightStep() {
            return transformationHeightStep;
        }

        public double getTransformationWidthStep() {
            return transformationWidthStep;
        }

        public double getTransformationRadiusStep() {
            return transformationRadiusStep;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public void decrementDuration(){
            duration --;
        }

        public int getPause(){
            return pause;
        }

        public int getDirection() {
            return direction;
        }

        public void setAlwaysReverse(boolean alwaysReverse) {
            this.alwaysReverse = alwaysReverse;
    }

		public int getLifeTime() {
			return LIFETIME;
		}

        public boolean getReverse() {
            return REVERSE;
        }

        public boolean isReversed() {
            return isReversed;
        }

        public boolean getAlwaysReverse() {
           return alwaysReverse;
        }

        public void decrementPauser() {
            pause --;
        }
}
