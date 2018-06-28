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
public class Hegarty2001 implements S4Classification {

    String description = "1 - S4 >= 0.9 - high\n"
            + "2 - 0.6 <= S4 < 0.9 - medium\n"
            + "3 - 0.4 <= S4 < 0.6 - low\n"
            + "4 - S4 < 0.4 - very low";

    @Override
    public float calculateLabel(float s4Value) {
        if (s4Value >= 0.9) {
            return 1.f;
        } else {
            if (0.6 <= s4Value && s4Value < 0.9) {
                return 2.f;
            } else {
                if (0.4 <= s4Value && s4Value < 0.6) {
                    return 3.f;
                } else {
                    return 4.f;
                }
            }
        }
    }
    
    @Override
    public String getDescription(){
        return description;
    }

}
