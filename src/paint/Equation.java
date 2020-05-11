package paint;

import java.util.ArrayList;

/**
 * Class represent finder function
 */

public class Equation {
    private ArrayList<Point> funcDifference;

    /**
     * Construct f(x)-g(x)
     * @param funcF
     * @param funcG
     */

    public Equation(ArrayList<Point> funcF, ArrayList<Point> funcG) {
        this.funcDifference = new ArrayList<>();
        funcDiff(funcF, funcG);
    }

    private void funcDiff(ArrayList<Point> funcF, ArrayList<Point> funcG) {
        for (int i = 0; i < funcF.size(); i++) {
            funcDifference.add(new Point(funcF.get(i).getX(), funcF.get(i).getY() - funcG.get(i).getY()));
        }

    }

    public ArrayList<Point> getFuncDifference() {
        return funcDifference;
    }
}
