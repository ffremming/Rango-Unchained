### Model

In our model we have implemented ECS and Factory pattern

This will be an explanation of how our model works and interacts with the controller as it does 
get a bit complicated. 

## ECS

- Components store variables that have a value. In other words, a component store a state. 
The components are named after what state they are responsible for storing.
- Entities have one or more components attached to it through a hashmap. An entity can only have 
one component of a certain type in the map and you can fetch it by calling 
"entity.getComponent(componentType.class)"
- Systems are responsible for manipulating the components of entities. The systems are divided into
logical components, each responsible for changing the state in some predefined way for every entity with 
the correct components. MovementSystem for instance changes the position of a playerEntity by taking the 
inputComponent and changing the velocity of the bodyComponent according to the direction it should be moving

## Factory
