# Builder Pattern

## Intent
Separate the construction of a complex object from its representation, allowing the same construction process to create different representations.

## Problem
Creating complex objects with many optional parameters or configuration steps can lead to constructors with numerous parameters (telescoping constructor problem) or objects in inconsistent states.

## Solution
Extract object construction code into separate builder objects. The builder provides methods to configure the object step by step, then builds and returns the final product.

## Structure
- **Builder**: Interface for creating parts of a Product
- **ConcreteBuilder**: Constructs and assembles parts, defines representation
- **Director**: Constructs an object using the Builder interface
- **Product**: Complex object under construction

## When to Use
- Object creation involves many steps
- Object has many optional parameters
- Different representations of the object are needed
- Construction process must allow different representations

## Benefits
- Separates construction from representation
- Provides fine control over construction process
- Allows varying product's internal representation
- Isolates complex construction code

## Drawbacks
- Increases overall code complexity
- Requires creating a separate builder for each type of product

## Real-World Examples
- StringBuilder in Java
- SQL query builders
- HTTP request builders
- Configuration objects
- Test data builders
- Document builders (HTML, XML)
