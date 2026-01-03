# Observer Pattern

## Intent
Define a one-to-many dependency between objects so that when one object changes state, all its dependents are notified and updated automatically.

## Problem
You need to maintain consistency between related objects without making them tightly coupled. Changes in one object should automatically update dependent objects.

## Solution
Define a subject that maintains a list of observers and notifies them automatically of any state changes. Observers register themselves with the subject.

## Structure
- **Subject**: Knows its observers, provides interface for attaching/detaching observers
- **ConcreteSubject**: Stores state, sends notification to observers when state changes
- **Observer**: Interface for objects that should be notified of changes
- **ConcreteObserver**: Maintains reference to ConcreteSubject, implements Observer interface

## When to Use
- Change to one object requires changing others
- Object should notify other objects without assumptions about them
- Abstraction has two aspects, one dependent on the other
- Event-driven systems

## Benefits
- Loose coupling between subject and observers
- Support for broadcast communication
- Dynamic relationships between subjects and observers
- Easy to add new observers

## Drawbacks
- Unexpected updates can occur
- Can cause memory leaks if observers aren't properly deregistered
- Order of notification is not guaranteed

## Real-World Examples
- Event listeners in GUI frameworks
- MVC architecture (Model notifies View)
- Publish-subscribe systems
- Social media notifications
- Stock market price updates
- RSS feeds
