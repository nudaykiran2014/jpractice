# Facade Pattern

## Intent
Provide a unified interface to a set of interfaces in a subsystem. Facade defines a higher-level interface that makes the subsystem easier to use.

## Problem
Complex systems with many interdependent classes can be difficult to use and understand. Clients need to know about many classes and their interactions.

## Solution
Create a facade class that provides a simple interface to the complex subsystem. The facade delegates client requests to appropriate subsystem objects.

## Structure
- **Facade**: Provides simplified interface to subsystem, delegates client requests
- **Subsystem Classes**: Implement subsystem functionality, handle work assigned by Facade
- **Client**: Uses Facade instead of subsystem classes directly

## When to Use
- Provide simple interface to complex subsystem
- Decouple subsystem from clients and other subsystems
- Layer subsystems with facades as entry points
- Want to wrap poorly designed APIs

## Benefits
- Simplifies interface to complex system
- Decouples subsystem from clients
- Promotes weak coupling
- Easier to use and understand
- Flexibility to use subsystem classes directly if needed

## Drawbacks
- Facade can become god object coupled to all subsystem classes
- Can limit access to advanced features
- May not eliminate complexity entirely

## Real-World Examples
- Compiler interface to lexer, parser, code generator
- Video conversion libraries
- Database access layers
- Web service facades
- Home theater systems
- Payment gateway integrations
