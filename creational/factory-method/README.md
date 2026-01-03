# Factory Method Pattern

## Intent
Define an interface for creating objects, but let subclasses decide which class to instantiate. Factory Method lets a class defer instantiation to subclasses.

## Problem
A framework needs to standardize the architectural model for applications but allow individual applications to define their own domain objects and provide for their instantiation.

## Solution
Define an abstract creator class that declares a factory method which returns an object of an abstract product type. Subclasses override the factory method to create and return specific product types.

## Structure
- **Product**: Interface for objects the factory method creates
- **ConcreteProduct**: Implements the Product interface
- **Creator**: Declares the factory method
- **ConcreteCreator**: Overrides factory method to return ConcreteProduct

## When to Use
- A class can't anticipate the class of objects it must create
- A class wants its subclasses to specify the objects it creates
- Classes delegate responsibility to one of several helper subclasses

## Benefits
- Eliminates need to bind application-specific classes into code
- Provides hooks for subclasses
- Connects parallel class hierarchies
- Code deals with interfaces, not concrete classes

## Drawbacks
- Can require creating a subclass just to instantiate a particular ConcreteProduct
- More classes to maintain

## Real-World Examples
- Database connectors (MySQL, PostgreSQL, Oracle)
- Document creation in applications (PDF, Word, Excel)
- UI components for different platforms
- Logger implementations
- Payment gateway integrations
