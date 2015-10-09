package DIC.Test;

/**
 * Created with IntelliJ IDEA.
 * User: Arnab Saha
 * Date: 3/29/15
 * Time: 10:53 AM
 */
public class Polygons {
    private String[][] polygonArray = new String[100][100];
    private final int LENGTH = 10;

    public void drawPolygon(int numberOfVertices) {
        if (numberOfVertices < 3) {
            System.out.println("Number of vertices should be at least 3");
            return;
        }
        int exteriorAngleOfPolygon = 360 / numberOfVertices;
        int angle = 0;
        Position position = new Position(40, 10);

        for (int i = 0; i < numberOfVertices; i++) {
            position = drawLine(position, angle);
            angle += exteriorAngleOfPolygon;
        }
    }

    public Position drawLine(Position firstVertex, int angle) {
        int x, y;
        int x1 = x = firstVertex.getX();
        int y1 = y = firstVertex.getY();
        double slope = Math.tan(Math.toRadians(angle));
        System.out.println("Equation of the line = y - " + y1 + " = " + slope + "(x - " + x1 + ")");
        double currentLength = 0;
        while (currentLength <= LENGTH) {
            polygonArray[x][y] = " *";
            if (angle <= 45 || angle > 315) {
                x++;
                y = (int) Math.round(slope * (x - x1) + y1);
            } else if (angle <= 135 && angle > 45) {
                y++;
                x = (int) Math.round((y - y1) / slope + x1);
            } else if (angle <= 225 && angle > 135) {
                x--;
                y = (int) Math.round(slope * (x - x1) + y1);
            } else {
                y--;
                x = (int) Math.round((y - y1) / slope + x1);
            }
            currentLength = Math.sqrt(Math.pow(x1 - x, 2) + Math.pow(y1 - y, 2));
        }
        return new Position(x, y);
    }

    public void printArray() {
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                if (polygonArray[j][i] == null)
                    System.out.print("  ");
                else
                    System.out.print(polygonArray[j][i]);
            }
            System.out.println("");
        }

    }

    class Position {
        int x;
        int y;

        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        int getX() {
            return x;
        }

        void setX(int x) {
            this.x = x;
        }

        int getY() {
            return y;
        }

        void setY(int y) {
            this.y = y;
        }
    }

    public static void main(String[] args) {
        Polygons polygons = new Polygons();
        polygons.drawPolygon(3);
        polygons.printArray();
    }
}
