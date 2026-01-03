# State Pattern

## Intent
Allow an object to alter its behavior when its internal state changes. The object will appear to change its class.

## Problem
An object's behavior depends on its state, and it must change behavior at runtime based on that state. Large conditional statements make code hard to maintain.

## Solution
Create separate state classes for each possible state. The context delegates state-specific behavior to the current state object.

## Structure
- **Context**: Maintains instance of ConcreteState subclass, defines interface of interest to clients
- **State**: Interface for encapsulating behavior associated with particular state
- **ConcreteState**: Each subclass implements behavior associated with a state

## When to Use
- Object behavior depends on its state
- Operations have large multipart conditional statements based on state
- State-specific behavior should be independent
- State transitions should be explicit

## Benefits
- Localizes state-specific behavior
- Makes state transitions explicit
- State objects can be shared
- Eliminates large conditional statements
- Easy to add new states

## Drawbacks
- Increases number of classes
- Can be overkill for simple state machines

## Real-World Examples
- TCP connection states
- Document workflow (draft, review, published)
- Vending machines
- Order processing (new, paid, shipped, delivered)
- Media players (playing, paused, stopped)
- Game character states
