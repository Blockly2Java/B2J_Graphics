//import Ausgabe.Circle;

/**
Erstelle dein Programm über Blockly und
klicke auf 'Play', um es auszuführen!
*/

public class B2J_Graphics_DiscoKugel extends Circle { 
   public B2J_Graphics_DiscoKugel(double x, double y) {
      super(x, y, Math.random() * 500);
   }

   public void act() {
      //setRadius(Math.random()*500); // diese Methode gibt es insgesamt nicht, da musst du einen anderen Weg finden (siehe VSCode Autocomplete) (laut Klassen Diagramm schon)
      setFillColor(Color.randomColor(), Math.random() * 255); // Color.random() gibt es eigentlich schon, fehlt nur bisher in meiner Library (ergänze ich zeitnah)
   }

}