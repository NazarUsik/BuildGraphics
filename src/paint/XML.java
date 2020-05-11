package paint;

import java.util.ArrayList;

public abstract class XML<E> {
    private ArrayList<E> arrayOfPoints;

    XML() {
        this.arrayOfPoints = new ArrayList<>();
    }

    XML(ArrayList<E> arrayOfPoints) {
        this.arrayOfPoints = new ArrayList<>(arrayOfPoints);
    }

    protected abstract void fromXML(String nameFile);

    public ArrayList<E> getArrayOfPointsXml() {
        return arrayOfPoints;
    }

    public void add(E element) {
        this.arrayOfPoints.add(element);
    }
}
