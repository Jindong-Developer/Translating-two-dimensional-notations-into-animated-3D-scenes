/* @author: Jindong Gu's Bachelor Thesis in Wuppertal University.**
** This implementation class inherits the abstract "Origami" class and implements the interface.**
** The interface to calculate coordinates of the four points is implemented according to the motion*/
package yoshizawa_randlett_system;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

/*This is an instance. It takes the motion of "Inside Reverse Fold" as an example.**
**1.Define an implementation class to inherit the abstract class**
**2.Implements the interface. In other words, calculate the coordinates of four points in a certain instant according to the mathemetic relationships.**
**3.Define the main function to set up the value of the rotation axis and then call the "output_arguments" function.*/
public class Inside_Reverse_Fold extends Origami { 
    @Override
    //Implement the interface.
    public double[] coordinates(double t, double start_time, double end_time) {
        //Define the coordinates of four points and an array to store them later.
        double x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4, a, b, c;
        double[] coordinates = new double[12];
        //Assign coordinates of already known point.
        double A, A0 = 3.14, R1 = 2, R2 = 0.585786;
        //Figure out the coordinates of three of the points by tansformming of the coordinate system.
        //Point E
        A = (t - start_time) / (end_time - start_time) * A0 / 2;
        x1 = 0;
        y1 = -R1 * sin(A);
        z1 = -R1 * cos(A);
        //Point B
        x2 = 0;
        y2 = 0;
        z2 = 0;
        //Point C
        x3 = 1.414213;
        y3 = -R2 * sin(A);
        z3 = -R2 * cos(A);
        /* Calculate the coordiante of the point A acoording to three relationships.
        The point A(x, y, z) lies in Z=0 plane because of symmetry (i.e. z=0).
        The distance from the point A to the point B is 2 (i.e. x*x +y*y +z*z =4).
        The distance from the point A to the point C is 0.8284, which means the equation (x-1.4142)*(x-1.4142) + (y+R2×sin(β))*(y+R2×sin(β)) + (z+ R2×cos(β))*(z+ R2×cos(β))=0.6863.
        These equations are solvable manually thanks to the symmetry in origami. Man can also resolve these equations with MATLAB.
        */
        //Solve the equations manually.
        y4 = y3 * 5.65685 / (2 + y3 * y3);
        x4 = sqrt(4 - y4 * y4);
        z4 = 0;
        //Store the coordinates of the four points in an array.
        coordinates[0] = x1;
        coordinates[1] = y1;
        coordinates[2] = z1;
        coordinates[3] = x2;
        coordinates[4] = y2;
        coordinates[5] = z2;
        coordinates[6] = x3;
        coordinates[7] = y3;
        coordinates[8] = z3;
        coordinates[9] = x4;
        coordinates[10] = y4;
        coordinates[11] = z4;
        return coordinates;
    }
    //The main function assign the value of rotation axis and calls the output_arguments function to get cooresponding arguments.
    public static void main(String[] args) {
        Inside_Reverse_Fold instance = new Inside_Reverse_Fold();
        instance.axis = "-1 0 1 ";
        instance.output_arguments(0.82, 0.87, 0.005);
    }
}
