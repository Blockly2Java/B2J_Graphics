/**
 * Beschreibt die grundlegenden Weltinformationen, die Grafikobjekten und Actoren zur Verfügung stehen.
 */
public interface IWorld {
    double getWidth();

    double getHeight();

    double getLeft();

    double getTop();

    void setCursor(String cursor);

    boolean hasActors();
}
