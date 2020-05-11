package paint;

import java.util.ArrayList;
import java.util.Comparator;

public interface Interpolation {
    /**
     * function to calculate values of F(x)
     * @see Function
     */
    void Interpolate();

    /**
     * Defines domain where graph  exist
     * @param funcF
     * @param funcG
     * @return all points sorted in decreasing order
     * @see Equation
     */
    static ArrayList<Double> domainOfDefinition(ArrayList<Point> funcF, ArrayList<Point> funcG) {
        ArrayList<Double> domainOfDefinition = new ArrayList<>();
        for (int i = 0; i < funcG.size(); i++) {
            boolean check = true;
            for (Double aDouble : domainOfDefinition) {
                if (funcG.get(i).getX() == aDouble)
                    check = false;
            }
            if (check)
                domainOfDefinition.add(funcG.get(i).getX());

        }

        domainOfDefinition.sort(new Comparator<Double>() {
            @Override
            public int compare(Double o1, Double o2) {
                return Double.compare(o1, o2);
            }
        });
        return domainOfDefinition;
    }
}
