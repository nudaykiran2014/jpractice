# Memento Pattern

## Intent
Without violating encapsulation, capture and externalize an object's internal state so that the object can be restored to this state later.

## Problem
You need to save and restore an object's state without exposing its internal structure or violating encapsulation.

## Solution
Create memento objects that store snapshots of the originator's state. A caretaker requests mementos from the originator and stores them.

## Structure
- **Memento**: Stores internal state of Originator
- **Originator**: Creates memento with current state, uses memento to restore state
- **Caretaker**: Responsible for memento's safekeeping, never operates on or examines memento contents

## When to Use
- Must save and restore object state
- Direct interface to state would expose implementation details
- Undo/redo functionality needed
- Transaction rollback required

## Benefits
- Preserves encapsulation boundaries
- Simplifies Originator
- Provides easy undo/redo mechanism
- Maintains state history

## Drawbacks
- Can be expensive if state is large
- Caretaker might not know when to delete old mementos
- Increased memory usage

## Real-World Examples
- Text editor undo/redo
- Database transactions
- Game save states
- Browser history
- Configuration snapshots
- Version control systems
