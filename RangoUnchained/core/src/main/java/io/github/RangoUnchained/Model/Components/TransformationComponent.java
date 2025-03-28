package io.github.RangoUnchained.Model.Components;

import com.badlogic.gdx.Gdx;

public class TransformationComponent implements Component {
    
    int heightScale;
    int widthScale;
    int radiusScale;

    double transformationHeightStep;
    double transformationWidthStep;
    double transformationRadiusStep;

    public static final int CIRCLE = 0;
    public static final int RECTANGLE = 1;
    public static final int UNKNOWN = -1;
    int type;


    public static final int UP = 10;
    public static final int LEFT = 11;
    public static final int DOWN = 12;
    public static final int RIGHT = 13;
    public static final int CENTER = 14;
    int direction;

    private boolean alwaysReverse;
    boolean autoReverse = false;
    boolean isReversed = false;
    final boolean REVERSE;
    final int LIFETIME;

    private int pause = 0;


    /**duration in frames */
    int duration;
       
    
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
            this.direction = direction;
    
            if (direction<10 || direction>14){
                direction = UNKNOWN;
            }
    
        }
    
        public int getType(){
            return type;
        }
    
        public int getHeightScale() {
            return heightScale;
        }
    
        public void setHeightScale(int heightScale) {
            this.heightScale = heightScale;
        }
    
        public int getWidthScale() {
            return widthScale;
        }
    
        public void setWidthScale(int widthScale) {
            this.widthScale = widthScale;
        }
    
        public int getRadiusScale() {
            return radiusScale;
        }
    
        public void setRadiusScale(int radiusScale) {
            this.radiusScale = radiusScale;
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
    
        public void setAutoReverse(boolean autoReverse){
            this.autoReverse = autoReverse;
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
