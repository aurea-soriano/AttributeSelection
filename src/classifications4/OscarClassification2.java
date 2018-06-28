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
public class OscarClassification2 implements S4Classification {

    String description = "4 - 3.34 <=value  \n"
            + "3 - 2.87 <= value < 3.34\n"
            + "2 - 2.2 <= value < 2.87 \n"
            + "1 -        value < 2.2 \n";

    @Override
    public float calculateLabel(float value) {
        if (3.34 <= value) {
            return 4.f;
        } else {
            if (2.87 <= value && value < 3.34) {
                return 3.f;
            } else {
                if (2.2 <= value && value < 2.87) {
                    return 2.f;
                } else {
                    return 1.f;
                }
            }
        }
    }

    @Override
    public String getDescription() {
        return description;
    }
}
