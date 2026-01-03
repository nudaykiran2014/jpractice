# Visitor Pattern

## Intent
Represent an operation to be performed on elements of an object structure. Visitor lets you define a new operation without changing the classes of the elements on which it operates.

## Problem
You need to perform operations on objects in a complex structure, but adding new operations requires modifying each class. This violates the Open/Closed Principle.

## Solution
Create visitor classes that implement operations. Elements accept visitors and call the appropriate visitor method. New operations are added by creating new visitors.

## Structure
- **Visitor**: Interface declaring visit operation for each ConcreteElement
- **ConcreteVisitor**: Implements each operation declared by Visitor
- **Element**: Interface defining accept method that takes visitor
- **ConcreteElement**: Implements accept to call visitor method corresponding to its class
- **ObjectStructure**: Can enumerate its elements, may provide interface for visitor

## When to Use
- Object structure contains many classes with differing interfaces
- Many distinct operations need to be performed on objects
- Classes defining object structure rarely change
- Want to avoid polluting classes with unrelated operations

## Benefits
- Easy to add new operations
- Groups related operations
- Can accumulate state while visiting elements
- Can visit elements across class hierarchies

## Drawbacks
- Adding new ConcreteElement classes is difficult
- Breaks encapsulation of element classes
- Can be complex to understand

## Real-World Examples
- Compiler operations (type checking, code generation)
- Document structure operations (spell check, word count)
- Tax calculation systems
- File system operations
- AST (Abstract Syntax Tree) traversal
- Report generation
