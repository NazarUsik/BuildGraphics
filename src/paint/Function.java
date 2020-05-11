package paint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.function.DoubleUnaryOperator;

/**
 * Class represent usage of main processes
 */
public class Function extends XMLReader implements MaxOnFunction, Interpolation, DoubleUnaryOperator, Serializable {
    private ArrayList<Point> arrayOfPoints;
    private ArrayList<Point> lagrangeFunc;
    private static transient ArrayList<Double> domainOfDefinition;

    Function(Function fX) {
        this.arrayOfPoints = new ArrayList<>(fX.arrayOfPoints);
        this.lagrangeFunc = new ArrayList<>(fX.lagrangeFunc);
        domainOfDefinition = new ArrayList<>();
    }

    Function(String nameFile) {
        super(nameFile);
        arrayOfPoints = new ArrayList<>(super.getArrayOfPointsXml());
        this.lagrangeFunc = new ArrayList<>();
        domainOfDefinition = new ArrayList<>();
    }

    Function(ArrayList<Point> arrayOfPoints) {
        this.arrayOfPoints = new ArrayList<>(arrayOfPoints);
        this.lagrangeFunc = new ArrayList<>();
        domainOfDefinition = new ArrayList<>();
    }

    Function() {
        this.arrayOfPoints = new ArrayList<>();
        this.lagrangeFunc = new ArrayList<>();
        domainOfDefinition = new ArrayList<>();
    }

    public ArrayList<Point> getArrayOfPoints() {
        return this.arrayOfPoints;
    }

    public ArrayList<Point> getLagrangeFunc() {
        return lagrangeFunc;
    }

    public ArrayList<Double> getDomainOfDefinition() {
        return domainOfDefinition;
    }

    public static void setDomainOfDefinition(ArrayList<Point> funcF, ArrayList<Point> funcG) {
        domainOfDefinition = new ArrayList<>(Interpolation.domainOfDefinition(funcF, funcG));
    }

    public static void setDomainOfDefinition(ArrayList<Double> funcF) {
        domainOfDefinition = new ArrayList<>(funcF);
    }

    public void writeFuncInXML(String nameFile) {
        XMLWriter writer = new XMLWriter(this.arrayOfPoints, nameFile);
    }

    public void writeLagrangeFuncInXML(String nameFile) {
        try {
            XMLWriter writer = new XMLWriter(this.lagrangeFunc, nameFile);
        } catch (NullPointerException ex) {
            System.out.println("First run to InterpolateLagrangePolynomial method");
        }
    }

    /**
     * find max value in intreval [a,b] using division by smaller parts
     * in neighbour +-epsilon
     *
     * @return finder max values on interval
     */
    @Override
    public Point findMaxToDichotomy() {
        Function operator = new Function(this);
        ArrayList<Point> arrayOfPoints = new ArrayList<>(operator.getArrayOfPoints());
        double from = arrayOfPoints.get(0).getX();
        double to = arrayOfPoints.get(arrayOfPoints.size() - 1).getX();
        double STEPS = 100;
        Point maxPoint = new Point();
        double max = operator.applyAsDouble(from);
        double h = (to - from) / STEPS;
        for (double x = from + h; x <= to; x += h) {
            double y = operator.applyAsDouble(x);
            if (max < y) {
                max = y;
                maxPoint.setX(x);
                maxPoint.setY(max);
            }
        }
        return maxPoint;
    }

    @Override
    public void writeMaxInXML(String nameFile) {
        try {
            ArrayList<Point> tmp = new ArrayList<>();
            tmp.add(new Point(this.findMaxToDichotomy()));
            XMLWriter writer = new XMLWriter(tmp, nameFile);
        } catch (NullPointerException ex) {
            System.out.println("Error in write Max to XML");
        }
    }

    /**
     * Lagrange polynomial implementation
     * Calculates intermediate value of func at its argument between x(n) ...x(n+1)
     * value x from domainOfDefinition
     * L(x) = sum [i=0,     y(i)L{i,x}
     * i->inf]
     * L{i,x} = mult[j=0,  x-x(j)/ x(i)-x(j)
     * j!=i]
     *
     * @see Interpolation
     */
    @Override
    public void Interpolate() {
        //___________________________________________________________________________________________
        ArrayList<Point> points = new ArrayList<>(this.getArrayOfPoints());
        ArrayList<Double> lagrangePol = new ArrayList<>();
        for (Double aDouble : domainOfDefinition) {
            double sum = 0;
            for (int i = 0; i < points.size(); i++) {
                double basicsPol = 1;
                for (int j = 0; j < points.size(); j++) {
                    if (j != i) {
                        basicsPol *= (aDouble - points.get(j).getX()) / (points.get(i).getX() - points.get(j).getX());
                    }
                }
                sum += basicsPol * points.get(i).getY();
            }
            lagrangePol.add(sum);
        }
        //______________________________________________________________________________________________

        for (int i = 0; i < domainOfDefinition.size(); i++) {
            lagrangeFunc.add(new Point(domainOfDefinition.get(i), lagrangePol.get(i)));
        }
    }


    @Override
    public double applyAsDouble(double v) {
        Function funkToLag = new Function(this.arrayOfPoints);
        ArrayList<Double> d = new ArrayList<>();
        d.add(v);
        setDomainOfDefinition(d);
        funkToLag.Interpolate();
        return funkToLag.getLagrangeFunc().get(0).getY();

    }
    /**
     * represent coordinates to build graph
     *
     * @param v
     * @return values to draw a graph
     * @see Main
     */

}
