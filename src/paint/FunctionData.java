package paint;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.io.Serializable;
import java.util.function.DoubleUnaryOperator;

public class FunctionData<E extends  DoubleUnaryOperator>{
    private DoubleUnaryOperator operator;
    private Paint paint = Color.BROWN;
    private double width;

    public FunctionData(E operator, Paint paint, double width) {
        this.operator = operator;
        this.paint = paint;
        this.width = width;
    }

    public DoubleUnaryOperator getOperator() {
        return operator;
    }

    public Paint getPaint() {
        return paint;
    }

    public double getWidth() {
        return width;
    }
}
