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
        +copy() Shape
        +setFillColor(Color|int|String) Shape
        +setBorderWidth(double) Shape
        +setAlpha(double) Shape
        +setVisible(boolean) Shape
        +setStatic(boolean) Shape
        +move(double, double) Shape
        +moveTo(double, double) Shape
        +setX(double) Shape
        +setY(double) Shape
        +rotate(double) Shape
        +scale(double) Shape
        +mirrorX() Shape
        +mirrorY() Shape
        +forward(double) Shape
        +getAngle() double
        +containsPoint(double, double) boolean
        +tint(Color|int|String) Shape
        +collidesWith(Shape) boolean
        +destroy() void
        +getWorld() World
        +bringToFront() Shape
        +sendToBack() Shape
        +getCenterX() double
        +getCenterY() double
        +setAngle(double) Shape
        +isOutsideView() boolean
        +collidesWithAnyShape() boolean
        +getFirstCollidingShape() Shape
        +getCollidingShapes(Group) List~Shape~
        +directionRelativeTo(Shape) Direction
        +moveBackFrom(Shape, boolean) Shape
        +defineCenter(double, double) Shape
        +defineCenterRelative(double, double) Shape
        +setDefaultVisibility(boolean) void
        +isStatic() boolean
        +isVisible() boolean
        +getFillColor() Color
        +getFillColorAsInt() int
        +getBorderColor() Color
        +getBorderColorAsInt() int
        +getBorderWidth() double
        +getAlpha() double
    }

    class FilledShape {
        <<Abstract>>
        +setDefaultBorder(double, String) void
        +setDefaultBorder(double, int, double) void
        +setDefaultFillColor(String) void
        +setDefaultFillColor(int, double) void
        +setDefaultFillColor(int) void
    }

    class Group {
        +Group()
        +Group(Shape...)
        +add(Shape) void
        +add(Shape...) void
        +remove(Shape) void
        +remove(int) void
        +get(int) T
        +indexOf(Shape) int
        +size() int
        +empty() void
        +destroyAllChildren() void
        +getChildren() List~Shape~
        +getCollidingShapes(Shape) List~Shape~
    }

    class World {
        +World()
        +World(double, double)
        +static World getWorld() World
        +static void clear() void
        +getWidth() double
        +getHeight() double
        +getTop() double
        +getLeft() double
        +getDefaultGroup() Group~Shape~
        +setDefaultGroup(Group~Shape~) void
        +setBackgroundColor(Color|int|String) void
        +getBackgroundColor() Color
        +move(double, double) void
        +rotate(double, double, double) void
        +scale(double, double, double) void
        +flipY() void
        +setCoordinateSystem(double, double, double, double) void
        +setCursor(String) void
        +follow(Shape, double, double, double, double, double) void
        +addMouseListener(Object) void
        +getAllShapes() List~Shape~
        +getRootShapes() List~Shape~
    }

    class Circle {
        +Circle()
        +Circle(double, double, double)
        +setRadius(double) Circle
        +getRadius() double
        +copy() Circle
    }

    class Rectangle {
        +Rectangle()
        +Rectangle(double, double, double, double)
        +setWidth(double) Rectangle
        +setHeight(double) Rectangle
        +getWidth() double
        +getHeight() double
        +moveTo(double, double) Rectangle
        +copy() Rectangle
    }

    class Triangle {
        +Triangle()
        +Triangle(double, double, double, double, double, double)
        +setPoints(double, double, double, double, double, double) Triangle
        +copy() Triangle
    }

    class Polygon {
        +Polygon()
        +Polygon(boolean)
        +Polygon(boolean, double...)
        +Polygon(Shape)
        +addPoint(double, double) void
        +setPoints(double[]) void
        +addPoints(double[]) void
        +insertPoint(double, double, int) void
        +movePointTo(double, double, int) void
        +open() void
        +close() void
        +copy() Polygon
    }

    class Line {
        +Line()
        +Line(double, double, double, double)
        +setPoints(double, double, double, double) Line
        +copy() Line
    }

    class Text {
        +Text()
        +Text(double, double, double, String)
        +Text(double, double, double, String, String)
        +Text(double, double, double, double)
        +Text(double, double, double, double, String)
        +setFontsize(double) void
        +getFontsize() double
        +setText(String) void
        +setText(double) void
        +getText() String
        +setAlignment(Alignment) void
        +setStyle(boolean, boolean) void
        +getWidth() double
        +getHeight() double
        +moveTo(double, double) Text
        +copy() Text
    }

    class Arc {
        +Arc()
        +Arc(double, double, double, double, double, double)
        +setInnerRadius(double) void
        +getInnerRadiusX() double
        +setOuterRadius(double) void
        +getOuterRadiusX() double
        +setStartAngle(double) void
        +getStartAngleX() double
        +setEndAngle(double) void
        +getEndAngleX() double
        +copy() Arc
    }

    class Ellipse {
        +Ellipse()
        +Ellipse(double, double, double, double)
        +setRadiusX(double) Ellipse
        +setRadiusY(double) Ellipse
        +getRadiusX() double
        +getRadiusY() double
        +copy() Ellipse
    }

    class Sector {
        +Sector()
        +Sector(double, double, double, double, double)
        +setRadius(double) void
        +getRadiusX() double
        +setStartAngle(double) void
        +getStartAngleX() double
        +setEndAngle(double) void
        +getEndAngleX() double
        +drawRadii(boolean) void
        +copy() Sector
    }

    class RoundedRectangle {
        +RoundedRectangle()
        +RoundedRectangle(double, double, double, double, double)
        +setWidth(double) RoundedRectangle
        +setHeight(double) RoundedRectangle
        +setRadius(double) RoundedRectangle
        +getWidth() double
        +getHeight() double
        +getRadius() double
        +moveTo(double, double) RoundedRectangle
        +copy() RoundedRectangle
    }

    class TileImage {
        +move(double, double) void
        +scale(double) void
        +mirrorX() void
        +mirrorY() void
        +getOffsetX() double
        +getOffsetY() double
        +getScaleX() double
        +getScaleY() double
        +getSprite() Sprite
    }

    Shape <|-- Circle
    Shape <|-- Rectangle
    Shape <|-- Triangle
    Shape <|-- Polygon
    Shape <|-- Line
    Shape <|-- Ellipse
    Shape <|-- RoundedRectangle
    Shape <|-- Group

    FilledShape <|-- Polygon
    FilledShape <|-- Text
    FilledShape <|-- Arc
    FilledShape <|-- Sector

    Group "1" *-- "many" Shape : contains
    World "1" *-- "many" Shape : manages
```

This diagram shows the public API for using the graphics library:

- **Shape**: Base class for all shapes with common transformation and rendering methods
- **FilledShape**: Helper class for shapes with fill colors (provides static default setters)
- **Group**: Container for organizing multiple shapes (extends Shape)
- **World**: Manages the game world and all shapes
- **Concrete Shape Classes**: Circle, Rectangle, Triangle, Polygon, Line, Text, Arc, Ellipse, Sector, RoundedRectangle, TileImage
- **Abstract Shape Classes**: Shape, FilledShape, Group
```