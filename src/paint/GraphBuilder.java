package paint;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleUnaryOperator;

public class GraphBuilder {
    // Кількість кроків для обчислення мінімуму й максимуму функцій:
    public static double STEPS = 100;
    // Мінімальний відступ від осей і графіку по вертикалі:
    public static double VERTICAL_GAP = 0.2;
    // Поріг видимості ліній сітки:
    public static double MIN_PIXELS = 20;
    // Поріг зміни кроку сітки:
    public static double SCALE_STEP = 10;
    // Мінімальний відступ від осей:
    public static double MIN_GAP = 2;
    // Відступ від краю малюнка:
    public static double MAX_GAP = 10;
    // Формат виведення чисел:
    public static String FORMAT = "%5.2f";

    // Приблизне обчислення мінімуму функції на деякому інтервалі:
    public static Point minPoint(double from, double to, DoubleUnaryOperator operator) {
        Point minPoint = new Point();
        double min = operator.applyAsDouble(from);
        double h = (to - from) / STEPS;
        for (double x = from + h; x <= to; x += h) {
            double y = operator.applyAsDouble(x);
            if (min > y) {
                min = y;
                minPoint.setY(min);
                minPoint.setX(x);
            }
        }
        return minPoint;
    }

    // Приблизне обчислення максимуму функції на деякому інтервалі:
    public static Point maxPoint(double from, double to, DoubleUnaryOperator operator) {
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

    // Панель, на якій відображатиметься графік:
    private transient Pane pane;

    // Кольори сітки та осей:
    private Color gridColor = Color.LIGHTGRAY;
    private Color axesColor = Color.BLACK;

    // Функції, які слід відобразити:
    private List<FunctionData<DoubleUnaryOperator>> funcs = new ArrayList<>();

    // У конструкторі обов'язково визначаємо панель для побудови графіку:
    public GraphBuilder(Pane pane) {
        this.pane = pane;
    }

    public void clearFunctions() {
        funcs.clear();
    }

    public void addFunction(DoubleUnaryOperator operator, Paint paint, double width) {
        funcs.add(new FunctionData<>(operator, paint, width));
    }

    // Побудова графіку:
    public void drawGraph(double xMin, double xMax) {
        // Визначаємо та обчислюємо діапазон функції:
        double yMin = -VERTICAL_GAP;
        double yMax = VERTICAL_GAP;
        for (FunctionData<DoubleUnaryOperator> func : funcs) {
            double min = minPoint(xMin, xMax, func.getOperator()).getY();
            double max = maxPoint(xMin, xMax, func.getOperator()).getY();
            if (yMin > min) {
                yMin = min;
            }
            if (yMax < max) {
                yMax = max;
            }
        }
        yMin -= VERTICAL_GAP * Math.abs(yMin);
        yMax += VERTICAL_GAP * Math.abs(yMax);
        // Розміри графіку:
        double width = 600;//pane.getWidth();
        double height = 550;//pane.getHeight();
        // Масштаби:
        double xScale = width / (xMax - xMin);
        double yScale = height / (yMax - yMin);
        // Координати проекції початку коордитат:
        double x0 = -xMin * xScale;
        double y0 = yMax * yScale;
        pane.getChildren().clear();

        // Сітка:
        double xStep = 1; // Крок сітки
        // Змінюємо крок, якщо лінії розташовані надто часто:
        while (xStep * xScale < MIN_PIXELS)
            xStep *= SCALE_STEP;
        // Змінюємо крок, якщо лінії розташовані надто рідко:
        while (xStep * xScale > MIN_PIXELS * SCALE_STEP)
            xStep /= SCALE_STEP;
        // Вертикальні лінії сітки
        for (double dx = xStep; dx < xMax; dx += xStep) {
            double x = x0 + dx * xScale;
            Line line = new Line(x, 0, x, height);
            line.setStroke(gridColor);
            pane.getChildren().add(line);
            pane.getChildren().add(new Text(x + MIN_GAP, MAX_GAP, String.format(FORMAT, dx)));
        }
        for (double dx = -xStep; dx >= xMin; dx -= xStep) {
            double x = x0 + dx * xScale;
            Line line = new Line(x, 0, x, height);
            line.setStroke(gridColor);
            pane.getChildren().add(line);
            pane.getChildren().add(new Text(x + MIN_GAP, MAX_GAP, String.format(FORMAT, dx)));
        }
        double yStep = 1;  // Крок сітки
        // Змінюємо крок, якщо лінії розташовані надто часто:
        while (yStep * yScale < MIN_PIXELS) {
            yStep *= SCALE_STEP;
        }
        // Змінюємо крок, якщо лінії розташовані надто рідко:
        while (yStep * yScale > MIN_PIXELS * SCALE_STEP)
            yStep /= SCALE_STEP;
        // Горизонтальні лінії сітки
        for (double dy = yStep; dy < yMax; dy += yStep) {
            double y = y0 - dy * yScale;
            Line line = new Line(0, y, width, y);
            line.setStroke(gridColor);
            pane.getChildren().add(line);
            pane.getChildren().add(new Text(MIN_GAP, y - MIN_GAP, String.format(FORMAT, dy)));
        }
        for (double dy = -yStep; dy > yMin; dy -= yStep) {
            double y = y0 - dy * yScale;
            Line line = new Line(0, y, width, y);
            line.setStroke(gridColor);
            pane.getChildren().add(line);
            pane.getChildren().add(new Text(MIN_GAP, y - MIN_GAP, String.format(FORMAT, dy)));
        }

        // Осі:
        Line verticalAxis = new Line(x0, 0, x0, height);
        verticalAxis.setStroke(axesColor);
        pane.getChildren().add(verticalAxis);
        Line horizontalAxis = new Line(0, y0, width, y0);
        pane.getChildren().add(horizontalAxis);
        pane.getChildren().add(new Text(MIN_GAP, y0 - MIN_GAP, "0.0"));
        pane.getChildren().add(new Text(MIN_GAP, y0 - MIN_GAP, "0.0"));
        pane.getChildren().add(new Text(width - MAX_GAP, y0 - MIN_GAP, "X"));
        pane.getChildren().add(new Text(x0 + MIN_GAP, MAX_GAP, "Y"));

        // Функції:
        for (FunctionData<DoubleUnaryOperator> func : funcs) {
            Polyline polyline = new Polyline();
            polyline.setStroke(func.getPaint());
            polyline.setStrokeWidth(func.getWidth());
            for (double x = 0; x < width; x++) {
                double dx = (x - x0) / xScale;
                double dy = func.getOperator().applyAsDouble(dx);
                double y = y0 - dy * yScale;
                if (y < height && y > 0)
                    polyline.getPoints().addAll(x, y);
            }
            pane.getChildren().add(polyline);
        }
    }

}
