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
    classStyle default hideEmptyMembers:true

    %% Base Classes
    class Shape {
        <<Abstract>>
        #double centerX
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
        +Shape tint(Color color)
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
        +static void setDefaultBorder(double width, String color)
        +static void setDefaultBorder(double width, int color, double alpha)
        +static void setDefaultFillColor(String color)
        +static void setDefaultFillColor(int color, double alpha)
        +static void setDefaultFillColor(int color)
    }

    class Group {
        +Group()
        +Group(Shape... shapes)
        +void add(Shape shape)
        +void add(Shape... shapes)
        +void remove(Shape shape)
        +void remove(int index)
        +T get(int index)
        +int indexOf(Shape shape)
        +int size()
        +void empty()
        +void destroyAllChildren()
        +List<Shape> getChildren()
    }

    class World {
        -static World currentWorld
        -double currentLeft
        -double currentTop
        -double currentWidth
        -double currentHeight
        -double worldRotationDeg
        -boolean flippedY
        -Color backgroundColor
        -List<Shape> allShapes
        -List<Shape> rootShapes
        -Group<? extends Shape> defaultGroup
        +World()
        +World(double width, double height)
        +static World getWorld()
        +static void clear()
        +Group<? extends Shape> getDefaultGroup()
        +void setDefaultGroup(Group<? extends Shape> defaultGroup)
        +void setBackgroundColor(Color color)
        +void setBackgroundColor(int color)
        +void setBackgroundColor(String color)
        +Color getBackgroundColor()
        +void move(double dx, double dy)
        +void rotate(double angleInDeg, double centerX, double centerY)
        +void scale(double factor, double centerX, double centerY)
        +void flipY()
        +void setCoordinateSystem(double left, double top, double width, double height)
        +void follow(Shape shape, double margin, double xMin, double xMax, double yMin, double yMax)
        +List<Shape> getAllShapes()
        +List<Shape> getRootShapes()
        +double getWidth()
        +double getHeight()
        +double getTop()
        +double getLeft()
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

    %% Helper Classes
    class Color {
        +int red
        +int green
        +int blue
        +int alpha
        +static Color parse(String color)
        +static Color fromInt(int color)
        +int toInt()
    }

    class Direction {
        <<Enumeration>>
        top
        right
        bottom
        left
    }

    Shape ..> Color : uses
    Shape ..> Direction : returns
```

This diagram shows the class hierarchy and relationships within the graphics library:

- **Shape**: Abstract base class for all graphical shapes with common properties and methods
- **FilledShape**: Abstract subclass of Shape for shapes with fill and border colors
- **Group**: Container class that can hold multiple shapes
- **World**: Manages the game world, all shapes, and provides rendering context
- **Concrete Shape Classes**: Circle, Rectangle, Triangle, Polygon, Line, Text, Arc, Ellipse, Sector, RoundedRectangle, TileImage, Bitmap
- **Color**: Helper class for color representation
- **Direction**: Enumeration for directional values
