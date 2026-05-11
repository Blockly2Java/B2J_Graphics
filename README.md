# Bockly2Java Local Dependencies

A graphics library for [Blockly2Java](https://github.com/ValentinHerrmann/Bockly2Java) (or the base project [OnlineIDE](https://github.com/martin-pabst/Online-IDE-new-compiler)) projects, providing local execution support when online environment dependencies aren't available.

## Purpose

This project serves as a bridge for Blockly2Java applications that need to run locally without relying on OnlineIDE's online environment dependencies. It provides the necessary components to execute Blockly2Java code in a local development environment.

## Getting started
Add the following dependency to you `pom.xml`
```
<dependency>
    <groupId>de.blockly2java</groupId>
    <artifactId>graphics</artifactId>
    <version>[0.1.0,)</version> <!-- e.g. any version newer than 0.1.0 -->
</dependency>
```

## UML

### Class Diagram

```mermaid
classDiagram
    %% Hide private and protected members
    hide emptyMembers

    class Shape {
        <<Abstract>>
        +Shape()
        +Shape(double, double)
        +copy()
        +setFillColor(Color|int|String)
        +setBorderColor(Color|int|String)
        +setBorderWidth(double)
        +setAlpha(double)
        +setVisible(boolean)
        +setStatic(boolean)
        +move(double, double)
        +moveTo(double, double)
        +setX(double)
        +setY(double)
        +rotate(double)
        +scale(double)
        +mirrorX()
        +mirrorY()
        +forward(double)
        +getAngle()
        +containsPoint(double, double)
        +tint(Color|int|String)
        +collidesWith(Shape)
        +destroy()
        +getWorld()
    }

    class FilledShape {
        <<Abstract>>
        +setDefaultBorder(double, String)
        +setDefaultFillColor(String)
    }

    class Group {
        +Group()
        +Group(Shape...)
        +add(Shape...)
        +remove(Shape)
        +get(int)
        +size()
        +getChildren()
    }

    class World {
        +World()
        +World(double, double)
        +static World getWorld()
        +static void clear()
        +getWidth()
        +getHeight()
        +getBackgroundColor()
        +setBackgroundColor(Color|int|String)
        +move(double, double)
        +follow(Shape, double)
        +getAllShapes()
    }

    Shape <|-- Circle
    Shape <|-- Rectangle
    Shape <|-- Triangle
    Shape <|-- Line
    Shape <|-- Text
    Shape <|-- Ellipse
    Shape <|-- RoundedRectangle
    Shape <|-- TileImage
    Shape <|-- Bitmap

    FilledShape <|-- Sector
    FilledShape <|-- Polygon
    FilledShape <|-- Arc

    Group <|-- Shape
    Group "1" *-- "many" Shape : contains
    World "1" *-- "many" Shape : manages
```

This diagram shows the public API for using the graphics library:

- **Shape**: Base class for all shapes with common transformation and rendering methods
- **FilledShape**: Helper class for shapes with fill colors (provides static default setters)
- **Group**: Container for organizing multiple shapes
- **World**: Manages the game world and all shapes
- **Concrete Shape Classes**: Circle, Rectangle, Triangle, Polygon, Line, Text, Arc, Ellipse, Sector, RoundedRectangle, TileImage, Bitmap
