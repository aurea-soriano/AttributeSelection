/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classifications4;

/**
 *
 * @author aurea
 */
public class Tiwari2011 implements S4Classification {

    String description = "1 - S4 >= 1.0 - high\n"
            + "2 - 0.5 <= S4 < 1.0 - medium\n"
            + "3 - S4 < 0.5 - low\n";

    @Override
    public float calculateLabel(float s4Value) {
        if (s4Value >= 1.0) {
            return 1.f;
        } else {
            if (0.5 <= s4Value && s4Value < 1.0) {
                return 2.f;
            } else {
                return 3.f;
            }
        }
    }

    @Override
    public String getDescription() {
        return description;
    }
}
