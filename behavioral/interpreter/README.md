# Interpreter Pattern

## Intent
Given a language, define a representation for its grammar along with an interpreter that uses the representation to interpret sentences in the language.

## Problem
You have a language to interpret and can represent statements in the language as abstract syntax trees.

## Solution
Define a class hierarchy representing the grammar. Each class implements an interpret operation that interprets a portion of the abstract syntax tree.

## Structure
- **AbstractExpression**: Interface for executing interpretation
- **TerminalExpression**: Implements interpret for terminal symbols
- **NonterminalExpression**: Implements interpret for grammar rules
- **Context**: Contains information global to interpreter
- **Client**: Builds abstract syntax tree and invokes interpret

## When to Use
- Grammar is simple
- Efficiency is not critical
- Want to represent grammar as class hierarchy
- Easy to change and extend grammar

## Benefits
- Easy to change and extend grammar
- Implementing grammar is straightforward
- Adding new ways to interpret expressions is easy

## Drawbacks
- Complex grammars are hard to maintain
- Not efficient for complex grammars
- Can become large and unwieldy

## Real-World Examples
- SQL parsing
- Regular expressions
- Mathematical expression evaluators
- Configuration file parsers
- Scripting language interpreters
- Compiler front-ends
