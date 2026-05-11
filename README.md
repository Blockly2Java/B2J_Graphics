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

    %% Base Classes
    class Shape {
        <<Abstract>>
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
        #double centerY
        #double angleDeg
        #double scaleFactor
        #boolean visible
        #boolean isStatic
        #Color fillColor
        #Color borderColor
        #double borderWidth
        #World world
        #Group<? extends Shape> belongsToGroup
        #static boolean defaultVisibility
        +Shape copy()
        +Shape setFillColor(Color color)
        +Shape setFillColor(int color)
        +Shape setFillColor(String color)
        +Shape setBorderColor(Color color)
        +Shape setBorderColor(int color)
        +Shape setBorderColor(String color)
        +Shape setBorderWidth(double width)
        +Shape setAlpha(double alpha)
        +Shape setVisible(boolean visible)
        +Shape setStatic(boolean isStatic)
        +Shape move(double dx, double dy)
        +Shape moveTo(double x, double y)
        +Shape setX(double x)
        +Shape setY(double y)
        +Shape rotate(double angleDeg)
        +Shape scale(double factor)
        +Shape mirrorX()
        +Shape mirrorY()
        +Shape defineDirection(double angleDeg)
        +Shape forward(double distance)
        +double getAngle()
        +boolean containsPoint(double x, double y)
        +boolean isOutsideView()
        +Shape tint(int color)
        +Shape tint(String color)
        +Shape tint(Color color)Shape: Abstract base class for all graphical shapes with common properties an
        +Direction directionRelativeTo(Shape other)
        +Shape moveBackFrom(Shape other, boolean keepColliding)
        +boolean collidesWith(Shape other)
        +boolean collidesWithAnyShape()
        +Shape getFirstCollidingShape()
        +List<Shape> getCollidingShapes(Group<? extends Shape> group)
        +World getWorld()
        +void destroy()
        +Shape defineCenter(double x, double y)
        +Shape defineCenterRelative(double relX, double relY)
        +double getCenterX()
        +double getCenterY()
        +Shape bringToFront()
        +Shape sendToBack()
        +Color getFillColor()
        +int getFillColorAsInt()
        +Color getBorderColor()
        +int getBorderColorAsInt()
        +double getBorderWidth()
        +double getAlpha()
        +boolean isVisible()
        +boolean isStatic()
        +static void setDefaultVisibility(boolean visible)
    }

    class FilledShape {
        <<Abstract>>
        +setDefaultBorder(double, String)
        +setDefaultFillColor(String)
    }

    class Group {
        +add(Shape...)
        +remove(Shape)
        +get(int)
        +size()
        +getChildren()
    }

    class World {
        +getWidth()
        +getHeight()
        +getBackgroundColor()
        +setBackgroundColor(Color|int|String)
        +move(double, double)
        +follow(Shape, double)
        +getAllShapes()
    }

    %% Shape Inheritance
    Shape <|-- FilledShape
    Shape <|-- Circle
    Shape <|-- Rectangle
    Shape <|-- Triangle
    Shape <|-- Polygon
    Shape <|-- Line
    Shape <|-- Text
    Shape <|-- Arc
    Shape <|-- Ellipse
    Shape <|-- Sector
    Shape <|-- RoundedRectangle
    Shape <|-- TileImage
    Shape <|-- Bitmap

    %% FilledShape Inheritance
    FilledShape <|-- Circle
    FilledShape <|-- Rectangle
    FilledShape <|-- Triangle
    FilledShape <|-- Polygon
    FilledShape <|-- Arc
    FilledShape <|-- Ellipse
    FilledShape <|-- Sector
    FilledShape <|-- RoundedRectangle

    %% Group Relationship
    Group <|-- Shape
    Group "1" *-- "many" Shape : contains

    %% World Relationship
    World "1" *-- "many" Shape : manages
    World "1" *-- "1" Group : defaultGroup

    Shape <|-- FilledShape
    Shape <|-- Circle
    Shape <|-- Rectangle
    Shape <|-- Triangle
    Shape <|-- Polygon
    Shape <|-- Line
    Shape <|-- Text
    Shape <|-- Arc
    Shape <|-- Ellipse
    Shape <|-- Sector
    Shape <|-- RoundedRectangle
    Shape <|-- TileImage
    Shape <|-- Bitmap

    FilledShape <|-- Circle
    FilledShape <|-- Rectangle
    FilledShape <|-- Triangle
    FilledShape <|-- Polygon
    FilledShape <|-- Arc
    FilledShape <|-- Ellipse
    FilledShape <|-- Sector
    FilledShape <|-- RoundedRectangle

    Group <|-- Shape
    Group "1" *-- "many" Shape : contains
    World "1" *-- "many" Shape : manages
```

This diagram shows the public API for using the graphics library:

- **Shape**: Base class for all shapes with common transformation and rendering methods
- **FilledShape**: Subclass for shapes with fill colors
- **Group**: Container for organizing multiple shapes
- **World**: Manages the game world and all shapes
- **Concrete Shape Classes**: Circle, Rectangle, Triangle, Polygon, Line, Text, Arc, Ellipse, Sector, RoundedRectangle, TileImage, Bitmap
