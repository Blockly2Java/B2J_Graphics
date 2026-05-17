public interface MouseListener {
    void onMouseUp(double x, double y, int button);

    void onMouseDown(double x, double y, int button);

    void onMouseMove(double x, double y);

    void onMouseEnter(double x, double y);

    void onMouseLeave(double x, double y);
}
