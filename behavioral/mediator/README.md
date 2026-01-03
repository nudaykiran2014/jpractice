# Mediator Pattern

## Intent
Define an object that encapsulates how a set of objects interact. Mediator promotes loose coupling by keeping objects from referring to each other explicitly.

## Problem
Objects in a system communicate in complex ways, creating tight coupling and making the system difficult to understand and modify.

## Solution
Create a mediator object that handles communication between objects. Objects communicate only with the mediator, not directly with each other.

## Structure
- **Mediator**: Interface for communicating with Colleague objects
- **ConcreteMediator**: Implements cooperative behavior by coordinating Colleague objects
- **Colleague**: Each Colleague knows its Mediator object
- **ConcreteColleague**: Communicates with other Colleagues through Mediator

## When to Use
- Set of objects communicate in complex ways
- Reusing object is difficult due to dependencies
- Behavior distributed between classes should be customizable
- Too many relationships make system hard to understand

## Benefits
- Decouples colleagues
- Simplifies object protocols
- Centralizes control
- Individual components become simpler
- Easy to understand object cooperation

## Drawbacks
- Mediator can become complex and monolithic
- Can be hard to maintain if it does too much

## Real-World Examples
- Air traffic control
- Chat room applications
- GUI dialog coordinators
- MVC controller
- Event dispatchers
- Workflow engines
