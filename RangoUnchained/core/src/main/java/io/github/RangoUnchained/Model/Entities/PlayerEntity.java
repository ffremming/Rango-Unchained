public class PlayerEntity {

    private VelocityComponent velocityComponent;
    private PositionComponent positionComponent;

    public PlayerEntity(VelocityComponent velocityComponent, PositionComponent positionComponent) {
        this.velocityComponent = velocityComponent;
        this.positionComponent = positionComponent;
    }

}
