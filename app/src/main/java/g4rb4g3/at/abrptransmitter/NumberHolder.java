package g4rb4g3.at.abrptransmitter;

public class NumberHolder {
  private float oldValueFloat = 0;
  private float newValueFloat = 0;

  private int oldValueInt = 0;
  private int newValueInt = 0;

  private double oldValueDouble;
  private double newValueDouble;

  public void setValues(int oldValue, int newValue) {
    this.oldValueInt = oldValue;
    this.newValueInt = newValue;
  }

  public void setValues(float oldValue, float newValue) {
    this.oldValueFloat = oldValue;
    this.newValueFloat = newValue;
  }

  public void setValues(double oldValue, double newValue) {
    this.oldValueDouble = oldValue;
    this.newValueDouble = newValue;
  }

  public boolean equals(float oldValue, float newValue) {
    return this.oldValueFloat == oldValue && this.newValueFloat == newValue;
  }

  public boolean equals(int oldValue, int newValue) {
    return this.oldValueInt == oldValue && this.newValueInt == newValue;
  }

  public boolean equals(double oldValue, double newValue) {
    return this.oldValueDouble == oldValue && this.newValueDouble == newValue;
  }
}
