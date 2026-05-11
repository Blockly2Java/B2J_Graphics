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
---
  config:
    class:
      hideEmptyMembersBox: true
      hidePrivateMembers: true
---
classDiagram

    class Shape {
        <<Abstract>>

        + Color getFillColor()
        + int getFillColorAsInt()
        + Color getBorderColor()
        + int getBorderColorAsInt()
        + double getBorderWidth()
        + double getAlpha()
        + boolean isVisible()
        + boolean isStatic()
        + double getCenterX()
        + double getCenterY()
        + double getAngle()
        + Shape getFirstCollidingShape()
        + List<Shape> getCollidingShapes(Group<? extends Shape> group)
        + World getWorld()

        + Shape setFillColor(Color|String|int color)
        + Shape setFillColor(Color|String|int color, double alpha)
        + Shape setBorderColor(Color|String|int color)
        + Shape setBorderColor(Color|String|int color, double alpha)
        + Shape setBorderWidth(double width)
        + Shape setAlpha(double alpha)
        + Shape setVisible(boolean visible)
        + Shape setStatic(boolean isStatic)
        + Shape setX(double x)
        + Shape setY(double y)
        + Shape setScale(double newScale)
        + Shape setAngle(double newAngle)
        + Shape tint(Color|String|int color)

        + Shape bringToFront()
        + Shape sendToBack()
        + Shape move(double dx, double dy)
        + Shape moveTo(double x, double y)
        + Shape rotate(double angleDeg)
        + Shape rotate(double angleDeg, double pivotX, double pivotY)
        + Shape scale(double factor)
        + Shape scale(double factor, double pivotX, double pivotY)
        + Shape mirrorX()
        + Shape mirrorY()
        %% + Shape defineDirection(double angleDeg)
        + Shape forward(double distance)
        + boolean containsPoint(double x, double y)
        + boolean isOutsideView()
        + Direction directionRelativeTo(Shape other)
        + Shape moveBackFrom(Shape other, boolean keepColliding)
        + boolean collidesWith(Shape other)
        + boolean collidesWithAnyShape()
        + void destroy()
        %% + Shape defineCenter(double x, double y)
        %% + Shape defineCenterRelative(double relX, double relY)
        %% # Bounds getBounds()
        %% # List<Point> getLocalPoints()
        %% # List<Point> getTransformedPoints()
        %% # Point transformPoint(Point local)
        %% # Point inverseTransformPoint(double x, double y)
    }
    class Group {
        + Group()
        + Group(Shape... shapes)

        + T get(int index)
        + int indexOf(Shape shape)
        + int size()
        + List<Shape> getChildren()
        + List<Shape> getCollidingShapes(Shape other)

        + Shape move(double dx, double dy)
        + void add(Shape... shapes)
        + void remove(Shape shape)
        + void remove(int index)
        + void empty()
        + void destroyAllChildren()
        %% + Group<T> copy()
        + boolean containsPoint(double x, double y)
        + boolean collidesWith(Shape other)
        %% # Bounds getBounds()
        %%void bringChildToFront(Shape child)
        %%void sendChildToBack(Shape child)
        %% - boolean containsRecursively(Shape shape)
        %% - void addInternal(Shape shape)
        %% - void updateCenterFromChildren()
        %% + String toString() 
    }
    class World {
        + World()
        + World(double width, double height)
        %%void registerShape(Shape shape) 
        %%void unregisterFromDefaultList(Shape shape) 
        %%void deregisterShape(Shape shape) 
        %%void bringToFront(Shape shape) 
        %%void sendToBack(Shape shape)

        + double getWidth()
        + double getHeight()
        + double getTop()
        + double getLeft()
        + Group<? extends Shape> getDefaultGroup()
        + Color getBackgroundColor()
        + List<Shape> getAllShapes()
        %% - void collectShapes(Shape shape, List<Shape> target)
        + List<Shape> getRootShapes()

        + void setDefaultGroup(Group<? extends Shape> defaultGroup)
        + void setBackgroundColor(Color|String|int color)
        + void setCoordinateSystem(double left, double top, double width, double height)
        + void setCursor(String cursor)

        + void move(double dx, double dy)
        + void rotate(double angleInDeg, double centerX, double centerY)
        + void scale(double factor, double centerX, double centerY)
        + void flipY()
        + void follow(Shape shape, double margin, double xMin, double xMax, double yMin, double yMax)
        + void addMouseListener(Object mouseListener)
    }
    class Circle {
        + Circle()
        + Circle(double x, double y, double r)

        + double getRadius()

        + Circle setRadius(double radius)

        %% + Circle copy()
        + boolean containsPoint(double x, double y)
        %% # Bounds getBounds()
        %% + String toString()
    }
    class Rectangle {
        + Rectangle()
        + Rectangle(double left, double top, double width, double height)

        + double getWidth()
        + double getHeight()

        + Rectangle setWidth(double width)
        + Rectangle setHeight(double height)

        + Rectangle moveTo(double x, double y)
        %% + Rectangle copy()
        %% # java.util.List<Point> getLocalPoints()
        + boolean containsPoint(double x, double y)
        %% + String toString()
        %% - double getLeft()
        %% - double getTop()
    }
    class Triangle {
        + Triangle()
        + Triangle(double x1, double y1, double x2, double y2, double x3, double y3)

        + Triangle setPoints(double x1, double y1, double x2, double y2, double x3, double y3)

        %% + Triangle copy()
        %% # java.util.List<Point> getLocalPoints()
        + boolean containsPoint(double x, double y)
        %% + String toString()
        %% - void setLocalPointsFromWorld(double wx1, double wy1, double wx2, double wy2, double wx3, double wy3)
    }
    class Polygon {
        + Polygon()
        + Polygon(boolean closeAndFill)
        + Polygon(boolean closeAndFill, double... coordinates)
        + Polygon(Shape shape)

        + void setPoints(double[] points)

        + void addPoint(double x, double y)
        + void addPoints(double[] points)
        + void insertPoint(double x, double y, int index)
        + void open()
        + void close()
        + boolean containsPoint(double x, double y)
        %% # List<Point> getLocalPoints()
        %% + Polygon copy()
        %% + String toString()
        %% # void setPointsInternal(double[] coordinates)
        %% - boolean pointInPolygon(double x, double y)
        %% - double distanceToPolyline(double x, double y)
        %% - double distancePointToSegment(double px, double py, double ax, double ay, double bx, double by)
    }
    class Line {
        + Line()
        + Line(double x1, double y1, double x2, double y2)

        + Line setPoints(double x1, double y1, double x2, double y2)

        %% + Line copy()
        %% # java.util.List<Point> getLocalPoints()
        + boolean containsPoint(double x, double y)
        %% + String toString()
        %% - void setLocalPointsFromWorld(double wx1, double wy1, double wx2, double wy2)
    }
    class FilledShape {
        <<Abstract>>

        %% # FilledShape()
        %% # FilledShape(double x, double y)
        
        + FilledShape setFillColor(Color|String|int color)
        + FilledShape setFillColor(Color|String|int color, double alpha)
        + FilledShape setBorderColor(Color|String|int color)
        + FilledShape setBorderColor(Color|String|int color, double alpha)

        + FilledShape setBorderWidth(double width)
        + FilledShape setAlpha(double alpha)
    }
    class Text {
        + Text()
        + Text(double x, double y, double fontSize, String|double text)
        + Text(double x, double y, double fontSize, String|double text, String fontFamily)

        + double getFontsize()
        + String getText()
        + double getWidth()
        + double getHeight()

        + void setFontsize(double fontsize)
        + void setText(String|double text)
        + void setAlignment(Alignment alignment)
        + void setStyle(boolean bold, boolean italic)

        + Text moveTo(double x, double y)
        %% + Text copy()
        %% # Bounds getBounds()
        %% + String toString()
        %% - void recalcBounds()
    }
    class Arc {
        + Arc()
        + Arc(double mx, double my, double innerRadius, double outerRadius, double startAngle, double endAngle)

        + double getInnerRadiusX()
        + double getOuterRadiusX()
        + double getStartAngleX()
        + double getEndAngleX()

        + void setInnerRadius(double innerRadius)
        + void setOuterRadius(double outerRadius)
        + void setStartAngle(double startAngle)
        + void setEndAngle(double endAngle)

        + boolean containsPoint(double x, double y)
        %% # Bounds getBounds()
        %% + Arc copy()
        %% + String toString()
    }
    class Ellipse {
        + Ellipse()
        + Ellipse(double x, double y, double rX, double rY)

        + double getRadiusX()
        + double getRadiusY()

        + Ellipse setRadiusX(double radiusX)
        + Ellipse setRadiusY(double radiusY)

        %% + Ellipse copy()
        %% # Bounds getBounds()
        %% # java.util.List<Point> getLocalPoints()
        %% + String toString()
    }
    class Sector {
        + Sector()
        + Sector(double mx, double my, double radius, double startAngle, double endAngle)

        + double getRadiusX()
        + double getStartAngleX()
        + double getEndAngleX()

        + void setRadius(double radius)
        + void setStartAngle(double startAngle)
        + void setEndAngle(double endAngle)
        + void drawRadii(boolean drawRadii)

        + boolean containsPoint(double x, double y)
        %% # Bounds getBounds()
        %% + Sector copy()
        %% + String toString()
        %% - boolean angleWithin(double angle, double start, double end)
    }
    class RoundedRectangle {
        + RoundedRectangle()
        + RoundedRectangle(double left, double top, double width, double height, double radius)

        + double getWidth()
        + double getHeight()
        + double getRadius()

        + RoundedRectangle setWidth(double width)
        + RoundedRectangle setHeight(double height)
        + RoundedRectangle setRadius(double radius)

        + RoundedRectangle moveTo(double x, double y)
        %% + RoundedRectangle copy()
        %% # java.util.List<Point> getLocalPoints()
        + boolean containsPoint(double x, double y)
        %% + String toString()
        %% - double getLeft()
        %% - double getTop()
    }
    
    Shape <|-- Circle
    Shape <|-- Rectangle
    Shape <|-- Triangle
    Shape <|-- FilledShape
    Shape <|-- Line
    Shape <|-- Ellipse
    Shape <|-- RoundedRectangle
    Shape <|-- Group
    FilledShape <|-- Polygon
    FilledShape <|-- Text
    FilledShape <|-- Arc
    FilledShape <|-- Sector

    Group "m" --> "n" Shape : children
    World "1" --> "n" Shape : allShapes
```
This diagram shows the public API for using the graphics library:
- **Shape**: Base class for all shapes with common transformation and rendering methods
- **FilledShape**: Helper class for shapes with fill colors (provides default setters) 
- **Group**: Container for organizing multiple shapes (extends Shape)
- **World**: Manages the game world and all shapes
- **Concrete Shape Classes**: Circle, Rectangle, Triangle, Polygon, Line, Text, Arc, Ellipse, Sector, RoundedRectangle, TileImage
- **Abstract Shape Classes**: Shape, FilledShape, Group
```