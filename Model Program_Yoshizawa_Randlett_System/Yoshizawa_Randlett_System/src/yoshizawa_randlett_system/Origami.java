/* @author: Jindong Gu's Bachelor Thesis in Wuppertal University.**
** This program is developed to calculate the angles of rotation in the OrientationInterpolator node of X3D file.**
** This program includes an abstract class, which encapsulates an interface and some functions that have already been implemented.**
** Besides, the program presents an implementation class that demonstrates how to inherit the abstract class and realize its functionalities.*/

package yoshizawa_randlett_system;

import static java.lang.Math.acos;
import static java.lang.Math.sqrt;
import static java.lang.Math.abs;

/* An abstract class that includes an interface and some functions is defined as follows.**
** This abstract class must be inherited later.*/
public abstract class Origami {
    //A rotation axis is defined, which can be changed in the implementation class. 
    public String axis = "0 0 0 ";
    
    // The purpose of this abstract function is to calculate all the point coordinates in instant t.
    // This interface must be also implemented in accordance with the concrete rotation of a portion of a paper model.
    public abstract double[] coordinates(double t, double start_time, double end_time);
    
    // If the only interface is implemented correctly, user can call this function with corresponding arguments to get results.
    public void output_arguments(double start_time, double end_time, double time_unit) {
        
        //The time is time threshold, namely, the instant when the biggest angle (about π /2) between the two faces occurs.
        double t_threshold = 0;
        //Define array to store the coordinates of four points.
        //The two of them lie in the line of the intersection between two faces.
        double[] coordinates = new double[12];
        //Define a varible to store the value of the angle in various situations.
        double angle;
        //Output arguments as  relative time in the "key" field of OrientationInterpolator node
        System.out.print("<OrientationInterpolator DEF=\"name\" key=\"");
        for (double t = start_time; t < end_time + time_unit; t = t + time_unit) {
            System.out.format("%.4f  ", t);
        }
        System.out.format("\"  \n");

        //Figure out the time thresold.
        t_threshold = time_threshold(start_time, end_time, time_unit);

        //Output arguments in the "keyvalue" field of OrientationInterpolator node
        //The rotation axis can be given directly. The angle of rotation angle in any instant can be calculated and then transformed to arguments.
        System.out.print("keyvalue=\"");
        for (double t = start_time; t < end_time + time_unit; t = t + time_unit) {
            //Calculate the coordiantes of the four points.
            coordinates = coordinates(t, start_time, end_time);
            //Calculate the acute angle between the two faces of rotation.
            angle = faces_angle(coordinates);
            //Transform the angles into arguments we need.
            angle = rotation_angle(angle, t, t_threshold);
            // output rotation axis and angle arguments
            System.out.format(axis + "%.4f  ", angle);
        }
        System.out.print("\"/>");
    }
    //Slect the instant, which is closest to the π /2.
    public double time_threshold(double start_time, double end_time, double time_unit) {
        //Micrify the time unit
        time_unit = time_unit / 10;
        //Define the time threshold, in which the value of anlge is π /2
        double t_threshold = 0;
        //Define and initial the array to store the two consequent angles
        double[] angle = new double[2];
        double[] coordinates = new double[12];
        angle[0] = 0.0;
        angle[1] = 0.0;
        for (double t = start_time; t < end_time + time_unit; t = t + time_unit) {
            coordinates = coordinates(t, start_time, end_time);
            angle[1] = faces_angle(coordinates);
            if (angle[1] < angle[0]) {
                 t_threshold = t;
                break;   
            } else {
               angle[0] = angle[1];
            }
        }
        return t_threshold;
       
    }
    
    //Transform the angles into arguments we need with the help of the time threshold
    public double rotation_angle(double angle, double t, double t_threshold) {
        if (t > t_threshold) {
            angle = 3.14159256 - angle;
        }
        return angle;
    }
    
    //Given the coordinates of four points, calculate the acute angle between the two faces of rotation.
    public double faces_angle(double[] coordinates) {
        double cosA, angle;
        //Define two normal vectors of the two faces of rotation respectively.
        double[] vektor1 = new double[3];
        double[] vektor2 = new double[3];
        //Calculate the normal vectors
        vektor1 = noraml_vektor(coordinates[0], coordinates[1], coordinates[2], coordinates[3], coordinates[4], coordinates[5], coordinates[6], coordinates[7], coordinates[8]);
        vektor2 = noraml_vektor(coordinates[3], coordinates[4], coordinates[5], coordinates[6], coordinates[7], coordinates[8], coordinates[9], coordinates[10], coordinates[11]);
        //Calcute the angle between the two vectors.
        cosA = (vektor1[0] * vektor2[0] + vektor1[1] * vektor2[1] + vektor1[2] * vektor2[2]) / (sqrt(vektor1[0] * vektor1[0] + vektor1[1] * vektor1[1] + vektor1[2] * vektor1[2]) * sqrt(vektor2[0] * vektor2[0] + vektor2[1] * vektor2[1] + vektor2[2] * vektor2[2]));
        angle = acos(cosA);
        //Transform the angle between two normal vectors into the acute angle between the two faces of rotation.
        if (angle >= 1.57079628) {
            angle = 3.14159625 - angle;
        }
        return angle;
    }

    //Calculate  the normal vectors, given coordinates of the three points that lie in a same plane.
    public double[] noraml_vektor(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3) {
        //Calculate the two vectors lying on the plane.
        double a, b, c, d, e, f;
        a = x1 - x2;
        b = y1 - y2;
        c = z1 - z2;
        d = x1 - x3;
        e = y1 - y3;
        f = z1 - z3;
        //Define and calculate the noraml vectors.
        double[] vektor = new double[3];
        vektor[2] = (a * e - b * d) / (c * d - f * a);
        vektor[1] = 1;
        vektor[0] = (c * e - f * b) / (f * a - c * d);
        if (abs(vektor[0] / vektor[1]) > 10000) {
            vektor[2] = (b * d - e * a) / (e * c - b * f);
            vektor[1] = (f * a - c * d) / (c * e - f * b);
            vektor[0] = 1;
            if (abs(vektor[0] / vektor[2]) > 10000) {
                vektor[2] = 1;
                vektor[0] = (b * f - e * c) / (e * a - b * d);
                vektor[1] = (a * f - d * c) / (b * d - a * e);
            }
        }
        return vektor;
    }
}
