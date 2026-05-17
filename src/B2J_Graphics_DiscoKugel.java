/**
 * Demo-Form, die beim Animieren wächst und schrumpft und auf Tastatur- sowie Maus-Eingaben reagiert.
 */
public class B2J_Graphics_DiscoKugel extends Circle { 
   private double dr = 10;

   /**
    * Erstellt die Demo-Scheibe an der angegebenen Position.
    */
   public B2J_Graphics_DiscoKugel(double x, double y) {
      super(x, y, Math.random() * 500);
   }

   /**
    * Animiert den Radius der Scheibe und ändert jede Frame ihre Farbe.
    */
   @Override
   public void act() {
      double r = getRadius();
      if(r > 300 && dr > 0) {
         dr = -dr;
      }
      else if(r < 50 && dr < 0) {
         dr = -dr;
      }
      setRadius(r+dr); 
      setFillColor(Color.randomColor());
   }

   /**
    * Moves the disc with the WASD keys.
    */
   @Override
   public void onKeyDown(String key) {
      switch (key) {
          case "d" -> move(5, 0);
          case "a" -> move(-5, 0);
          case "w" -> move(0, -5);
          case "s" -> move(0, 5);
      }
   }

   /**
    * Repositions the disc to the mouse click location.
    */
   @Override
   public void onMouseDown(double x, double y, int button) {
      setX(x);
      setY(y);
   }
}