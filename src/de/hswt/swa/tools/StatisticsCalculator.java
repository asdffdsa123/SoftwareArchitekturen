package de.hswt.swa.tools;


import java.util.Enumeration;
import java.util.Vector;

public class StatisticsCalculator { //implements StatisticsCalculatorService {
  private Vector values;

  public void setValues(Vector values) {
    this.values = values;
  }
  
  public double getStandardDeviation() {
    if ((values == null) || (values.size() <= 1))
      return 0;

    double sum = 0;
    
    Enumeration e = values.elements();
    while (e.hasMoreElements()) {
      Double value = (Double) e.nextElement();
      double dValue = value.doubleValue();
      sum += dValue;
    }

    double mean = sum/values.size();
    double tmp = 0;

    e = values.elements();
    while (e.hasMoreElements()) {
      Double value = (Double) e.nextElement();
      tmp += Math.pow(value - mean,2.0);
    }

    double stddev = Math.sqrt(tmp/(values.size() - 1)); 
    System.out.println("Ergebnis f�r " + values + " " + stddev);
    return stddev;
  }
}